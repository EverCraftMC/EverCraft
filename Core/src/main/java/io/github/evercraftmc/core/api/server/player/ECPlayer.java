package io.github.evercraftmc.core.api.server.player;

import java.net.InetAddress;
import java.util.UUID;

public interface ECPlayer {
    UUID getUuid();

    String getName();

    String getDisplayName();

    void setDisplayName(String displayName);

    InetAddress getAddress();

    String getServer();

    void sendMessage(String message);

    void kick(String message);
}