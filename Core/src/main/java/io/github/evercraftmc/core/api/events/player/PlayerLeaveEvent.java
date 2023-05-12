package io.github.evercraftmc.core.api.events.player;

import io.github.evercraftmc.core.api.server.player.ECPlayer;

public class PlayerLeaveEvent extends PlayerEvent {
    public PlayerLeaveEvent(ECPlayer player) {
        super(player);
    }
}