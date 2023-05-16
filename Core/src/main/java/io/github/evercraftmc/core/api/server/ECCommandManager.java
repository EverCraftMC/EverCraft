package io.github.evercraftmc.core.api.server;

import io.github.evercraftmc.core.api.commands.ECCommand;

public interface ECCommandManager {
    public ECCommand get(String name);

    public ECCommand register(ECCommand command);

    public ECCommand register(ECCommand command, boolean addPrefix, boolean distinguishServer);

    public ECCommand unregister(ECCommand command);

    public void unregisterAll();
}