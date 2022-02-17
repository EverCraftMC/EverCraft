package com.kale_ko.kalesutilities.shared.networking;

import java.util.List;

public class ServerPlayers {
    private int online;
    private int max;
    private List<ServerPlayer> sample;

    public int getOnline() {
        return online;
    }

    public int getMax() {
        return max;
    }

    public List<ServerPlayer> getOnlinePlayers() {
        return sample;
    }
}