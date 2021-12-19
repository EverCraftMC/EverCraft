package com.kale_ko.kalesutilities.listeners;

import com.kale_ko.kalesutilities.Main;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class SeenListener implements Listener {
    File dataFile;
    YamlConfiguration data;

    public SeenListener() {
        File dataFolder = Main.Instance.getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }

        dataFile = Paths.get(dataFolder.getAbsolutePath(), "seen.yml").toFile();

        data = YamlConfiguration.loadConfiguration(dataFile);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws IOException {
        data.set("players." + event.getPlayer().getName(), new Date().getTime());

        data.save(dataFile);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) throws IOException {
        data.set("players." + event.getPlayer().getName(), new Date().getTime());

        data.save(dataFile);
    }
}