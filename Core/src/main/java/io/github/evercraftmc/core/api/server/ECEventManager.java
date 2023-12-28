package io.github.evercraftmc.core.api.server;

import io.github.evercraftmc.core.api.events.ECEvent;
import io.github.evercraftmc.core.api.events.ECListener;
import org.jetbrains.annotations.NotNull;

public interface ECEventManager {
    void emit(@NotNull ECEvent event);

    @NotNull ECListener register(@NotNull ECListener listener);

    @NotNull ECListener unregister(@NotNull ECListener listener);

    void unregisterAll();
}