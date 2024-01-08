package io.github.evercraftmc.core.api.events.player;

import io.github.evercraftmc.core.api.events.ECEvent;
import io.github.evercraftmc.core.api.server.player.ECPlayer;
import org.jetbrains.annotations.NotNull;

public class PlayerLeaveEvent extends ECEvent {
    protected final @NotNull ECPlayer player;

    protected @NotNull String leaveMessage;

    public PlayerLeaveEvent(@NotNull ECPlayer player, @NotNull String leaveMessage) {
        this.player = player;

        this.leaveMessage = leaveMessage;
    }

    public @NotNull ECPlayer getPlayer() {
        return this.player;
    }

    public @NotNull String getLeaveMessage() {
        return this.leaveMessage;
    }

    public void setLeaveMessage(@NotNull String leaveMessage) {
        this.leaveMessage = leaveMessage;
    }
}