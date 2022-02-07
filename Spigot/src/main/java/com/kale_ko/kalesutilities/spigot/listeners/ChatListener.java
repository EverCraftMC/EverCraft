package com.kale_ko.kalesutilities.spigot.listeners;

import com.kale_ko.kalesutilities.spigot.SpigotPlugin;
import com.kale_ko.kalesutilities.spigot.Util;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
    @EventHandler
    public void onChatMessage(AsyncPlayerChatEvent event) {
        if (!event.isCancelled()) {
            Util.messageBungee("globalChat", SpigotPlugin.Instance.config.getString("config.serverName"), "[" + SpigotPlugin.Instance.config.getString("config.serverName") + "] " + event.getFormat().replace("%2$s", event.getMessage()));

            SpigotPlugin.Instance.bot.sendMessage(Util.discordFormating("[" + SpigotPlugin.Instance.config.getString("config.serverName") + "] " + event.getFormat().replace("%2$s", event.getMessage())));
        }
    }
}