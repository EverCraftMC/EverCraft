package io.github.evercraftmc.core.impl.bungee.server.player;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.UUID;
import io.github.evercraftmc.core.ECData;
import io.github.evercraftmc.core.api.server.player.ECPlayer;
import io.github.evercraftmc.core.impl.bungee.server.util.ECBungeeComponentFormatter;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ECBungeePlayer implements ECPlayer {
    protected ProxiedPlayer handle;

    protected UUID uuid;
    protected String name;

    protected String displayName;

    public ECBungeePlayer(ECData.Player data) {
        this.uuid = data.uuid;
        this.name = data.name;

        this.displayName = data.displayName;
    }

    public ECBungeePlayer(ECData.Player data, ProxiedPlayer handle) {
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
    public void setDisplayName(String displayName) {
        this.displayName = displayName;

        if (this.handle != null) {
            this.handle.setDisplayName(this.displayName);
        }
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
    public void sendMessage(String message) {
        this.handle.sendMessage(ECBungeeComponentFormatter.stringToComponent(message));
    }
}