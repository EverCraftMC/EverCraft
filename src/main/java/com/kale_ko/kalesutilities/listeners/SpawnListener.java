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
    File dataFile;
    YamlConfiguration data;

    public SpawnListener() {
        File dataFolder = Main.Instance.getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }

        dataFile = Paths.get(dataFolder.getAbsolutePath(), "spawn.yml").toFile();

        data = YamlConfiguration.loadConfiguration(dataFile);
    }

    @EventHandler
    public void onPlayerSpawn(PlayerSpawnLocationEvent event) {
        event.setSpawnLocation(new Location(event.getSpawnLocation().getWorld(), data.getDouble(event.getSpawnLocation().getWorld().getName() + ".x"), data.getDouble(event.getSpawnLocation().getWorld().getName() + ".y"), data.getDouble(event.getSpawnLocation().getWorld().getName() + ".z"), Float.parseFloat(data.getString(event.getSpawnLocation().getWorld().getName() + ".pitch")), Float.parseFloat(data.getString(event.getSpawnLocation().getWorld().getName() + ".yaw"))));
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        event.setRespawnLocation(new Location(event.getRespawnLocation().getWorld(), data.getDouble(event.getRespawnLocation().getWorld().getName() + ".x"), data.getDouble(event.getRespawnLocation().getWorld().getName() + ".y"), data.getDouble(event.getRespawnLocation().getWorld().getName() + ".z"), Float.parseFloat(data.getString(event.getRespawnLocation().getWorld().getName() + ".pitch")), Float.parseFloat(data.getString(event.getRespawnLocation().getWorld().getName() + ".yaw"))));
    }
}