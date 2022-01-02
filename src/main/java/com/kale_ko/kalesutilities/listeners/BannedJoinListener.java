package com.kale_ko.kalesutilities.listeners;

import com.kale_ko.kalesutilities.Main;
import com.kale_ko.kalesutilities.Util;
import com.kale_ko.kalesutilities.Config;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public class BannedJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerLoginEvent event) {
        YamlConfiguration data = Config.load("players.yml").getConfig();

        if (data.getBoolean("players." + event.getPlayer().getName() + ".banned")) {
            event.setKickMessage(data.getString("players." + event.getPlayer().getName() + ".banMessage"));
            event.disallow(Result.KICK_BANNED, data.getString("players." + event.getPlayer().getName() + ".banMessage"));

            for (Player player : Main.Instance.getServer().getOnlinePlayers()) {
                if (player != event.getPlayer() && Util.hasPermission(player, "kalesutilities.ban")) {
                    Util.sendMessage(player, Main.Instance.config.getConfig().getString("messages.bannedJoin").replace("{player}", Util.getPlayerName(event.getPlayer())));
                }
            }
        }
    }
}