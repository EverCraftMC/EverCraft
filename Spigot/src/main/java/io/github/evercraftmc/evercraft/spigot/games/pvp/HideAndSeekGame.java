package io.github.evercraftmc.evercraft.spigot.games.pvp;

import java.util.Arrays;
import java.util.List;
import org.bukkit.entity.Player;
import io.github.evercraftmc.evercraft.spigot.SpigotMain;
import io.github.evercraftmc.evercraft.spigot.commands.kit.KitCommand;
import io.github.evercraftmc.evercraft.spigot.games.TeamedGame;

public class HideAndSeekGame extends TeamedGame {
    protected String startWarpName;

    protected String seekerKitName;

    public HideAndSeekGame(String name, String warpName, Integer countdownLength, List<String> teamsList, String startWarpName, String seekerKitName) {
        super(name, warpName, 1f, Float.MAX_VALUE, countdownLength, teamsList);

        this.startWarpName = startWarpName;

        this.seekerKitName = seekerKitName;
    }

    public String getStartWarpName() {
        return this.startWarpName;
    }

    public String getSeekerKitName() {
        return this.seekerKitName;
    }

    @Override
    public void startNoCountdown() {
        super.startNoCountdown();

        for (Player player : this.players) {
            if (!this.playerTeams.containsKey(player)) {
                this.playerTeams.put(player, "hiders");
            }

            if (this.playerTeams.get(player).equalsIgnoreCase("hiders")) {
                player.teleport(SpigotMain.getInstance().getWarps().getParsed().warps.get(startWarpName).toBukkitLocation());
            } else if (this.playerTeams.get(player).equalsIgnoreCase("seekers")) {
                player.teleport(SpigotMain.getInstance().getWarps().getParsed().warps.get(startWarpName).toBukkitLocation());

                new KitCommand("kit", null, Arrays.asList(), null).run(player, new String[] { seekerKitName, "true" });
            }
        }
    }

    @Override
    public void leave(Player player, LeaveReason leaveReason) {
        super.leave(player, leaveReason);

        if (leaveReason != LeaveReason.GAMEOVER) {
            if (this.started) {
                Integer hiders = 0;
                for (Player player2 : this.players) {
                    if (this.playerTeams.get(player2).equalsIgnoreCase("hiders")) {
                        hiders++;
                    }
                }

                if (hiders == 0) {
                    this.stop();
                }
            }
        }
    }

    @Override
    public void joinTeam(Player player, String team) {
        super.joinTeam(player, team);

        if (!this.started) {
            Integer hiders = 0;
            Integer seekers = 0;

            for (Player player2 : this.players) {
                if (this.playerTeams.containsKey(player2)) {
                    if (this.playerTeams.get(player2).equalsIgnoreCase("hiders")) {
                        hiders++;
                    } else if (this.playerTeams.get(player2).equalsIgnoreCase("seekers")) {
                        seekers++;
                    }
                }
            }

            if (hiders > 0 && seekers > 0) {
                this.start();
            }
        }
    }
}