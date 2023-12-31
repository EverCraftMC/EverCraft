package io.github.evercraftmc.core.api.server;

import io.github.evercraftmc.core.api.commands.ECCommand;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public interface ECCommandManager {
    @NotNull List<ECCommand> getAll();

    @NotNull Map<String, List<ECCommand>> getAllNative();

    @NotNull ECCommand get(@NotNull String name);

    @NotNull ECCommand register(@NotNull ECCommand command);

    @NotNull ECCommand register(@NotNull ECCommand command, boolean distinguishServer);

    @NotNull ECCommand register(@NotNull ECCommand command, boolean distinguishServer, boolean forwardToOther);

    @NotNull ECCommand unregister(@NotNull ECCommand command);

    void unregisterAll();
}