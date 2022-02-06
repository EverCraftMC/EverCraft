package com.kale_ko.kalesutilities.spigot.listeners;

import com.kale_ko.kalesutilities.spigot.SpigotPlugin;
import com.kale_ko.kalesutilities.spigot.Util;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class WelcomeListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        SpigotPlugin.Instance.bot.sendMessage("[" + SpigotPlugin.Instance.config.getString("config.serverName") + "] " + Util.discordFormating(SpigotPlugin.Instance.config.getString("messages.joinMessage").replace("{player}", Util.getPlayerName(event.getPlayer()))));

        event.setJoinMessage(Util.formatMessage(SpigotPlugin.Instance.config.getString("messages.joinMessage").replace("{player}", Util.getPlayerName(event.getPlayer()))));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        SpigotPlugin.Instance.bot.sendMessage("[" + SpigotPlugin.Instance.config.getString("config.serverName") + "] " + Util.discordFormating(SpigotPlugin.Instance.config.getString("messages.joinMessage").replace("{player}", Util.getPlayerName(event.getPlayer()))));

        event.setQuitMessage(Util.formatMessage(SpigotPlugin.Instance.config.getString("messages.quitMessage").replace("{player}", Util.getPlayerName(event.getPlayer()))));
    }
}