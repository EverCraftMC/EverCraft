package io.github.evercraftmc.core.api.events.player;

import io.github.evercraftmc.core.api.events.ECCancelableReasonEvent;
import io.github.evercraftmc.core.api.server.player.ECPlayer;
import java.util.List;

public class PlayerChatEvent extends ECCancelableReasonEvent {
    protected int type;

    protected ECPlayer player;

    protected String message;
    protected List<ECPlayer> recipients;

    public PlayerChatEvent(ECPlayer player, String message, int type, List<ECPlayer> recipients) {
        this.player = player;

        this.message = message;

        this.type = type;
        this.recipients = recipients;
    }

    public int getType() {
        return this.type;
    }

    public ECPlayer getPlayer() {
        return this.player;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ECPlayer> getRecipients() {
        return this.recipients;
    }

    public void setRecipients(List<ECPlayer> recipients) {
        this.recipients = recipients;
    }

    public static class MessageType {
        public static final int CHAT = 1;

        public static final int DEATH = 2;
        public static final int ADVANCEMENT = 3;

        private MessageType() {
        }
    }
}