package com.kale_ko.kalesutilities.listeners;

import com.kale_ko.kalesutilities.Main;
import com.kale_ko.kalesutilities.Util;
import java.io.File;
import java.nio.file.Paths;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

@SuppressWarnings("deprecation")
public class MuteListener implements Listener {
    @EventHandler
    public void onChatMessage(PlayerChatEvent event) {
        File dataFolder = Main.Instance.getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }

        File dataFile = Paths.get(dataFolder.getAbsolutePath(), "players.yml").toFile();

        YamlConfiguration data = YamlConfiguration.loadConfiguration(dataFile);

        if (data.getBoolean("players." + event.getPlayer().getName() + ".muted")) {
            event.setCancelled(true);

            Util.sendMessage(event.getPlayer(), data.getString("players." + event.getPlayer().getName() + ".mutedMessage"));

            for (Player player : Main.Instance.getServer().getOnlinePlayers()) {
                if (player != event.getPlayer() && Util.hasPermission(player, "kalesutilities.mute")) {
                    Util.sendMessage(player, Main.Instance.config.getString("messages.mutedMessage").replace("{player}", Util.getPlayerName(event.getPlayer())).replace("{message}", event.getMessage()));
                }
            }
        }
    }
}