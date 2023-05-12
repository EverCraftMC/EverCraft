/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ProxyServer
 *  net.md_5.bungee.api.connection.ProxiedPlayer
 */
package io.github.evercraftmc.core.impl.bungee.server;

import io.github.evercraftmc.core.ECData;
import io.github.evercraftmc.core.ECPlugin;
import io.github.evercraftmc.core.api.server.ECCommandManager;
import io.github.evercraftmc.core.api.server.ECEventManager;
import io.github.evercraftmc.core.api.server.ECServer;
import io.github.evercraftmc.core.api.server.player.ECPlayer;
import io.github.evercraftmc.core.impl.ECEnvironment;
import io.github.evercraftmc.core.impl.bungee.server.ECBungeeCommandManager;
import io.github.evercraftmc.core.impl.bungee.server.ECBungeeEventManager;
import io.github.evercraftmc.core.impl.bungee.server.player.ECBungeePlayer;
import io.github.evercraftmc.core.impl.spigot.server.player.ECSpigotPlayer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ECBungeeServer
implements ECServer {
    protected ECPlugin plugin;
    protected ProxyServer handle;
    protected ECBungeeCommandManager commandManager;
    protected ECBungeeEventManager eventManager;

    public ECBungeeServer(ECPlugin plugin, ProxyServer handle) {
        this.plugin = plugin;
        this.handle = handle;
        this.commandManager = new ECBungeeCommandManager(this);
        this.eventManager = new ECBungeeEventManager(this);
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
        for (ECData.Player player : this.plugin.getData().players.values()) {
            players.add(new ECBungeePlayer(player));
        }
        return Collections.unmodifiableCollection(players);
    }

    @Override
    public ECPlayer getPlayer(UUID uuid) {
        if (this.plugin.getData().players.containsKey(uuid.toString())) {
            return new ECBungeePlayer(this.plugin.getData().players.get(uuid.toString()));
        }
        return null;
    }

    @Override
    public ECPlayer getPlayer(String name) {
        for (ECData.Player player : this.plugin.getData().players.values()) {
            if (!player.name.equalsIgnoreCase(name)) continue;
            return new ECBungeePlayer(player);
        }
        return null;
    }

    @Override
    public Collection<ECPlayer> getOnlinePlayers() {
        ArrayList<ECBungeePlayer> players = new ArrayList<ECBungeePlayer>();
        for (ProxiedPlayer bungeePlayer : this.handle.getPlayers()) {
            if (!this.plugin.getData().players.containsKey(bungeePlayer.getUniqueId().toString())) continue;
            players.add(new ECBungeePlayer(this.plugin.getData().players.get(bungeePlayer.getUniqueId().toString()), bungeePlayer));
        }
        return Collections.unmodifiableCollection(players);
    }

    @Override
    public ECPlayer getOnlinePlayer(UUID uuid) {
        if (this.handle.getPlayer(uuid) != null) {
            if (this.plugin.getData().players.containsKey(uuid.toString())) {
                return new ECBungeePlayer(this.plugin.getData().players.get(uuid.toString()), this.handle.getPlayer(uuid));
            }
            return null;
        }
        return null;
    }

    @Override
    public ECPlayer getOnlinePlayer(String name) {
        if (this.handle.getPlayer(name) != null) {
            for (ECData.Player player : this.plugin.getData().players.values()) {
                if (!player.name.equalsIgnoreCase(name)) continue;
                return new ECBungeePlayer(player, this.handle.getPlayer(name));
            }
            return null;
        }
        return null;
    }

    @Override
    public ECPlayer getConsole() {
        ECData.Player console = new ECData.Player();
        console.uuid = UUID.fromString("00000000-0000-0000-0000-000000000000");
        console.name = "Console";
        console.displayName = "&lConsole";
        return new ECSpigotPlayer(console);
    }

    @Override
    public ECCommandManager getCommandManager() {
        return this.commandManager;
    }

    @Override
    public ECEventManager getEventManager() {
        return this.eventManager;
    }
}

