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

        this.nickname = name;
        this.prefix = "";
    }

    public SimplePlayer(UUID uuid, String name, String nickname, String prefix) {
        this(uuid, name);

        this.nickname = nickname;
        this.prefix = prefix;
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