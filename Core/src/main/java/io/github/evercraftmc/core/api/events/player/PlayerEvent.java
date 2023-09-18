package io.github.evercraftmc.core.api.events.player;

import io.github.evercraftmc.core.api.events.ECEvent;
import io.github.evercraftmc.core.api.server.player.ECPlayer;

public abstract class PlayerEvent extends ECEvent {
    protected ECPlayer player;

    protected PlayerEvent(ECPlayer player) {
        this.player = player;
    }

    public ECPlayer getPlayer() {
        return this.player;
    }
}