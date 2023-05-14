package io.github.evercraftmc.core.api.server.player;

import java.net.InetAddress;
import java.util.UUID;
import io.github.evercraftmc.core.impl.util.ECTextFormatter;

public interface ECConsole extends ECPlayer {
    @Override
    public default UUID getUuid() {
        return UUID.fromString("00000000-0000-0000-0000-000000000000");
    }

    @Override
    public default String getName() {
        return "Console";
    }

    @Override
    public default String getDisplayName() {
        return ECTextFormatter.translateColors("&l&4Console");
    }

    @Override
    public default void setDisplayName(String displayName) {
        throw new UnsupportedOperationException("Player is console");
    }

    @Override
    public default InetAddress getAddress() {
        return null;
    }
}