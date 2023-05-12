package io.github.evercraftmc.core.api.server.player;

import java.util.UUID;

public interface ECPlayer {
    public UUID getUuid();

    public String getName();

    public String getDisplayName();

    public void setDisplayName(String displayName);

    public void sendMessage(String message);
}