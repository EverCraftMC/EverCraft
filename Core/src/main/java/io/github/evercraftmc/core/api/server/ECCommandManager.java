package io.github.evercraftmc.core.api.server;

import io.github.evercraftmc.core.api.commands.ECCommand;
import org.jetbrains.annotations.NotNull;

public interface ECCommandManager {
    @NotNull ECCommand get(@NotNull String name);

    @NotNull ECCommand register(@NotNull ECCommand command);

    @NotNull ECCommand register(@NotNull ECCommand command, boolean distinguishServer);

    @NotNull ECCommand register(@NotNull ECCommand command, boolean distinguishServer, boolean forwardToOther);

    @NotNull ECCommand unregister(@NotNull ECCommand command);

    void unregisterAll();
}