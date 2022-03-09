package com.kale_ko.evercraft.shared.websocket;

import java.util.UUID;

public class TextWebSocketMessage extends WebSocketMessage {
    public String message;

    public TextWebSocketMessage(String sender, String channel, String message) {
        super(sender, channel);

        this.message = message;
    }

    public TextWebSocketMessage(UUID sender, String channel, String message) {
        this(sender.toString(), channel, message);
    }
}