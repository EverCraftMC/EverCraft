package io.github.evercraftmc.core.api.events.player;

import io.github.evercraftmc.core.api.events.ECCancelableReasonEvent;
import io.github.evercraftmc.core.api.server.player.ECPlayer;

public class PlayerJoinEvent extends ECCancelableReasonEvent {
    protected ECPlayer player;

    protected String joinMessage;

    public PlayerJoinEvent(ECPlayer player, String joinMessage) {
        this.player = player;

        this.joinMessage = joinMessage;
    }

    public ECPlayer getPlayer() {
        return this.player;
    }

    public String getJoinMessage() {
        return this.joinMessage;
    }

    public void setJoinMessage(String joinMessage) {
        this.joinMessage = joinMessage;
    }
}