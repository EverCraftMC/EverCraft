package com.kale_ko.evercraft.spigot.listeners;

import com.kale_ko.evercraft.spigot.SpigotPlugin;
import com.kale_ko.evercraft.spigot.Util;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatFormatListener implements Listener {
    @EventHandler
    public void onChatMessage(AsyncPlayerChatEvent event) {
        String player = Util.getPlayerNickName(event.getPlayer());
        String prefix = Util.getPlayerPrefix(event.getPlayer());

        if (Util.hasPermission(event.getPlayer(), "evercraft.features.colorchat")) {
            event.setMessage(Util.formatMessage(event.getMessage()));
        }

        event.setFormat(Util.formatMessage(SpigotPlugin.Instance.config.getString("config.chatFormat")).replace("{prefix}", prefix).replace("{player}", player).replace("{message}", "%2$s"));
    }
}