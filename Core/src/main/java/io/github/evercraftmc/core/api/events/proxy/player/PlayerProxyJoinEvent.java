package io.github.evercraftmc.core.api.events.proxy.player;

import io.github.evercraftmc.core.api.events.player.PlayerJoinEvent;
import io.github.evercraftmc.core.api.server.player.ECPlayer;

public class PlayerProxyJoinEvent extends PlayerJoinEvent {
    protected String targetServer;

    public PlayerProxyJoinEvent(ECPlayer player, String joinMessage, String targetServer) {
        super(player, joinMessage);

        this.targetServer = targetServer;
    }

    public String getTargetServer() {
        return this.targetServer;
    }

    public void setTargetServer(String targetServer) {
        this.targetServer = targetServer;
    }
}