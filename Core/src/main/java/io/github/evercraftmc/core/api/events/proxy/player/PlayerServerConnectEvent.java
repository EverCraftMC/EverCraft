package io.github.evercraftmc.core.api.events.proxy.player;

import io.github.evercraftmc.core.api.events.ECCancelableReasonEvent;
import io.github.evercraftmc.core.api.server.player.ECPlayer;

public class PlayerServerConnectEvent extends ECCancelableReasonEvent {
    protected ECPlayer player;

    protected String targetServer;

    public PlayerServerConnectEvent(ECPlayer player, String targetServer) {
        this.player = player;

        this.targetServer = targetServer;
    }

    public ECPlayer getPlayer() {
        return this.player;
    }

    public String getTargetServer() {
        return this.targetServer;
    }

    public void setTargetServer(String targetServer) {
        this.targetServer = targetServer;
    }
}