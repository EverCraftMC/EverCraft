package io.github.evercraftmc.evercraft.discord.data.types.data;

public class Warning {
    private String user = "";
    private String mod = "";

    private String reason = "";

    public Warning(String user, String mod, String reason) {
        this.user = user;
        this.mod = mod;

        this.reason = reason;
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
}