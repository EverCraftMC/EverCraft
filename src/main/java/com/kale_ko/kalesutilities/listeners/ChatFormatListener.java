package com.kale_ko.kalesutilities.listeners;

import com.kale_ko.kalesutilities.Main;
import com.kale_ko.kalesutilities.Util;
import java.io.File;
import java.nio.file.Paths;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

@SuppressWarnings("deprecation")
public class ChatFormatListener implements Listener {
    @EventHandler
    public void onChatMessage(PlayerChatEvent event) {
        File dataFolder = Main.Instance.getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }

        File dataFile = Paths.get(dataFolder.getAbsolutePath(), "players.yml").toFile();

        YamlConfiguration data = YamlConfiguration.loadConfiguration(dataFile);

        if (Util.hasPermission(event.getPlayer(), "kalesutilities.colorchat")) {
            event.setMessage(Util.formatMessage(event.getMessage()));
        }

        if (data.getString("players." + event.getPlayer().getName() + ".prefix") == null) {
            event.setFormat(event.getPlayer().getName() + " > " + event.getMessage());
        } else {
            event.setFormat(Util.formatMessage(data.getString("players." + event.getPlayer().getName() + ".prefix")) + " " + event.getPlayer().getName() + " > " + event.getMessage());
        }
    }
}