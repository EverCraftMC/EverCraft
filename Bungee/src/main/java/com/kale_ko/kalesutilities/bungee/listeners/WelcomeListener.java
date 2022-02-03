package com.kale_ko.kalesutilities.bungee.listeners;

import com.kale_ko.kalesutilities.bungee.BungeePlugin;
import com.kale_ko.kalesutilities.bungee.Util;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class WelcomeListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PostLoginEvent event) {
        Util.broadcastMessage(Util.formatMessage(BungeePlugin.Instance.config.getString("messages.joinMessage").replace("{player}", Util.getPlayerName(event.getPlayer()))));
    }

    @EventHandler
    public void onPlayerLeave(PlayerDisconnectEvent event) {
        Util.broadcastMessage(Util.formatMessage(BungeePlugin.Instance.config.getString("messages.quitMessage").replace("{player}", Util.getPlayerName(event.getPlayer()))));
    }
}