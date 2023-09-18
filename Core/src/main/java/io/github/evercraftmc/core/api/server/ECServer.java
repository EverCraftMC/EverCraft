package io.github.evercraftmc.core.api.server;

import io.github.evercraftmc.core.ECPlugin;
import io.github.evercraftmc.core.api.server.player.ECPlayer;
import io.github.evercraftmc.core.impl.ECEnvironment;
import java.util.Collection;
import java.util.UUID;

public interface ECServer {
    ECPlugin getPlugin();

    String getName();

    ECEnvironment getEnvironment();

    Collection<ECPlayer> getPlayers();

    ECPlayer getPlayer(UUID uuid);

    ECPlayer getPlayer(String name);

    Collection<ECPlayer> getOnlinePlayers();

    ECPlayer getOnlinePlayer(UUID uuid);

    ECPlayer getOnlinePlayer(String name);

    ECPlayer getConsole();

    default void broadcastMessage(String message) {
        for (ECPlayer player : this.getOnlinePlayers()) {
            player.sendMessage(message);
        }
    }

    ECCommandManager getCommandManager();

    ECEventManager getEventManager();

    ECScheduler getScheduler();
}