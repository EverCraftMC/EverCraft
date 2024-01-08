package io.github.evercraftmc.global.events.player;

import io.github.evercraftmc.core.api.events.ECEvent;
import io.github.evercraftmc.core.api.server.player.ECPlayer;
import org.jetbrains.annotations.NotNull;

public class PlayerDisplayNameChangeEvent extends ECEvent {
    protected final @NotNull ECPlayer player;

    public PlayerDisplayNameChangeEvent(@NotNull ECPlayer player) {
        this.player = player;
    }

    public @NotNull ECPlayer getPlayer() {
        return this.player;
    }
}