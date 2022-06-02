package io.github.evercraftmc.evercraft.spigot.games.pvp;

import java.util.Arrays;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import io.github.evercraftmc.evercraft.spigot.commands.kit.KitCommand;
import io.github.evercraftmc.evercraft.spigot.games.Game;

public class PvpGame extends Game {
    protected String kitName;

    public PvpGame(String name, Location location, String kitName) {
        super(name, location, 1f, Float.MAX_VALUE);

        this.kitName = kitName;
    }

    @Override
    public void join(Player player) {
        new KitCommand("kit", null, Arrays.asList(), null).run(player, new String[] { kitName });
    }
}