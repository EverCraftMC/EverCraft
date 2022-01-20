package com.kale_ko.kalesutilities.spigot.listeners;

import com.kale_ko.kalesutilities.spigot.Main;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class SpawnListener implements Listener {
    @EventHandler
    public void onPlayerSpawn(PlayerSpawnLocationEvent event) {
        if (Main.Instance.spawn.getBoolean("overidespawn")) {
            event.setSpawnLocation(Main.Instance.spawn.getSerializable(event.getSpawnLocation().getWorld().getName(), Location.class));
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (Main.Instance.spawn.getBoolean("overidespawn")) {
            event.setRespawnLocation(Main.Instance.spawn.getSerializable(event.getRespawnLocation().getWorld().getName(), Location.class));
        }
    }
}