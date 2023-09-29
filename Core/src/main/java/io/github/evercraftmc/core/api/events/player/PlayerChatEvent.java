package io.github.evercraftmc.core.api.events.player;

import io.github.evercraftmc.core.api.events.ECCancelableReasonEvent;
import io.github.evercraftmc.core.api.server.player.ECPlayer;

public class PlayerChatEvent extends ECCancelableReasonEvent {
    protected ECPlayer player;

    protected String message;

    public PlayerChatEvent(ECPlayer player, String message) {
        this.player = player;

        this.message = message;
    }

    public ECPlayer getPlayer() {
        return this.player;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}