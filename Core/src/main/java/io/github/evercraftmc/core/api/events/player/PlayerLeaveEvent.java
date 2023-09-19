package io.github.evercraftmc.core.api.events.player;

import io.github.evercraftmc.core.api.events.ECEvent;
import io.github.evercraftmc.core.api.server.player.ECPlayer;

public class PlayerLeaveEvent extends ECEvent {
    protected ECPlayer player;

    protected String leaveMessage;

    public PlayerLeaveEvent(ECPlayer player, String leaveMessage) {
        this.player = player;

        this.leaveMessage = leaveMessage;
    }

    public ECPlayer getPlayer() {
        return this.player;
    }

    public String getLeaveMessage() {
        return this.leaveMessage;
    }

    public void setLeaveMessage(String leaveMessage) {
        this.leaveMessage = leaveMessage;
    }
}