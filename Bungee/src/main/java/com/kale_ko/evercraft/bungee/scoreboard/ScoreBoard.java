package com.kale_ko.evercraft.bungee.scoreboard;

import java.util.HashMap;
import java.util.Map;
import com.kale_ko.evercraft.shared.util.Closable;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.score.Scoreboard;

public class ScoreBoard implements Closable {
    private Map<ProxiedPlayer, Scoreboard> scoreBoards;

    public ScoreBoard() {
        this.scoreBoards = new HashMap<ProxiedPlayer, Scoreboard>();
    }

    public void close() {

    }
}