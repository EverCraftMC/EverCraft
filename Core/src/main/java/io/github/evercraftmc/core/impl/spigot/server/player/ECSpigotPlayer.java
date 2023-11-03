package io.github.evercraftmc.core.impl.spigot.server.player;

import io.github.evercraftmc.core.ECPlayerData;
import io.github.evercraftmc.core.api.server.player.ECPlayer;
import io.github.evercraftmc.core.impl.spigot.util.ECSpigotComponentFormatter;
import io.github.evercraftmc.core.impl.util.ECTextFormatter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.UUID;
import org.bukkit.entity.Player;

public class ECSpigotPlayer implements ECPlayer {
    protected Player handle;

    protected UUID uuid;
    protected String name;

    protected String displayName;

    public ECSpigotPlayer(ECPlayerData.Player data) {
        this.uuid = data.uuid;
        this.name = data.name;

        this.displayName = ECTextFormatter.translateColors((data.prefix != null ? data.prefix + "&r " : "&r") + data.displayName + "&r");
    }

    public ECSpigotPlayer(ECPlayerData.Player data, Player handle) {
        this(data);

        this.handle = handle;
    }

    public Player getHandle() {
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
    }

    @Override
    public String getOnlineDisplayName() {
        return ECSpigotComponentFormatter.componentToString(this.handle.displayName());
    }

    @Override
    public void setOnlineDisplayName(String displayName) {
        this.setDisplayName(displayName);

        this.handle.customName(ECSpigotComponentFormatter.stringToComponent(displayName));
        this.handle.displayName(ECSpigotComponentFormatter.stringToComponent(displayName));
        this.handle.playerListName(ECSpigotComponentFormatter.stringToComponent(displayName));
    }

    @Override
    public InetAddress getAddress() {
        InetSocketAddress socketAddress = this.handle.getAddress();
        if (socketAddress != null) {
            return socketAddress.getAddress();
        } else {
            return null;
        }
    }

    @Override
    public InetSocketAddress getServerAddress() {
        return this.handle.getVirtualHost();
    }

    @Override
    public String getServer() {
        throw new UnsupportedOperationException("Server is backend");
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.handle.hasPermission(permission);
    }

    @Override
    public void sendMessage(String message) {
        this.handle.sendMessage(ECSpigotComponentFormatter.stringToComponent(message));
    }

    @Override
    public void kick(String message) {
        this.handle.kick(ECSpigotComponentFormatter.stringToComponent(message));
    }
}