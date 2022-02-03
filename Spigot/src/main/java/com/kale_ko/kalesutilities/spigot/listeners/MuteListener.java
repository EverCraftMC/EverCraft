package com.kale_ko.kalesutilities.spigot.listeners;

import com.kale_ko.kalesutilities.spigot.SpigotPlugin;
import com.kale_ko.kalesutilities.spigot.Util;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class MuteListener implements Listener {
    @EventHandler
    public void onChatMessage(AsyncPlayerChatEvent event) {
        if (SpigotPlugin.Instance.players.getBoolean(event.getPlayer().getName() + ".muted")) {
            event.setCancelled(true);

            Util.sendMessage(event.getPlayer(), SpigotPlugin.Instance.players.getString(event.getPlayer().getName() + ".mutedMessage"));

            Util.sendMessage(SpigotPlugin.Instance.getServer().getConsoleSender(), SpigotPlugin.Instance.config.getString("messages.mutedMessage").replace("{player}", Util.getPlayerName(event.getPlayer())).replace("{message}", event.getMessage()));
        }
    }
}