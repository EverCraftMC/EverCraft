package io.github.evercraftmc.core.api.events.proxy.player;

import io.github.evercraftmc.core.api.events.ECCancelableReasonEvent;
import io.github.evercraftmc.core.api.server.player.ECPlayer;
import org.jetbrains.annotations.NotNull;

public class PlayerServerConnectEvent extends ECCancelableReasonEvent {
    protected final @NotNull ECPlayer player;

    protected @NotNull String targetServer;

    public PlayerServerConnectEvent(@NotNull ECPlayer player, @NotNull String targetServer) {
        this.player = player;

        this.targetServer = targetServer;
    }

    public @NotNull ECPlayer getPlayer() {
        return this.player;
    }

    public @NotNull String getTargetServer() {
        return this.targetServer;
    }

    public void setTargetServer(@NotNull String targetServer) {
        this.targetServer = targetServer;
    }
}