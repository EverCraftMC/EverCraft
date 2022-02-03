package com.kale_ko.kalesutilities.bungee.listeners;

import com.kale_ko.kalesutilities.bungee.BungeePlugin;
import com.kale_ko.kalesutilities.bungee.Util;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(ServerConnectEvent event) {
        if (event.getReason().equals(ServerConnectEvent.Reason.JOIN_PROXY)) {
            event.setTarget(BungeePlugin.Instance.getProxy().getServerInfo(BungeePlugin.Instance.config.getString("config.mainServer")));
        }
    }

    @EventHandler
    public void onPlayerJoin(PostLoginEvent event) {
        Util.updatePlayerName(event.getPlayer());
    }
}