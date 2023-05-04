package io.github.evercraftmc.evercraft.spigot.games.spleef;

import java.util.Arrays;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import io.github.evercraftmc.evercraft.spigot.commands.kit.KitCommand;
import io.github.evercraftmc.evercraft.spigot.games.RoundedGame;

public class SpleefGame extends RoundedGame {
    protected String kitName;

    protected Material deathBlock;

    public SpleefGame(String name, String warpName, Integer countdownLength, String kitName, Material deathBlock) {
        super(name, warpName, 0, Integer.MAX_VALUE, countdownLength);

        this.kitName = kitName;

        this.deathBlock = deathBlock;
    }

    @Override
    public void join(Player player) {
        super.join(player);

        if (this.kitName != null) {
            new KitCommand("kit", null, Arrays.asList(), null, true).run(player, new String[] { kitName });
        }
    }

    @Override
    public void tick() {
        super.tick();

        for (Player player : this.players) {
            if (player.getLocation().getBlock().getType() == this.deathBlock) {
                player.damage(Integer.MAX_VALUE);
            }
        }
    }
}