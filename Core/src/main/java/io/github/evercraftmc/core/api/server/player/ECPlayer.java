package io.github.evercraftmc.core.api.server.player;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ECPlayer {
    @NotNull UUID getUuid();

    @NotNull String getName();

    @NotNull String getDisplayName();

    void setDisplayName(@NotNull String displayName);

    @NotNull String getOnlineDisplayName();

    void setOnlineDisplayName(@NotNull String displayName);

    @Nullable InetAddress getAddress();

    @Nullable InetSocketAddress getServerAddress();

    @Nullable String getServer();

    boolean hasPermission(@NotNull String permission);

    void sendMessage(@NotNull String message);

    void kick(@NotNull String message);
}