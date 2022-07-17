package io.github.evercraftmc.evercraft.spigot.games;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import io.github.evercraftmc.evercraft.spigot.SpigotMain;
import io.github.evercraftmc.evercraft.spigot.util.formatting.ComponentFormatter;

public abstract class TeamedGame extends RoundedGame {
    protected Map<Player, String> teams = new HashMap<Player, String>();

    protected TeamedGame(String name, String warpName, Float minPlayers, Float maxPlayers) {
        super(name, warpName, minPlayers, maxPlayers);
    }

    @Override
    public void leave(Player player, LeaveReason leaveReason) {
        super.leave(player, leaveReason);

        if (teams.containsKey(player)) {
            this.leaveTeam(player);
        }
    }

    public void joinTeam(Player player, String team) {
        if (!this.teams.containsKey(player)) {
            this.teams.put(player, team);

            player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getParsed().games.joinedTeam.replace("{team}", team))));

            for (Player player2 : this.players) {
                if (player2 != player) {
                    player2.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getParsed().games.teamJoin.replace("{player}", ComponentFormatter.componentToString(player.displayName())).replace("{team}", team))));
                }
            }
        } else {
            throw new RuntimeException("Player is already on a team");
        }
    }

    public void leaveTeam(Player player) {
        if (this.teams.containsKey(player)) {
            String team = this.teams.get(player);
            this.teams.remove(player);

            player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getParsed().games.leftTeam.replace("{team}", team))));

            for (Player player2 : this.players) {
                if (player2 != player) {
                    player2.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getParsed().games.teamLeave.replace("{player}", ComponentFormatter.componentToString(player.displayName())).replace("{team}", team))));
                }
            }
        } else {
            throw new RuntimeException("Player is not on a team");
        }
    }

    public String getTeam(Player player) {
        return this.teams.get(player);
    }
}