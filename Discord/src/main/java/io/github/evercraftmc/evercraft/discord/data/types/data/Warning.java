package io.github.evercraftmc.evercraft.discord.data.types.data;

import java.time.Instant;

public class Warning {
    private String user = "";
    private String mod = "";

    private String reason = "";

    private String timestamp;

    public Warning(String user, String mod, String reason, Instant timestamp) {
        this.user = user;
        this.mod = mod;

        this.reason = reason;

        this.timestamp = timestamp.toString();
    }

    public String getUser() {
        return this.user;
    }

    public String getMod() {
        return this.mod;
    }

    public String getReason() {
        return this.reason;
    }

    public Instant getTimestamp() {
        return Instant.parse(this.timestamp);
    }
}