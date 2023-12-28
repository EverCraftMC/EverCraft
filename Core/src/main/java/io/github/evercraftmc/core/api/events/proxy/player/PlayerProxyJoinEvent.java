package io.github.evercraftmc.core.api.events.proxy.player;

import io.github.evercraftmc.core.api.events.player.PlayerJoinEvent;
import io.github.evercraftmc.core.api.server.player.ECPlayer;
import org.jetbrains.annotations.NotNull;

public class PlayerProxyJoinEvent extends PlayerJoinEvent {
    protected @NotNull String targetServer;

    public PlayerProxyJoinEvent(@NotNull ECPlayer player, @NotNull String joinMessage, @NotNull String targetServer) {
        super(player, joinMessage);

        this.targetServer = targetServer;
    }

    public @NotNull String getTargetServer() {
        return this.targetServer;
    }

    public void setTargetServer(@NotNull String targetServer) {
        this.targetServer = targetServer;
    }
}