package io.github.evercraftmc.core.api.events.proxy.player;

import io.github.evercraftmc.core.api.events.ECEvent;
import io.github.evercraftmc.core.api.server.player.ECPlayer;

public class PlayerServerConnectedEvent extends ECEvent {
    protected ECPlayer player;

    protected String server;

    protected String connectMessage;

    public PlayerServerConnectedEvent(ECPlayer player, String server, String connectMessage) {
        this.player = player;

        this.server = server;

        this.connectMessage = connectMessage;
    }

    public ECPlayer getPlayer() {
        return this.player;
    }

    public String getServer() {
        return this.server;
    }

    public String getConnectMessage() {
        return this.connectMessage;
    }

    public void setConnectMessage(String connectMessage) {
        this.connectMessage = connectMessage;
    }
}