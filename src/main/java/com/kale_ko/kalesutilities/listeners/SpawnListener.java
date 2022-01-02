package com.kale_ko.kalesutilities.listeners;

import com.kale_ko.kalesutilities.Config;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class SpawnListener implements Listener {
    @EventHandler
    public void onPlayerSpawn(PlayerSpawnLocationEvent event) {
        YamlConfiguration data = Config.load("players.yml").getConfig();

        event.setSpawnLocation(new Location(event.getSpawnLocation().getWorld(), data.getDouble(event.getSpawnLocation().getWorld().getName() + ".x"), data.getDouble(event.getSpawnLocation().getWorld().getName() + ".y"), data.getDouble(event.getSpawnLocation().getWorld().getName() + ".z"), Float.parseFloat(data.getString(event.getSpawnLocation().getWorld().getName() + ".pitch")), Float.parseFloat(data.getString(event.getSpawnLocation().getWorld().getName() + ".yaw"))));
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        YamlConfiguration data = Config.load("players.yml").getConfig();

        event.setRespawnLocation(new Location(event.getRespawnLocation().getWorld(), data.getDouble(event.getRespawnLocation().getWorld().getName() + ".x"), data.getDouble(event.getRespawnLocation().getWorld().getName() + ".y"), data.getDouble(event.getRespawnLocation().getWorld().getName() + ".z"), Float.parseFloat(data.getString(event.getRespawnLocation().getWorld().getName() + ".pitch")), Float.parseFloat(data.getString(event.getRespawnLocation().getWorld().getName() + ".yaw"))));
    }
}