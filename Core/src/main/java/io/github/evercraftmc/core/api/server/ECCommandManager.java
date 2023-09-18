package io.github.evercraftmc.core.api.server;

import io.github.evercraftmc.core.api.commands.ECCommand;

public interface ECCommandManager {
    ECCommand get(String name);

    ECCommand register(ECCommand command);

    ECCommand register(ECCommand command, boolean distinguishServer);

    ECCommand register(ECCommand command, boolean distinguishServer, boolean forwardToOther);

    ECCommand unregister(ECCommand command);

    void unregisterAll();
}