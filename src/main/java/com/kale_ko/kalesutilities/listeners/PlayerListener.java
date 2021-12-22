package com.kale_ko.kalesutilities.listeners;

import com.kale_ko.kalesutilities.Main;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws IOException {
        File dataFolder = Main.Instance.getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }

        File dataFile = Paths.get(dataFolder.getAbsolutePath(), "players.yml").toFile();

        YamlConfiguration data = YamlConfiguration.loadConfiguration(dataFile);

        if (data.getString("players." + event.getPlayer().getName() + ".nickname") == null) {
            data.set("players." + event.getPlayer().getName() + ".nickname", event.getPlayer().getName());
        }

        if (data.getString("players." + event.getPlayer().getName() + ".prefix") == null) {
            data.set("players." + event.getPlayer().getName() + ".prefix", "");
        }

        data.save(dataFile);
    }
}