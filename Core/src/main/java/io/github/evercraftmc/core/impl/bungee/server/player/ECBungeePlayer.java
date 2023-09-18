package io.github.evercraftmc.core.impl.bungee.server.player;

import io.github.evercraftmc.core.ECPlayerData;
import io.github.evercraftmc.core.api.server.player.ECPlayer;
import io.github.evercraftmc.core.impl.bungee.server.util.ECBungeeComponentFormatter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.UUID;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ECBungeePlayer implements ECPlayer {
    protected ProxiedPlayer handle;

    protected UUID uuid;
    protected String name;

    public ECBungeePlayer(ECPlayerData.Player data) {
        this.uuid = data.uuid;
        this.name = data.name;
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
    public void sendMessage(String message) {
        this.handle.sendMessage(ECBungeeComponentFormatter.stringToComponent(message));
    }
}