package com.kale_ko.kalesutilities.bungee.listeners;

import com.kale_ko.kalesutilities.bungee.BungeePlugin;
import com.kale_ko.kalesutilities.bungee.Util;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class WelcomeListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PostLoginEvent event) {
        BungeePlugin.Instance.bot.sendMessage("[" + BungeePlugin.Instance.config.getString("config.serverName") + "] " + Util.discordFormating(BungeePlugin.Instance.config.getString("messages.joinMessage").replace("{player}", Util.getPlayerName(event.getPlayer()))));

        Util.broadcastMessage(Util.formatMessage(BungeePlugin.Instance.config.getString("messages.joinMessage").replace("{player}", Util.getPlayerName(event.getPlayer()))));
    }

    @EventHandler
    public void onPlayerMove(ServerConnectEvent event) {
        if (event.getReason().equals(ServerConnectEvent.Reason.COMMAND) || event.getReason().equals(ServerConnectEvent.Reason.PLUGIN) || event.getReason().equals(ServerConnectEvent.Reason.PLUGIN_MESSAGE) || event.getReason().equals(ServerConnectEvent.Reason.SERVER_DOWN_REDIRECT) || event.getReason().equals(ServerConnectEvent.Reason.KICK_REDIRECT) || event.getReason().equals(ServerConnectEvent.Reason.LOBBY_FALLBACK)) {
            Util.broadcastMessage(Util.formatMessage(BungeePlugin.Instance.config.getString("messages.moveMessage").replace("{player}", Util.getPlayerName(event.getPlayer())).replace("{server}", event.getTarget().getName())));
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerDisconnectEvent event) {
        BungeePlugin.Instance.bot.sendMessage("[" + BungeePlugin.Instance.config.getString("config.serverName") + "] " + Util.discordFormating(BungeePlugin.Instance.config.getString("messages.quitMessage").replace("{player}", Util.getPlayerName(event.getPlayer()))));

        Util.broadcastMessage(Util.formatMessage(BungeePlugin.Instance.config.getString("messages.quitMessage").replace("{player}", Util.getPlayerName(event.getPlayer()))));
    }
}