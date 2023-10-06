package io.github.evercraftmc.core.api.server.player;

import io.github.evercraftmc.core.impl.util.ECTextFormatter;
import java.net.InetAddress;
import java.util.UUID;

public interface ECConsole extends ECPlayer {
    @Override
    default UUID getUuid() {
        return UUID.fromString("00000000-0000-0000-0000-000000000000");
    }

    @Override
    default String getName() {
        return "Console";
    }

    @Override
    default String getDisplayName() {
        return ECTextFormatter.translateColors("&l&4Console");
    }

    @Override
    default void setDisplayName(String displayName) {
        throw new UnsupportedOperationException("Player is console");
    }

    @Override
    default InetAddress getAddress() {
        return null;
    }

    @Override
    default String getServer() {
        throw new UnsupportedOperationException("Player is console");
    }
}