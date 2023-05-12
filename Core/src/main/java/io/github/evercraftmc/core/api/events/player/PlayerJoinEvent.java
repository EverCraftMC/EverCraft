package io.github.evercraftmc.core.api.events.player;

import io.github.evercraftmc.core.api.server.player.ECPlayer;

public class PlayerJoinEvent extends PlayerEvent {
    public PlayerJoinEvent(ECPlayer player) {
        super(player);
    }
}