package com.kale_ko.evercraft.bungee.websocket;

import java.util.UUID;
import com.kale_ko.evercraft.shared.websocket.WebSocketMessage;

public class VersionsWebSocketMessage extends WebSocketMessage {
    public String name;
    public String protocol;

    public VersionsWebSocketMessage(String sender, String channel, String name, String protocol) {
        super(sender, channel);

        this.name = name;
        this.protocol = protocol;
    }

    public VersionsWebSocketMessage(UUID sender, String channel, String name, String protocol) {
        this(sender.toString(), channel, name, protocol);
    }
}