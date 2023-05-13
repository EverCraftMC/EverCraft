package io.github.evercraftmc.core.api.server.player;

import java.net.InetAddress;
import java.util.UUID;

public interface ECPlayer {
    public UUID getUuid();

    public String getName();

    public String getDisplayName();

    public void setDisplayName(String displayName);

    public InetAddress getAddress();

    public void sendMessage(String message);
}