package io.github.evercraftmc.evercraft.discord.data.types.data;

public class ModCase {
    private String user = "";
    private String mod = "";

    private ModType type = ModType.OTHER;
    private String reason = "";

    public ModCase(String user, String mod, ModType type, String reason) {
        this.user = user;
        this.mod = mod;

        this.type = type;
        this.reason = reason;
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
}