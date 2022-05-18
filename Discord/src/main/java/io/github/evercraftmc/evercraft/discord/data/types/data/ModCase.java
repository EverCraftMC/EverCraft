package io.github.evercraftmc.evercraft.discord.data.types.data;

import java.time.Instant;

public class ModCase {
    private String user = "";
    private String mod = "";

    private ModType type = ModType.OTHER;
    private String reason = "";

    private String timestamp;

    public ModCase(String user, String mod, ModType type, String reason, Instant timestamp) {
        this.user = user;
        this.mod = mod;

        this.type = type;
        this.reason = reason;

        this.timestamp = timestamp.toString();
    }

    public String getUser() {
        return this.user;
    }

    public String getMod() {
        return this.mod;
    }

    public ModType getType() {
        return this.type;
    }

    public String getReason() {
        return this.reason;
    }

    public Instant getTimestamp() {
        return Instant.parse(this.timestamp);
    }
}