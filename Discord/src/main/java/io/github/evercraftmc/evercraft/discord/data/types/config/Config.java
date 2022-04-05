package io.github.evercraftmc.evercraft.discord.data.types.config;

import io.github.evercraftmc.evercraft.discord.data.DataParseable;
import net.dv8tion.jda.api.entities.Activity.ActivityType;

public class Config extends DataParseable {
    private String token = "";

    private String prefix = "!";

    private ActivityType statusType = ActivityType.WATCHING;
    private String status = "for bad boys";

    private ConfigColor color = new ConfigColor();

    private String guildId = "";
    private String welcomeChannel = "";
    private String logChannel = "";
    private String botCommandsChannel = "";

    private String[] bannedWords = new String[] {};

    public String getToken() {
        return this.token;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public ActivityType getStatusType() {
        return this.statusType;
    }

    public String getStatus() {
        return this.status;
    }

    public ConfigColor getColor() {
        return this.color;
    }

    public String getGuildId() {
        return this.guildId;
    }

    public String getWelcomeChannel() {
        return this.welcomeChannel;
    }

    public String getLogChannel() {
        return this.logChannel;
    }

    public String getBotCommandsChannel() {
        return this.botCommandsChannel;
    }

    public String[] getBannedWords() {
        return this.bannedWords;
    }
}