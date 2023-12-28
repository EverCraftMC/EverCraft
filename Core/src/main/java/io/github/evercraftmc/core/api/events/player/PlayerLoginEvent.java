package io.github.evercraftmc.core.api.events.player;

import io.github.evercraftmc.core.api.events.ECCancelableReasonEvent;
import io.github.evercraftmc.core.api.server.player.ECPlayer;
import org.jetbrains.annotations.NotNull;

public class PlayerLoginEvent extends ECCancelableReasonEvent {
    protected @NotNull ECPlayer player;

    public PlayerLoginEvent(@NotNull ECPlayer player) {
        this.player = player;
    }

    public @NotNull ECPlayer getPlayer() {
        return this.player;
    }
}