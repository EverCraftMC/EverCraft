package io.github.evercraftmc.core.impl.bungee.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import io.github.evercraftmc.core.ECPlayerData;
import io.github.evercraftmc.core.ECPlugin;
import io.github.evercraftmc.core.api.server.ECCommandManager;
import io.github.evercraftmc.core.api.server.ECEventManager;
import io.github.evercraftmc.core.api.server.ECScheduler;
import io.github.evercraftmc.core.api.server.ECServer;
import io.github.evercraftmc.core.api.server.player.ECPlayer;
import io.github.evercraftmc.core.impl.ECEnvironment;
import io.github.evercraftmc.core.impl.bungee.server.player.ECBungeeConsole;
import io.github.evercraftmc.core.impl.bungee.server.player.ECBungeePlayer;
import net.md_5.bungee.api.ProxyServer;
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
    public Collection<ECPlayer> getPlayers() {
        ArrayList<ECBungeePlayer> players = new ArrayList<ECBungeePlayer>();

        for (ECPlayerData.Player player : this.plugin.getPlayerData().players.values()) {
            players.add(new ECBungeePlayer(player));
        }

        return Collections.unmodifiableCollection(players);
    }

    @Override
    public ECPlayer getPlayer(UUID uuid) {
        if (this.plugin.getPlayerData().players.containsKey(uuid.toString())) {
            return new ECBungeePlayer(this.plugin.getPlayerData().players.get(uuid.toString()));
        } else {
            return null;
        }
    }

    @Override
    public ECPlayer getPlayer(String name) {
        for (ECPlayerData.Player player : this.plugin.getPlayerData().players.values()) {
            if (player.name.equalsIgnoreCase(name)) {
                return new ECBungeePlayer(player);
            }
        }

        return null;
    }

    @Override
    public Collection<ECPlayer> getOnlinePlayers() {
        ArrayList<ECBungeePlayer> players = new ArrayList<ECBungeePlayer>();

        for (ProxiedPlayer bungeePlayer : this.handle.getPlayers()) {
            if (this.plugin.getPlayerData().players.containsKey(bungeePlayer.getUniqueId().toString())) {
                players.add(new ECBungeePlayer(this.plugin.getPlayerData().players.get(bungeePlayer.getUniqueId().toString()), bungeePlayer));
            }
        }

        return Collections.unmodifiableCollection(players);
    }

    @Override
    public ECPlayer getOnlinePlayer(UUID uuid) {
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
    public ECPlayer getOnlinePlayer(String name) {
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

    @Override
    public ECPlayer getConsole() {
        return new ECBungeeConsole(this.handle.getConsole());
    }

    @Override
    public ECCommandManager getCommandManager() {
        return this.commandManager;
    }

    @Override
    public ECEventManager getEventManager() {
        return this.eventManager;
    }

    @Override
    public ECScheduler getScheduler() {
        return this.scheduler;
    }
}