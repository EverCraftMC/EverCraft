package io.github.evercraftmc.core.api.events.player;

import io.github.evercraftmc.core.api.events.ECCancelableReasonEvent;
import io.github.evercraftmc.core.api.server.player.ECPlayer;
import org.jetbrains.annotations.NotNull;

public class PlayerCommandEvent extends ECCancelableReasonEvent {
    protected final @NotNull ECPlayer player;

    protected @NotNull String command;

    public PlayerCommandEvent(@NotNull ECPlayer player, @NotNull String command) {
        this.player = player;

        this.command = command;
    }

    public @NotNull ECPlayer getPlayer() {
        return this.player;
    }

    public @NotNull String getCommand() {
        return this.command;
    }

    public void setCommand(@NotNull String command) {
        this.command = command;
    }
}