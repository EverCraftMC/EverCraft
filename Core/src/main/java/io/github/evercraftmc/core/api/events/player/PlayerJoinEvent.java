package io.github.evercraftmc.core.api.events.player;

import io.github.evercraftmc.core.api.events.ECCancelableReasonEvent;
import io.github.evercraftmc.core.api.server.player.ECPlayer;
import org.jetbrains.annotations.NotNull;

public class PlayerJoinEvent extends ECCancelableReasonEvent {
    protected @NotNull ECPlayer player;

    protected @NotNull String joinMessage;

    public PlayerJoinEvent(@NotNull ECPlayer player, @NotNull String joinMessage) {
        this.player = player;

        this.joinMessage = joinMessage;
    }

    public @NotNull ECPlayer getPlayer() {
        return this.player;
    }

    public @NotNull String getJoinMessage() {
        return this.joinMessage;
    }

    public void setJoinMessage(@NotNull String joinMessage) {
        this.joinMessage = joinMessage;
    }
}