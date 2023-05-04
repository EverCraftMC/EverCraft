package io.github.evercraftmc.evercraft.spigot.games;

import java.util.Arrays;
import org.bukkit.entity.Player;
import io.github.evercraftmc.evercraft.spigot.commands.kit.KitCommand;

public class KittedGame extends Game {
    protected String kitName;

    public KittedGame(String name, String warpName, String kitName) {
        super(name, warpName, 1f, Float.MAX_VALUE);

        this.kitName = kitName;
    }

    public String getKitName() {
        return this.kitName;
    }

    @Override
    public void join(Player player) {
        super.join(player);

        if (this.kitName != null) {
            new KitCommand("kit", null, Arrays.asList(), null).run(player, new String[] { this.kitName, "true" });
        }
    }
}