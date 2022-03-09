package com.kale_ko.evercraft.shared.websocket;

import java.util.UUID;

public class WebSocketMessage {
    public String sender;

    public String channel;

    public WebSocketMessage(String sender, String channel) {
        this.sender = sender;
        this.channel = channel;
    }

    public WebSocketMessage(UUID sender, String channel) {
        this(sender.toString(), channel);
    }
}