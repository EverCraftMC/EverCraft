package io.github.evercraftmc.global.listeners;

import io.github.evercraftmc.core.api.events.ECHandler;
import io.github.evercraftmc.core.api.events.ECHandlerOrder;
import io.github.evercraftmc.core.api.events.ECListener;
import io.github.evercraftmc.core.api.events.proxy.player.PlayerProxyPingEvent;
import io.github.evercraftmc.core.impl.bungee.server.ECBungeeServer;
import io.github.evercraftmc.core.impl.util.ECTextFormatter;
import io.github.evercraftmc.global.GlobalModule;

public class ServerPingListener implements ECListener {
    protected final GlobalModule parent;

    public ServerPingListener(GlobalModule parent) {
        this.parent = parent;
    }

    @ECHandler(order=ECHandlerOrder.BEFORE)
    public void onPlayerPing(PlayerProxyPingEvent event) {
        String server = ((ECBungeeServer) parent.getPlugin().getServer()).getDefaultServer();
        boolean isDefault = true;

        if (event.getServerAddress() != null) {
            String[] address = event.getServerAddress().getHostString().split("\\.");
            if (address.length > 2 && !address[0].equalsIgnoreCase(((ECBungeeServer) parent.getPlugin().getServer()).getFallbackServer()) && ((ECBungeeServer) parent.getPlugin().getServer()).getServer(address[0].toLowerCase())) {
                server = address[0].toLowerCase();
                isDefault = false;
            }
        }

        String[] motd = new String[2];
        motd[0] = ECTextFormatter.translateColors("&r&6--------[ &3&lEverCraft &r&7" + ((ECBungeeServer) parent.getPlugin().getServer()).getAllMinecraftVersions() + " &r&6]--------");
        if (isDefault) {
            motd[1] = ECTextFormatter.translateColors("&7Lots of fun minigames!");
        } else {
            motd[1] = ECTextFormatter.translateColors("&r" + server.substring(0, 1).toUpperCase() + server.substring(1).toLowerCase());
        }
        event.setMotd(String.join("\n", motd));
    }
}