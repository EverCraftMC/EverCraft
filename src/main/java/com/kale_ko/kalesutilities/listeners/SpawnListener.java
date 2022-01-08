package com.kale_ko.kalesutilities.listeners;

import com.kale_ko.kalesutilities.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class SpawnListener implements Listener {
    @EventHandler
    public void onPlayerSpawn(PlayerSpawnLocationEvent event) {
        if (Main.Instance.spawn.getBoolean("overidespawn")) {
            event.setSpawnLocation(Main.Instance.spawn.getLocation(event.getSpawnLocation().getWorld().getName()));
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (Main.Instance.spawn.getBoolean("overidespawn")) {
            event.setRespawnLocation(Main.Instance.spawn.getLocation(event.getRespawnLocation().getWorld().getName()));
        }
    }
}