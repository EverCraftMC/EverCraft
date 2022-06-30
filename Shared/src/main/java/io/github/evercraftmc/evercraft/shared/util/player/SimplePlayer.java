package io.github.evercraftmc.evercraft.shared.util.player;

import java.util.UUID;

public class SimplePlayer {
    private UUID uuid;
    private String name;

    private String prefix;
    private String nickname;

    public SimplePlayer(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;

        this.prefix = "";
        this.nickname = name;
    }

    public SimplePlayer(UUID uuid, String name, String prefix, String nickname) {
        this(uuid, name);

        this.prefix = prefix;
        this.nickname = nickname;
    }

    public UUID getUniqueId() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }

    public String getDisplayName() {
        return this.prefix + this.nickname;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public String getNickname() {
        return this.nickname;
    }
}