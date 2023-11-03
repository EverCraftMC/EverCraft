package io.github.evercraftmc.global.listeners;

import io.github.evercraftmc.core.api.events.ECHandler;
import io.github.evercraftmc.core.api.events.ECHandlerOrder;
import io.github.evercraftmc.core.api.events.ECListener;
import io.github.evercraftmc.core.api.events.proxy.player.PlayerProxyJoinEvent;
import io.github.evercraftmc.core.api.events.proxy.player.PlayerServerConnectedEvent;
import io.github.evercraftmc.core.impl.bungee.server.ECBungeeServer;
import io.github.evercraftmc.core.impl.util.ECTextFormatter;
import io.github.evercraftmc.global.GlobalModule;

public class ServerChoiceListener implements ECListener {
    protected final GlobalModule parent;

    public ServerChoiceListener(GlobalModule parent) {
        this.parent = parent;
    }

    @ECHandler(order=ECHandlerOrder.BEFORE)
    public void onPlayerJoin(PlayerProxyJoinEvent event) {
        String[] address = event.getPlayer().getServerAddress().getHostString().split("\\.");
        if (address.length > 2) {
            String server = GlobalModule.DEFAULT_SERVER;
            if (!address[0].equalsIgnoreCase(GlobalModule.FALLBACK_SERVER) && ((ECBungeeServer) parent.getPlugin().getServer()).getServer(address[0].toLowerCase())) {
                server = address[0].toLowerCase();
            }

            event.setTargetServer(server);
        }
    }

    @ECHandler(order=ECHandlerOrder.BEFORE)
    public void onPlayerConnect(PlayerServerConnectedEvent event) {
        event.setConnectMessage(ECTextFormatter.translateColors("&e" + event.getPlayer().getDisplayName() + " &r&emoved to " + event.getServer()));
    }
}