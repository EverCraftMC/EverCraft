package com.kale_ko.kalesutilities.listeners;

import com.kale_ko.kalesutilities.Main;
import com.kale_ko.kalesutilities.Util;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class MuteListener implements Listener {
    @EventHandler
    public void onChatMessage(AsyncPlayerChatEvent event) {
        if (Main.Instance.players.getBoolean(event.getPlayer().getName() + ".muted")) {
            event.setCancelled(true);

            Util.sendMessage(event.getPlayer(), Main.Instance.players.getString(event.getPlayer().getName() + ".mutedMessage"));

            Util.sendMessage(Main.Instance.getServer().getConsoleSender(), Main.Instance.config.getString("messages.mutedMessage").replace("{player}", Util.getPlayerName(event.getPlayer())).replace("{message}", event.getMessage()));
        }
    }
}