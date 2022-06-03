package io.github.evercraftmc.evercraft.spigot.games;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;

public abstract class TeamedGame extends RoundedGame {
    protected Map<Player, String> teams = new HashMap<Player, String>();

    protected TeamedGame(String name, String warpName, Float minPlayers, Float maxPlayers) {
        super(name, warpName, minPlayers, maxPlayers);
    }

    @Override
    public void leave(Player player, LeaveReason leaveReason) {
        super.leave(player, leaveReason);

        this.leaveTeam(player);
    }

    public void joinTeam(Player player, String team) {
        this.teams.put(player, team);
    }

    public void leaveTeam(Player player) {
        this.teams.remove(player);
    }

    public String getTeam(Player player) {
        return this.teams.get(player);
    }
}