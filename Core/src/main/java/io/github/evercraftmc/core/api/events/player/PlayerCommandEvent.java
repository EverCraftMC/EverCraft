package io.github.evercraftmc.core.api.events.player;

import io.github.evercraftmc.core.api.events.ECCancelableReasonEvent;
import io.github.evercraftmc.core.api.server.player.ECPlayer;

public class PlayerCommandEvent extends ECCancelableReasonEvent {
    protected ECPlayer player;

    protected String command;

    public PlayerCommandEvent(ECPlayer player, String command) {
        this.player = player;

        this.command = command;
    }

    public ECPlayer getPlayer() {
        return this.player;
    }

    public String getCommand() {
        return this.command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}