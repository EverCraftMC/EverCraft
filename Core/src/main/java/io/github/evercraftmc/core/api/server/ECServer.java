package io.github.evercraftmc.core.api.server;

import java.util.Collection;
import java.util.UUID;
import io.github.evercraftmc.core.ECPlugin;
import io.github.evercraftmc.core.api.server.player.ECPlayer;
import io.github.evercraftmc.core.impl.ECEnvironment;

public interface ECServer {
    public ECPlugin getPlugin();

    public String getName();

    public ECEnvironment getEnvironment();

    public Collection<ECPlayer> getPlayers();

    public ECPlayer getPlayer(UUID uuid);

    public ECPlayer getPlayer(String name);

    public Collection<ECPlayer> getOnlinePlayers();

    public ECPlayer getOnlinePlayer(UUID uuid);

    public ECPlayer getOnlinePlayer(String name);

    public ECPlayer getConsole();

    public default void broadcastMessage(String message) {
        for (ECPlayer player : this.getOnlinePlayers()) {
            player.sendMessage(message);
        }
    }

    public ECCommandManager getCommandManager();

    public ECEventManager getEventManager();

    public ECScheduler getScheduler();
}