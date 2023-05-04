package io.github.evercraftmc.evercraft.spigot.games.pvp;

import java.util.Arrays;
import org.bukkit.entity.Player;
import io.github.evercraftmc.evercraft.spigot.commands.kit.KitCommand;
import io.github.evercraftmc.evercraft.spigot.games.Game;

public class PvpGame extends Game {
    protected String kitName;

    public PvpGame(String name, String warpName, String kitName) {
        super(name, warpName, 0, Integer.MAX_VALUE);

        this.kitName = kitName;
    }

    @Override
    public void join(Player player) {
        super.join(player);

        new KitCommand("kit", null, Arrays.asList(), null, true).run(player, new String[] { kitName });
    }
}