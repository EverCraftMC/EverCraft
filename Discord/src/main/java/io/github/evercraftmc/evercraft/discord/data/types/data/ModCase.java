package io.github.evercraftmc.evercraft.discord.data.types.data;

public class ModCase {
    public String user = "";
    public String mod = "";

    public ModType type = ModType.OTHER;
    public String reason = "";

    public ModCase(String user, String mod, ModType type, String reason) {
        this.user = user;
        this.mod = mod;

        this.type = type;
        this.reason = reason;
    }
}