package com.kale_ko.kalesutilities.listeners;

import com.kale_ko.kalesutilities.Main;
import java.io.File;
import java.nio.file.Paths;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class SpawnListener implements Listener {
    @EventHandler
    public void onPlayerSpawn(PlayerSpawnLocationEvent event) {
        File dataFolder = Main.Instance.getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }

        File dataFile = Paths.get(dataFolder.getAbsolutePath(), "spawn.yml").toFile();

        YamlConfiguration data = YamlConfiguration.loadConfiguration(dataFile);

        event.setSpawnLocation(new Location(event.getSpawnLocation().getWorld(), data.getDouble(event.getSpawnLocation().getWorld().getName() + ".x"), data.getDouble(event.getSpawnLocation().getWorld().getName() + ".y"), data.getDouble(event.getSpawnLocation().getWorld().getName() + ".z"), Float.parseFloat(data.getString(event.getSpawnLocation().getWorld().getName() + ".pitch")), Float.parseFloat(data.getString(event.getSpawnLocation().getWorld().getName() + ".yaw"))));
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        File dataFolder = Main.Instance.getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }

        File dataFile = Paths.get(dataFolder.getAbsolutePath(), "spawn.yml").toFile();

        YamlConfiguration data = YamlConfiguration.loadConfiguration(dataFile);

        event.setRespawnLocation(new Location(event.getRespawnLocation().getWorld(), data.getDouble(event.getRespawnLocation().getWorld().getName() + ".x"), data.getDouble(event.getRespawnLocation().getWorld().getName() + ".y"), data.getDouble(event.getRespawnLocation().getWorld().getName() + ".z"), Float.parseFloat(data.getString(event.getRespawnLocation().getWorld().getName() + ".pitch")), Float.parseFloat(data.getString(event.getRespawnLocation().getWorld().getName() + ".yaw"))));
    }
}