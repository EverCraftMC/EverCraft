package com.kale_ko.kalesutilities.listeners;

import com.kale_ko.kalesutilities.Main;
import java.io.File;
import java.nio.file.Paths;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public class BannedJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerLoginEvent event) {
        File dataFolder = Main.Instance.getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }

        File dataFile = Paths.get(dataFolder.getAbsolutePath(), "players.yml").toFile();

        YamlConfiguration data = YamlConfiguration.loadConfiguration(dataFile);

        if (data.getBoolean("players." + event.getPlayer().getName() + ".banned")) {
            event.setKickMessage(data.getString("players." + event.getPlayer().getName() + ".banMessage"));
            event.disallow(Result.KICK_BANNED, data.getString("players." + event.getPlayer().getName() + ".banMessage"));
        }
    }
}