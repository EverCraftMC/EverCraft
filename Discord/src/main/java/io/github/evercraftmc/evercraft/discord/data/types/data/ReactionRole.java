package io.github.evercraftmc.evercraft.discord.data.types.data;

public class ReactionRole {
    private String channel;
    private String message;

    private String emoji;
    private String role;

    public ReactionRole(String channel, String message, String emoji, String role) {
        this.channel = channel;
        this.message = message;

        this.emoji = emoji;
        this.role = role;
    }

    public String getChannel() {
        return this.channel;
    }

    public String getMessage() {
        return this.message;
    }

    public String getEmoji() {
        return this.emoji;
    }

    public String getRole() {
        return this.role;
    }
}