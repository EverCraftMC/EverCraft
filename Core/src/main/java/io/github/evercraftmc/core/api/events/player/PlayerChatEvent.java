package io.github.evercraftmc.core.api.events.player;

import io.github.evercraftmc.core.api.events.ECCancelableReasonEvent;
import io.github.evercraftmc.core.api.server.player.ECPlayer;

public class PlayerChatEvent extends ECCancelableReasonEvent {
    public enum MessageType {
        CHAT,
        DEATH,
        ADVANCEMENT
    }

    protected ECPlayer player;

    protected MessageType type;
    protected String message;

    public PlayerChatEvent(ECPlayer player, MessageType type, String message) {
        this.player = player;

        this.type = type;
        this.message = message;
    }

    public ECPlayer getPlayer() {
        return this.player;
    }

    public MessageType getType() {
        return this.type;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}