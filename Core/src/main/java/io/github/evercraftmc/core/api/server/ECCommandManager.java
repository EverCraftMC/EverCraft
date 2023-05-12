/*
 * Decompiled with CFR 0.152.
 */
package io.github.evercraftmc.core.api.server;

import io.github.evercraftmc.core.api.commands.ECCommand;

public interface ECCommandManager {
    public ECCommand get(String var1);

    public ECCommand register(ECCommand var1);

    public ECCommand unregister(ECCommand var1);

    public void unregisterAll();
}

