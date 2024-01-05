package io.github.evercraftmc.core.impl.bungee.server;

import io.github.evercraftmc.core.ECPlayerData;
import io.github.evercraftmc.core.ECPlugin;
import io.github.evercraftmc.core.api.server.ECServer;
import io.github.evercraftmc.core.impl.ECEnvironment;
import io.github.evercraftmc.core.impl.ECEnvironmentType;
import io.github.evercraftmc.core.impl.bungee.server.player.ECBungeeConsole;
import io.github.evercraftmc.core.impl.bungee.server.player.ECBungeePlayer;
import java.util.*;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ECBungeeServer implements ECServer {
    protected @NotNull ECPlugin plugin;

    protected @NotNull ProxyServer handle;

    protected @NotNull ECBungeeCommandManager commandManager;
    protected @NotNull ECBungeeEventManager eventManager;

    protected @NotNull ECBungeeScheduler scheduler;

    public ECBungeeServer(@NotNull ECPlugin plugin, @NotNull ProxyServer handle) {
        this.plugin = plugin;

        this.handle = handle;

        this.eventManager = new ECBungeeEventManager(this);
        this.commandManager = new ECBungeeCommandManager(this);

        this.scheduler = new ECBungeeScheduler(this);
    }

    @Override
    public @NotNull ECPlugin getPlugin() {
        return this.plugin;
    }

    public @NotNull ProxyServer getHandle() {
        return this.handle;
    }

    @Override
    public @NotNull String getSoftwareVersion() {
        return this.handle.getName() + " " + this.handle.getVersion();
    }

    @Override
    public @NotNull String getMinecraftVersion() {
        String[] versions = this.handle.getConfig().getGameVersion().split("-");
        return versions[versions.length - 1].trim().replace(".x", "");
    }

    public @NotNull String getAllMinecraftVersions() {
        return this.handle.getConfig().getGameVersion();
    }

    @Override
    public @NotNull ECEnvironment getEnvironment() {
        return ECEnvironment.BUNGEE;
    }

    @Override
    public @NotNull ECEnvironmentType getEnvironmentType() {
        return ECEnvironmentType.PROXY;
    }

    @Override
    public @NotNull Collection<ECBungeePlayer> getPlayers() {
        ArrayList<ECBungeePlayer> players = new ArrayList<>();

        for (ECPlayerData.Player player : this.plugin.getPlayerData().players.values()) {
            players.add(new ECBungeePlayer(player));
        }

        return Collections.unmodifiableCollection(players);
    }

    @Override
    public ECBungeePlayer getPlayer(@NotNull UUID uuid) {
        if (this.plugin.getPlayerData().players.containsKey(uuid.toString())) {
            return new ECBungeePlayer(this.plugin.getPlayerData().players.get(uuid.toString()));
        }

        return null;
    }

    @Override
    public ECBungeePlayer getPlayer(@NotNull String name) {
        for (ECPlayerData.Player player : this.plugin.getPlayerData().players.values()) {
            if (player.name.equalsIgnoreCase(name)) {
                return new ECBungeePlayer(player);
            }
        }

        return null;
    }

    @Override
    public @NotNull Collection<ECBungeePlayer> getOnlinePlayers() {
        ArrayList<ECBungeePlayer> players = new ArrayList<>();

        for (ProxiedPlayer bungeePlayer : this.handle.getPlayers()) {
            if (this.plugin.getPlayerData().players.containsKey(bungeePlayer.getUniqueId().toString())) {
                players.add(new ECBungeePlayer(this.plugin.getPlayerData().players.get(bungeePlayer.getUniqueId().toString()), bungeePlayer));
            }
        }

        return Collections.unmodifiableCollection(players);
    }

    @Override
    public ECBungeePlayer getOnlinePlayer(@NotNull UUID uuid) {
        ProxiedPlayer bungeePlayer = this.handle.getPlayer(uuid);
        if (bungeePlayer != null && this.plugin.getPlayerData().players.containsKey(uuid.toString())) {
            return new ECBungeePlayer(this.plugin.getPlayerData().players.get(uuid.toString()), bungeePlayer);
        }

        return null;
    }

    @Override
    public ECBungeePlayer getOnlinePlayer(@NotNull String name) {
        ProxiedPlayer bungeePlayer = this.handle.getPlayer(name);
        if (bungeePlayer != null) {
            for (ECPlayerData.Player player : this.plugin.getPlayerData().players.values()) {
                if (player.name.equalsIgnoreCase(name)) {
                    return new ECBungeePlayer(player, bungeePlayer);
                }
            }
        }

        return null;
    }

    public ECBungeePlayer getOnlinePlayer(@NotNull Connection connection) {
        for (ProxiedPlayer bungeePlayer : this.handle.getPlayers()) {
            if (bungeePlayer.getPendingConnection().getSocketAddress().equals(connection.getSocketAddress())) {
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

    public @NotNull String getDefaultServer() {
        List<String> servers = this.handle.getConfig().getListeners().stream().toList().get(0).getServerPriority();
        return servers.get(0);
    }

    public @NotNull String getFallbackServer() {
        List<String> servers = this.handle.getConfig().getListeners().stream().toList().get(0).getServerPriority();
        return servers.get(servers.size() - 1);
    }

    public boolean getServer(@NotNull String name) {
        return this.handle.getServerInfo(name) != null;
    }

    public @Nullable String getDefaultMotd() {
        return this.handle.getConfig().getListeners().stream().toList().get(0).getMotd();
    }

    public @Nullable String getServerMotd(@NotNull String name) {
        if (!getServer(name)) {
            return null;
        }
        return this.handle.getServerInfo(name).getMotd();
    }

    @Override
    public @NotNull ECBungeeConsole getConsole() {
        return new ECBungeeConsole(this.handle.getConsole());
    }

    @Override
    public @NotNull ECBungeeCommandManager getCommandManager() {
        return this.commandManager;
    }

    @Override
    public @NotNull ECBungeeEventManager getEventManager() {
        return this.eventManager;
    }

    @Override
    public @NotNull ECBungeeScheduler getScheduler() {
        return this.scheduler;
    }
}