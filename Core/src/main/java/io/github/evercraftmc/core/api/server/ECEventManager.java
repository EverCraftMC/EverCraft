package io.github.evercraftmc.core.api.server;

import io.github.evercraftmc.core.api.events.ECEvent;
import io.github.evercraftmc.core.api.events.ECListener;

public interface ECEventManager {
    void emit(ECEvent event);

    ECListener register(ECListener listener);

    ECListener unregister(ECListener listener);

    void unregisterAll();
}