package io.github.evercraftmc.core.api.events.player;

import io.github.evercraftmc.core.api.events.ECCancelableReasonEvent;
import io.github.evercraftmc.core.api.server.player.ECPlayer;

public class PlayerLoginEvent extends ECCancelableReasonEvent {
    protected ECPlayer player;

    public PlayerLoginEvent(ECPlayer player) {
        this.player = player;
    }

    public ECPlayer getPlayer() {
        return this.player;
    }
}