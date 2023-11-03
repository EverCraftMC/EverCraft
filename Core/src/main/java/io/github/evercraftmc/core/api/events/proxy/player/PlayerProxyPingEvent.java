package io.github.evercraftmc.core.api.events.proxy.player;

import io.github.evercraftmc.core.api.events.ECEvent;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.UUID;

public class PlayerProxyPingEvent extends ECEvent {
    protected String motd;

    protected int onlinePlayers;
    protected int maxPlayers;
    protected Map<UUID, String> players;

    protected InetAddress address;
    protected InetSocketAddress serverAddress;

    public PlayerProxyPingEvent(String motd, int onlinePlayers, int maxPlayers, Map<UUID, String> players, InetAddress address, InetSocketAddress serverAddress) {
        this.motd = motd;

        this.onlinePlayers = onlinePlayers;
        this.maxPlayers = maxPlayers;
        this.players = players;

        this.address = address;
        this.serverAddress = serverAddress;
    }

    public String getMotd() {
        return this.motd;
    }

    public void setMotd(String motd) {
        this.motd = motd;
    }

    public int getOnlinePlayers() {
        return this.onlinePlayers;
    }

    public void setOnlinePlayers(int onlinePlayers) {
        this.onlinePlayers = onlinePlayers;
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public Map<UUID, String> getPlayers() {
        return this.players;
    }

    public void setPlayers(Map<UUID, String> players) {
        this.players = players;
    }

    public InetAddress getAddress() {
        return this.address;
    }

    public InetSocketAddress getServerAddress() {
        return this.serverAddress;
    }
}