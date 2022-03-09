package com.kale_ko.evercraft.shared.websocket;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.AuthorizationListener;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.kale_ko.evercraft.shared.util.ParamRunnable;

public class WebSocket {
    public SocketIOServer socket;

    public WebSocket(Integer port) {
        Configuration config = new Configuration();
        config.setPort(port);
        config.setOrigin("*");
        config.setAuthorizationListener(new AuthorizationListener() {
            @Override
            public boolean isAuthorized(HandshakeData data) {
                try {
                    URL url = new URL(data.getUrl());

                    return url.getPath().equalsIgnoreCase("/socket");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                return false;
            }
        });
        socket = new SocketIOServer(config);
        socket.start();
    }

    public void registerListener(String channel, ParamRunnable callback) {
        socket.addEventListener(channel, WebSocketMessage.class, new DataListener<WebSocketMessage>() {
            @Override
            public void onData(SocketIOClient client, WebSocketMessage data, AckRequest ackSender) throws Exception {
                callback.init(channel, client.getSessionId(), data);
                callback.run();
            }
        });
    }

    public void sendMessage(UUID clientId, String channel, WebSocketMessage message) {
        socket.getClient(clientId).sendEvent(channel, message);
    }

    public void sendMessage(String clientId, String channel, WebSocketMessage message) {
        sendMessage(UUID.fromString(clientId), channel, message);
    }

    public void close() {
        socket.stop();
    }
}