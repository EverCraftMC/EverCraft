package io.github.evercraftmc.evercraft.shared.util.player;

import java.util.UUID;

public class SimplePlayer {
    private UUID uuid;
    private String name;

    private String displayName;

    public SimplePlayer(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.displayName = name;
    }

    public SimplePlayer(UUID uuid, String name, String displayName) {
        this(uuid, name);
        this.displayName = displayName;
    }

    public UUID getUniqueId() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }

    public String getDisplayName() {
        return this.displayName;
    }
}