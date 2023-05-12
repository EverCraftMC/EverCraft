/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package io.github.evercraftmc.core.impl.spigot.server.player;

import io.github.evercraftmc.core.ECData;
import io.github.evercraftmc.core.api.server.player.ECPlayer;
import java.util.UUID;
import org.bukkit.entity.Player;

public class ECSpigotPlayer
implements ECPlayer {
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
            this.handle.setCustomName(this.displayName);
            this.handle.setDisplayName(this.displayName);
            this.handle.setPlayerListName(this.displayName);
        }
    }

    @Override
    public void sendMessage(String message) {
        this.handle.sendMessage(message);
    }
}

