package com.kale_ko.evercraft.bungee.listeners;

import com.kale_ko.evercraft.bungee.BungeePlugin;
import com.kale_ko.evercraft.bungee.Util;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class BannedJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PostLoginEvent event) {
        if (BungeePlugin.Instance.players.getBoolean(event.getPlayer().getName() + ".banned") != null && BungeePlugin.Instance.players.getBoolean(event.getPlayer().getName() + ".banned")) {
            event.getPlayer().disconnect(Util.stringToBungeeComponent(BungeePlugin.Instance.players.getString(event.getPlayer().getName() + ".banMessage")));

            Util.sendMessage(BungeePlugin.Instance.getProxy().getConsole(), BungeePlugin.Instance.config.getString("messages.bannedJoin").replace("{player}", Util.getPlayerName(event.getPlayer())));
        }
    }
}