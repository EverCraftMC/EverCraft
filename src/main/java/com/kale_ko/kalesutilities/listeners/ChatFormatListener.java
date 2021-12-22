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

        String player = event.getPlayer().getName();
        String prefix = "";
        String message = event.getMessage();

        if (data.getString("players." + event.getPlayer().getName() + ".nickname") != null) {
            if (Util.hasPermission(event.getPlayer(), "kalesutilities.nonickstar")) {
                player = Util.formatMessage(data.getString("players." + event.getPlayer().getName() + ".nickname") + "&r");
            } else {
                player = Util.formatMessage("*" + data.getString("players." + event.getPlayer().getName() + ".nickname") + "&r");
            }
        }

        if (data.getString("players." + event.getPlayer().getName() + ".prefix") != null) {
            prefix = Util.formatMessage(data.getString("players." + event.getPlayer().getName() + ".prefix") + "&r");
        }

        if (Util.hasPermission(event.getPlayer(), "kalesutilities.colorchat")) {
            message = Util.formatMessage(event.getMessage());
        }

        event.setFormat(Util.formatMessage(Main.Instance.config.getString("config.chatFormat")).replace("{prefix}", prefix).replace("{player}", player).replace("{message}", message));
    }
}