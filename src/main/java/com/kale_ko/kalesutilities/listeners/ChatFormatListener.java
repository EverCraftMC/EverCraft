package com.kale_ko.kalesutilities.listeners;

import com.kale_ko.kalesutilities.Util;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

@SuppressWarnings("deprecation")
public class ChatFormatListener implements Listener {
    @EventHandler
    public void onChatMessage(PlayerChatEvent event) {
        if (Util.hasPermission(event.getPlayer(), "kalesutilities.colorchat")) {
            event.setFormat(event.getPlayer().getDisplayName() + " > " + Util.formatMessage(event.getMessage()));
        } else {
            event.setFormat(event.getPlayer().getDisplayName() + " > " + event.getMessage());
        }
    }
}