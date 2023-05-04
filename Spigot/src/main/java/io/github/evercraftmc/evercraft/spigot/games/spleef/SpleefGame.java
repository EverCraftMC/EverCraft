package io.github.evercraftmc.evercraft.spigot.games.spleef;

import org.bukkit.Material;
import io.github.evercraftmc.evercraft.spigot.games.pvp.SumoGame;

public class SpleefGame extends SumoGame {
    public SpleefGame(String name, String warpName, String kitName, Material deathBlock) {
        super(name, warpName, kitName, deathBlock, Integer.MAX_VALUE);
    }

    // TODO
}