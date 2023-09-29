package io.github.evercraftmc.core.impl.bungee.server;

import io.github.evercraftmc.core.ECPlayerData;
import io.github.evercraftmc.core.ECPlugin;
import io.github.evercraftmc.core.api.server.ECServer;
import io.github.evercraftmc.core.impl.ECEnvironment;
import io.github.evercraftmc.core.impl.bungee.server.player.ECBungeeConsole;
import io.github.evercraftmc.core.impl.bungee.server.player.ECBungeePlayer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ECBungeeServer implements ECServer {
    protected ECPlugin plugin;

    protected ProxyServer handle;

    protected ECBungeeCommandManager commandManager;
    protected ECBungeeEventManager eventManager;

    protected ECBungeeScheduler scheduler;

    public ECBungeeServer(ECPlugin plugin, ProxyServer handle) {
        this.plugin = plugin;

        this.handle = handle;

        this.eventManager = new ECBungeeEventManager(this);
        this.commandManager = new ECBungeeCommandManager(this);

        this.scheduler = new ECBungeeScheduler(this);
    }

    @Override
    public ECPlugin getPlugin() {
        return this.plugin;
    }

    public ProxyServer getHandle() {
        return this.handle;
    }

    @Override
    public String getName() {
        return this.handle.getName() + " v" + this.handle.getVersion();
    }

    @Override
    public ECEnvironment getEnvironment() {
        return ECEnvironment.BUNGEE;
    }

    @Override
    public Collection<ECBungeePlayer> getPlayers() {
        ArrayList<ECBungeePlayer> players = new ArrayList<>();

        for (ECPlayerData.Player player : this.plugin.getPlayerData().players.values()) {
            players.add(new ECBungeePlayer(player));
        }

        return Collections.unmodifiableCollection(players);
    }

    @Override
    public ECBungeePlayer getPlayer(UUID uuid) {
        if (this.plugin.getPlayerData().players.containsKey(uuid.toString())) {
            return new ECBungeePlayer(this.plugin.getPlayerData().players.get(uuid.toString()));
        } else {
            return null;
        }
    }

    @Override
    public ECBungeePlayer getPlayer(String name) {
        for (ECPlayerData.Player player : this.plugin.getPlayerData().players.values()) {
            if (player.name.equalsIgnoreCase(name)) {
                return new ECBungeePlayer(player);
            }
        }

        return null;
    }

    @Override
    public Collection<ECBungeePlayer> getOnlinePlayers() {
        ArrayList<ECBungeePlayer> players = new ArrayList<>();

        for (ProxiedPlayer bungeePlayer : this.handle.getPlayers()) {
            if (this.plugin.getPlayerData().players.containsKey(bungeePlayer.getUniqueId().toString())) {
                players.add(new ECBungeePlayer(this.plugin.getPlayerData().players.get(bungeePlayer.getUniqueId().toString()), bungeePlayer));
            }
        }

        return Collections.unmodifiableCollection(players);
    }

    @Override
    public ECBungeePlayer getOnlinePlayer(UUID uuid) {
        if (this.handle.getPlayer(uuid) != null) {
            if (this.plugin.getPlayerData().players.containsKey(uuid.toString())) {
                return new ECBungeePlayer(this.plugin.getPlayerData().players.get(uuid.toString()), this.handle.getPlayer(uuid));
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public ECBungeePlayer getOnlinePlayer(String name) {
        if (this.handle.getPlayer(name) != null) {
            for (ECPlayerData.Player player : this.plugin.getPlayerData().players.values()) {
                if (player.name.equalsIgnoreCase(name)) {
                    return new ECBungeePlayer(player, this.handle.getPlayer(name));
                }
            }

            return null;
        } else {
            return null;
        }
    }

    public ECBungeePlayer getOnlinePlayer(Connection connection) {
        for (ProxiedPlayer bungeePlayer : this.handle.getPlayers()) {
            if (bungeePlayer.getPendingConnection().equals(connection)) {
                for (ECPlayerData.Player player : this.plugin.getPlayerData().players.values()) {
                    if (player.uuid.equals(bungeePlayer.getUniqueId())) {
                        return new ECBungeePlayer(player, bungeePlayer);
                    }
                }

                return null;
            }
        }

        return null;
    }

    @Override
    public ECBungeeConsole getConsole() {
        return new ECBungeeConsole(this.handle.getConsole());
    }

    @Override
    public ECBungeeCommandManager getCommandManager() {
        return this.commandManager;
    }

    @Override
    public ECBungeeEventManager getEventManager() {
        return this.eventManager;
    }

    @Override
    public ECBungeeScheduler getScheduler() {
        return this.scheduler;
    }
}