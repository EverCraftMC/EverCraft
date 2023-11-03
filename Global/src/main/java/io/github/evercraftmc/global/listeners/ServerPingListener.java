package io.github.evercraftmc.global.listeners;

import io.github.evercraftmc.core.api.events.ECHandler;
import io.github.evercraftmc.core.api.events.ECHandlerOrder;
import io.github.evercraftmc.core.api.events.ECListener;
import io.github.evercraftmc.core.api.events.proxy.player.PlayerProxyPingEvent;
import io.github.evercraftmc.core.impl.bungee.server.ECBungeeServer;
import io.github.evercraftmc.global.GlobalModule;

public class ServerPingListener implements ECListener {
    protected final GlobalModule parent;

    public ServerPingListener(GlobalModule parent) {
        this.parent = parent;
    }

    @ECHandler(order=ECHandlerOrder.BEFORE)
    public void onPlayerPing(PlayerProxyPingEvent event) {
        String server = ((ECBungeeServer) parent.getPlugin().getServer()).getDefaultServer();

        String[] address = event.getServerAddress().getHostString().split("\\.");
        if (address.length > 2 && !address[0].equalsIgnoreCase(((ECBungeeServer) parent.getPlugin().getServer()).getFallbackServer()) && ((ECBungeeServer) parent.getPlugin().getServer()).getServer(address[0].toLowerCase())) {
            server = address[0].toLowerCase();
        }

        String[] motd = new String[] { ((ECBungeeServer) parent.getPlugin().getServer()).getDefaultMotd().split("\n")[0], "" };
        motd[1] = server;
        event.setMotd(String.join("\n", motd));
    }
}