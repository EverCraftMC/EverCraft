package io.github.evercraftmc.core.impl.spigot.server.player;

import java.net.InetAddress;
import java.util.UUID;
import org.bukkit.entity.Player;
import io.github.evercraftmc.core.ECData;
import io.github.evercraftmc.core.api.server.player.ECPlayer;
import io.github.evercraftmc.core.impl.spigot.server.util.ECSpigotComponentFormatter;

public class ECSpigotPlayer implements ECPlayer {
    protected Player handle;

    protected UUID uuid;
    protected String name;

    protected String displayName;

    public ECSpigotPlayer(ECData.Player data) {
        this.uuid = data.uuid;
        this.name = data.name;

        this.displayName = data.displayName;
    }

    public ECSpigotPlayer(ECData.Player data, Player handle) {
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

        if (this.handle != null) {
            this.handle.customName(ECSpigotComponentFormatter.stringToComponent(this.displayName));
            this.handle.displayName(ECSpigotComponentFormatter.stringToComponent(this.displayName));
            this.handle.playerListName(ECSpigotComponentFormatter.stringToComponent(this.displayName));
        }
    }

    @Override
    public InetAddress getAddress() {
        return this.handle.getAddress().getAddress();
    }

    @Override
    public void sendMessage(String message) {
        this.handle.sendMessage(ECSpigotComponentFormatter.stringToComponent(message));
    }
}