/*
 * Decompiled with CFR 0.152.
 */
package io.github.evercraftmc.core.api.server;

import io.github.evercraftmc.core.api.events.ECEvent;
import io.github.evercraftmc.core.api.events.ECListener;

public interface ECEventManager {
    public void emit(ECEvent var1);

    public ECListener register(ECListener var1);

    public ECListener unregister(ECListener var1);

    public void unregisterAll();
}

