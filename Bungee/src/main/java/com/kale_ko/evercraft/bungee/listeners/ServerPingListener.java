package com.kale_ko.evercraft.bungee.listeners;

import java.util.ArrayList;
import java.util.List;
import com.kale_ko.evercraft.bungee.BungeePlugin;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.ServerPing.PlayerInfo;
import net.md_5.bungee.api.ServerPing.Players;
import net.md_5.bungee.api.ServerPing.Protocol;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerPingListener implements Listener {
    @EventHandler
    public void onServerPing(ProxyPingEvent event) {
        Protocol protocol = event.getResponse().getVersion();
        protocol.setName(BungeePlugin.Instance.getProxy().getName() + " 1.8.x - " + BungeePlugin.Instance.getProxy().getVersion() + ".x");
        if (!event.getConnection().isLegacy() && event.getConnection().getVersion() > 47 && event.getConnection().getVersion() < 758) {
            protocol.setProtocol(event.getConnection().getVersion());
        } else {
            protocol.setProtocol(758);
        }

        List<PlayerInfo> playerlist = new ArrayList<PlayerInfo>();

        for (ProxiedPlayer player : BungeePlugin.Instance.getProxy().getPlayers()) {
            playerlist.add(new PlayerInfo(player.getName(), player.getUniqueId()));
        }

        Players players = event.getResponse().getPlayers();
        players.setSample(playerlist.toArray(new PlayerInfo[] {}));

        event.setResponse(new ServerPing(protocol, players, event.getResponse().getDescriptionComponent(), event.getResponse().getFaviconObject()));
    }
}