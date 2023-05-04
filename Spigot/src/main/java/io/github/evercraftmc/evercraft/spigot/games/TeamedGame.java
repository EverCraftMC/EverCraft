package io.github.evercraftmc.evercraft.spigot.games;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.entity.Player;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import io.github.evercraftmc.evercraft.spigot.SpigotMain;
import io.github.evercraftmc.evercraft.spigot.util.formatting.ComponentFormatter;

public abstract class TeamedGame extends RoundedGame {
    protected List<String> teamsList;

    protected Map<Player, String> playerTeams = new HashMap<Player, String>();

    protected TeamedGame(String name, String warpName, Integer minPlayers, Integer maxPlayers, Integer countdownLength, List<String> teamsList) {
        super(name, warpName, minPlayers, maxPlayers, countdownLength);

        this.teamsList = teamsList;
    }

    public List<String> getTeamsList() {
        return new ArrayList<String>(teamsList);
    }

    @Override
    public void leave(Player player, LeaveReason leaveReason) {
        super.leave(player, leaveReason);

        if (this.playerTeams.containsKey(player)) {
            this.leaveTeam(player, leaveReason); // TODO Dont send message
        }
    }

    public String getTeam(Player player) {
        return this.playerTeams.get(player);
    }

    public void joinTeam(Player player, String team) {
        if (!this.playerTeams.containsKey(player)) {
            if (this.teamsList.contains(team)) {
                this.playerTeams.put(player, team);

                player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().games.joinedTeam.replace("{team}", team))));

                for (Player player2 : this.players) {
                    if (player2 != player) {
                        player2.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().games.teamJoin.replace("{player}", ComponentFormatter.componentToString(player.displayName())).replace("{team}", team))));
                    }
                }
            } else {
                throw new RuntimeException("Team does not exist");
            }
        } else {
            throw new RuntimeException("Player is already on a team");
        }
    }

    public void leaveTeam(Player player, LeaveReason leaveReason) {
        if (this.playerTeams.containsKey(player)) {
            String team = this.playerTeams.get(player);
            this.playerTeams.remove(player);

            if (leaveReason != LeaveReason.GAMEOVER && leaveReason != LeaveReason.DISCONNECT && leaveReason != LeaveReason.DEATH) {
                player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().games.leftTeam.replace("{team}", team))));

                for (Player player2 : this.players) {
                    if (player2 != player) {
                        player2.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().games.teamLeave.replace("{player}", ComponentFormatter.componentToString(player.displayName())).replace("{team}", team))));
                    }
                }
            }
        } else {
            throw new RuntimeException("Player is not on a team");
        }
    }
}