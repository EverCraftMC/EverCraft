package com.kale_ko.kalesutilities.shared.networking;

public class ServerPing {
    public String motd;

    public Integer onlinePlayers;
    public Integer maxPlayers;

    public ServerPing(String motd, Integer onlinePlayers, Integer maxPlayers) {
        this.motd = motd;

        this.onlinePlayers = onlinePlayers;
        this.maxPlayers = maxPlayers;
    }
}