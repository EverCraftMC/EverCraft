package io.github.evercraftmc.core.impl.spigot.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import io.github.evercraftmc.core.ECData;
import io.github.evercraftmc.core.ECPlugin;
import io.github.evercraftmc.core.api.server.ECCommandManager;
import io.github.evercraftmc.core.api.server.ECEventManager;
import io.github.evercraftmc.core.api.server.ECScheduler;
import io.github.evercraftmc.core.api.server.ECServer;
import io.github.evercraftmc.core.api.server.player.ECPlayer;
import io.github.evercraftmc.core.impl.ECEnvironment;
import io.github.evercraftmc.core.impl.spigot.server.player.ECSpigotPlayer;

public class ECSpigotServer implements ECServer {
    protected ECPlugin plugin;

    protected Server handle;

    protected ECSpigotCommandManager commandManager;
    protected ECSpigotEventManager eventManager;

    protected ECSpigotScheduler scheduler;

    public ECSpigotServer(ECPlugin plugin, Server handle) {
        this.plugin = plugin;

        this.handle = handle;

        this.commandManager = new ECSpigotCommandManager(this);
        this.eventManager = new ECSpigotEventManager(this);

        this.scheduler = new ECSpigotScheduler(this);
    }

    @Override
    public ECPlugin getPlugin() {
        return this.plugin;
    }

    public Server getHandle() {
        return this.handle;
    }

    @Override
    public String getName() {
        return this.handle.getName() + " v" + this.handle.getVersion();
    }

    @Override
    public ECEnvironment getEnvironment() {
        return ECEnvironment.SPIGOT;
    }

    @Override
    public Collection<ECPlayer> getPlayers() {
        ArrayList<ECSpigotPlayer> players = new ArrayList<ECSpigotPlayer>();

        for (ECData.Player player : this.plugin.getData().players.values()) {
            players.add(new ECSpigotPlayer(player));
        }

        return Collections.unmodifiableCollection(players);
    }

    @Override
    public ECPlayer getPlayer(UUID uuid) {
        if (this.plugin.getData().players.containsKey(uuid.toString())) {
            return new ECSpigotPlayer(this.plugin.getData().players.get(uuid.toString()));
        } else {
            return null;
        }
    }

    @Override
    public ECPlayer getPlayer(String name) {
        for (ECData.Player player : this.plugin.getData().players.values()) {
            if (player.name.equalsIgnoreCase(name)) {
                return new ECSpigotPlayer(player);
            }
        }

        return null;
    }

    @Override
    public Collection<ECPlayer> getOnlinePlayers() {
        ArrayList<ECSpigotPlayer> players = new ArrayList<ECSpigotPlayer>();

        for (Player spigotPlayer : this.handle.getOnlinePlayers()) {
            if (this.plugin.getData().players.containsKey(spigotPlayer.getUniqueId().toString())) {
                players.add(new ECSpigotPlayer(this.plugin.getData().players.get(spigotPlayer.getUniqueId().toString()), spigotPlayer));
            }
        }

        return Collections.unmodifiableCollection(players);
    }

    @Override
    public ECPlayer getOnlinePlayer(UUID uuid) {
        if (this.handle.getPlayer(uuid) != null) {
            if (this.plugin.getData().players.containsKey(uuid.toString())) {
                return new ECSpigotPlayer(this.plugin.getData().players.get(uuid.toString()), this.handle.getPlayer(uuid));
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
            for (ECData.Player player : this.plugin.getData().players.values()) {
                if (player.name.equalsIgnoreCase(name)) {
                    return new ECSpigotPlayer(player, this.handle.getPlayer(name));
                }
            }

            return null;
        } else {
            return null;
        }
    }

    @Override
    public ECPlayer getConsole() {
        ECData.Player console = new ECData.Player(UUID.fromString("00000000-0000-0000-0000-000000000000"), "Console");
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

    @Override
    public ECScheduler getScheduler() {
        return this.scheduler;
    }
}