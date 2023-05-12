package io.github.evercraftmc.core.api.server;

import io.github.evercraftmc.core.api.commands.ECCommand;

public interface ECCommandManager {
    public ECCommand get(String name);

    public ECCommand register(ECCommand command);

    public ECCommand unregister(ECCommand command);

    public void unregisterAll();
}