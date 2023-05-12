/*
 * Decompiled with CFR 0.152.
 */
package io.github.evercraftmc.core.api.server;

import io.github.evercraftmc.core.ECPlugin;
import io.github.evercraftmc.core.api.server.ECCommandManager;
import io.github.evercraftmc.core.api.server.ECEventManager;
import io.github.evercraftmc.core.api.server.player.ECPlayer;
import io.github.evercraftmc.core.impl.ECEnvironment;
import java.util.Collection;
import java.util.UUID;

public interface ECServer {
    public ECPlugin getPlugin();

    public String getName();

    public ECEnvironment getEnvironment();

    public Collection<ECPlayer> getPlayers();

    public ECPlayer getPlayer(UUID var1);

    public ECPlayer getPlayer(String var1);

    public Collection<ECPlayer> getOnlinePlayers();

    public ECPlayer getOnlinePlayer(UUID var1);

    public ECPlayer getOnlinePlayer(String var1);

    public ECPlayer getConsole();

    public ECCommandManager getCommandManager();

    public ECEventManager getEventManager();
}

