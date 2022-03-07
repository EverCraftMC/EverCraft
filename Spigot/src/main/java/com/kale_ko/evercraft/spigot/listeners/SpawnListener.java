package com.kale_ko.evercraft.spigot.listeners;

import com.kale_ko.evercraft.spigot.SpigotPlugin;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class SpawnListener implements Listener {
    @EventHandler
    public void onPlayerSpawn(PlayerSpawnLocationEvent event) {
        if (SpigotPlugin.Instance.spawn.getBoolean("overidespawn")) {
            event.setSpawnLocation(SpigotPlugin.Instance.spawn.getSerializable(event.getSpawnLocation().getWorld().getName(), Location.class));
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (SpigotPlugin.Instance.spawn.getBoolean("overidespawn")) {
            event.setRespawnLocation(SpigotPlugin.Instance.spawn.getSerializable(event.getRespawnLocation().getWorld().getName(), Location.class));
        }
    }
}