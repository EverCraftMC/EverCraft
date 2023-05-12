package io.github.evercraftmc.core.api.server;

import io.github.evercraftmc.core.api.events.ECEvent;
import io.github.evercraftmc.core.api.events.ECListener;

public interface ECEventManager {
    public void emit(ECEvent event);

    public ECListener register(ECListener listener);

    public ECListener unregister(ECListener listener);

    public void unregisterAll();
}