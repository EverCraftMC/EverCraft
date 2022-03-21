package com.kale_ko.evercraft.bungee.listeners;

import java.util.Date;
import com.kale_ko.evercraft.bungee.BungeePlugin;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class SeenListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PostLoginEvent event) {
        BungeePlugin.Instance.players.set(event.getPlayer().getName() + ".lastseen", new Date().getTime());
    }

    @EventHandler
    public void onPlayerLeave(PlayerDisconnectEvent event) {
        BungeePlugin.Instance.players.set(event.getPlayer().getName() + ".lastseen", new Date().getTime());
    }
}