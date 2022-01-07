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
        event.setSpawnLocation(new Location(event.getSpawnLocation().getWorld(), Main.Instance.players.getConfig().getDouble(event.getSpawnLocation().getWorld().getName() + ".x"), Main.Instance.players.getConfig().getDouble(event.getSpawnLocation().getWorld().getName() + ".y"), Main.Instance.players.getConfig().getDouble(event.getSpawnLocation().getWorld().getName() + ".z"), Float.parseFloat(Main.Instance.players.getConfig().getString(event.getSpawnLocation().getWorld().getName() + ".pitch")), Float.parseFloat(Main.Instance.players.getConfig().getString(event.getSpawnLocation().getWorld().getName() + ".yaw"))));
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        event.setRespawnLocation(new Location(event.getRespawnLocation().getWorld(), Main.Instance.players.getConfig().getDouble(event.getRespawnLocation().getWorld().getName() + ".x"), Main.Instance.players.getConfig().getDouble(event.getRespawnLocation().getWorld().getName() + ".y"), Main.Instance.players.getConfig().getDouble(event.getRespawnLocation().getWorld().getName() + ".z"), Float.parseFloat(Main.Instance.players.getConfig().getString(event.getRespawnLocation().getWorld().getName() + ".pitch")), Float.parseFloat(Main.Instance.players.getConfig().getString(event.getRespawnLocation().getWorld().getName() + ".yaw"))));
    }
}