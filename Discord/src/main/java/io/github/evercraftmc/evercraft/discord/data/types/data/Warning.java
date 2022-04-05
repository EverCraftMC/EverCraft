package io.github.evercraftmc.evercraft.discord.data.types.data;

public class Warning {
    public String user = "";
    public String mod = "";

    public String reason = "";

    public Warning(String user, String mod, String reason) {
        this.user = user;
        this.mod = mod;

        this.reason = reason;
    }
}