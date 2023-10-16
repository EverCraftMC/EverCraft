package io.github.evercraftmc.core.api.server.player;

import java.net.InetAddress;
import java.util.UUID;

public interface ECPlayer {
    UUID getUuid();

    String getName();

    String getDisplayName();

    String getOnlineDisplayName();

    void setDisplayName(String displayName);

    InetAddress getAddress();

    String getServer();

    boolean hasPermission(String permission);

    void sendMessage(String message);

    void kick(String message);
}