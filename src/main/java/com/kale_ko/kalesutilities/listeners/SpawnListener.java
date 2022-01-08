package com.kale_ko.kalesutilities.listeners;

import com.kale_ko.kalesutilities.Main;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class SpawnListener implements Listener {
    @EventHandler
    public void onPlayerSpawn(PlayerSpawnLocationEvent event) {
        if (Main.Instance.spawn.getBoolean("overidespawn")) {
            event.setSpawnLocation(new Location(event.getSpawnLocation().getWorld(), Main.Instance.spawn.getDouble(event.getSpawnLocation().getWorld().getName() + ".x"), Main.Instance.spawn.getDouble(event.getSpawnLocation().getWorld().getName() + ".y"), Main.Instance.spawn.getDouble(event.getSpawnLocation().getWorld().getName() + ".z"), Float.parseFloat(Main.Instance.spawn.getString(event.getSpawnLocation().getWorld().getName() + ".pitch")), Float.parseFloat(Main.Instance.spawn.getString(event.getSpawnLocation().getWorld().getName() + ".yaw"))));
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (Main.Instance.spawn.getBoolean("overidespawn")) {
            event.setRespawnLocation(new Location(event.getRespawnLocation().getWorld(), Main.Instance.spawn.getDouble(event.getRespawnLocation().getWorld().getName() + ".x"), Main.Instance.spawn.getDouble(event.getRespawnLocation().getWorld().getName() + ".y"), Main.Instance.spawn.getDouble(event.getRespawnLocation().getWorld().getName() + ".z"), Float.parseFloat(Main.Instance.spawn.getString(event.getRespawnLocation().getWorld().getName() + ".pitch")), Float.parseFloat(Main.Instance.spawn.getString(event.getRespawnLocation().getWorld().getName() + ".yaw"))));
        }
    }
}