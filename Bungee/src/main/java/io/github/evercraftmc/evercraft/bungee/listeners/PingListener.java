package io.github.evercraftmc.evercraft.bungee.listeners;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import io.github.evercraftmc.evercraft.bungee.BungeeMain;
import io.github.evercraftmc.evercraft.bungee.util.formatting.ComponentFormatter;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.ServerPing.PlayerInfo;
import net.md_5.bungee.api.ServerPing.Players;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.event.EventHandler;

public class PingListener extends BungeeListener {
    @EventHandler
    public void onPing(ProxyPingEvent event) {
        ServerPing ping = event.getResponse();

        try {
            if (!BungeeMain.getInstance().getPluginData().get().maintenance) {
                if (event.getConnection().getVirtualHost() != null && BungeeMain.getInstance().serverMotds.containsKey(event.getConnection().getVirtualHost().getHostName().split("\\.")[0].toLowerCase())) {
                    ping.setDescriptionComponent(ComponentFormatter.flatenComponent(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().serverMotds.get(event.getConnection().getVirtualHost().getHostName().split("\\.")[0].toLowerCase())))));
                } else {
                    ping.setDescriptionComponent(ComponentFormatter.flatenComponent(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().serverMotd))));
                }

                List<PlayerInfo> sample = new ArrayList<PlayerInfo>();
                for (ProxiedPlayer player : BungeeMain.getInstance().getProxy().getPlayers()) {
                    sample.add(new PlayerInfo(player.getDisplayName(), player.getUniqueId()));
                }
                Players players = new Players(BungeeMain.getInstance().serverMaxPlayers, BungeeMain.getInstance().getProxy().getOnlineCount(), sample.toArray(new PlayerInfo[] {}));
                ping.setPlayers(players);
            } else {
                ping.setDescriptionComponent(ComponentFormatter.flatenComponent(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().serverMotd.split("\n")[0] + "\n" + TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().get().moderation.maintenance.motd)))));

                Players players = new Players(BungeeMain.getInstance().serverMaxPlayers, 0, new PlayerInfo[] {});
                ping.setPlayers(players);
            }
        } catch (NullPointerException | ConcurrentModificationException e) {
            return;
        }

        event.setResponse(ping);
    }
}