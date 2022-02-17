package com.kale_ko.kalesutilities.shared.networking;

public class ServerPing {
    private ServerMotd description;
    private ServerPlayers players;
    private ServerVersion version;
    private String favicon;
    private int time;

    public ServerMotd getMotd() {
        return this.description;
    }

    public ServerPlayers getPlayers() {
        return this.players;
    }

    public ServerVersion getVersion() {
        return this.version;
    }

    public String getFavicon() {
        return this.favicon;
    }

    public int getPing() {
        return this.time;
    }

    public void setPing(int time) {
        this.time = time;
    }
}