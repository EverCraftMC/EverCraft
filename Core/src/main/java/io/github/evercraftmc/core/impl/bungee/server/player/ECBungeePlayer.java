package io.github.evercraftmc.core.impl.bungee.server.player;

import io.github.evercraftmc.core.ECPlayerData;
import io.github.evercraftmc.core.api.server.player.ECPlayer;
import io.github.evercraftmc.core.impl.bungee.server.util.ECBungeeComponentFormatter;
import io.github.evercraftmc.core.impl.util.ECTextFormatter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.UUID;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ECBungeePlayer implements ECPlayer {
    protected ProxiedPlayer handle;

    protected UUID uuid;
    protected String name;

    protected String displayName;

    public ECBungeePlayer(ECPlayerData.Player data) {
        this.uuid = data.uuid;
        this.name = data.name;

        this.displayName = ECTextFormatter.translateColors((data.prefix != null ? data.prefix + "&r " : "&r") + data.displayName + "&r");
    }

    public ECBungeePlayer(ECPlayerData.Player data, ProxiedPlayer handle) {
        this(data);

        this.handle = handle;
    }

    public ProxiedPlayer getHandle() {
        return this.handle;
    }

    @Override
    public UUID getUuid() {
        return this.uuid;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public String getOnlineDisplayName() {
        return this.handle.getDisplayName();
    }

    @Override
    public void setDisplayName(String displayName) {
        this.handle.setDisplayName(displayName);
    }

    @Override
    public InetAddress getAddress() {
        if (this.handle.getSocketAddress() instanceof InetSocketAddress address) {
            return address.getAddress();
        } else {
            return null;
        }
    }

    @Override
    public String getServer() {
        return this.handle.getServer() != null ? this.handle.getServer().getInfo().getName().toLowerCase() : null;
    }

    @Override
    public void sendMessage(String message) {
        this.handle.sendMessage(ECBungeeComponentFormatter.stringToComponent(message));
    }

    @Override
    public void kick(String message) {
        this.handle.disconnect(ECBungeeComponentFormatter.stringToComponent(message));
    }
}