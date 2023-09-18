package io.github.evercraftmc.core.impl.spigot.server.player;

import io.github.evercraftmc.core.ECPlayerData;
import io.github.evercraftmc.core.api.server.player.ECPlayer;
import io.github.evercraftmc.core.impl.spigot.server.util.ECSpigotComponentFormatter;
import java.net.InetAddress;
import java.util.UUID;
import org.bukkit.entity.Player;

public class ECSpigotPlayer implements ECPlayer {
    protected Player handle;

    protected UUID uuid;
    protected String name;

    public ECSpigotPlayer(ECPlayerData.Player data) {
        this.uuid = data.uuid;
        this.name = data.name;
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
        return ECSpigotComponentFormatter.componentToString(this.handle.displayName());
    }

    @Override
    public void setDisplayName(String displayName) {
        this.handle.customName(ECSpigotComponentFormatter.stringToComponent(displayName));
        this.handle.displayName(ECSpigotComponentFormatter.stringToComponent(displayName));
        this.handle.playerListName(ECSpigotComponentFormatter.stringToComponent(displayName));
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