package com.kale_ko.evercraft.bungee.websocket;

import java.util.List;
import java.util.UUID;
import com.kale_ko.evercraft.shared.websocket.WebSocketMessage;

public class PlayersWebSocketMessage extends WebSocketMessage {
    public Integer online;
    public Integer max;

    public List<PlayerSample> sample;

    public PlayersWebSocketMessage(String sender, String channel, Integer online, Integer max, List<PlayerSample> sample) {
        super(sender, channel);

        this.online = online;
        this.max = max;

        this.sample = sample;
    }

    public PlayersWebSocketMessage(UUID sender, String channel, Integer online, Integer max, List<PlayerSample> sample) {
        this(sender.toString(), channel, online, max, sample);
    }
}