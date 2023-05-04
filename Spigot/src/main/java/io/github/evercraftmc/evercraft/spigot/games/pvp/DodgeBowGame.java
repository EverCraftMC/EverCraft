package io.github.evercraftmc.evercraft.spigot.games.pvp;

import java.util.Arrays;
import org.bukkit.entity.Player;
import io.github.evercraftmc.evercraft.spigot.SpigotMain;
import io.github.evercraftmc.evercraft.spigot.commands.kit.KitCommand;
import io.github.evercraftmc.evercraft.spigot.games.TeamedGame;

public class DodgeBowGame extends TeamedGame {
    protected String runnerWarpName;
    protected String bowerWarpName;

    protected String bowerKitName;

    public DodgeBowGame(String name, String warpName, Integer countdownLength, String runnerWarpName, String bowerWarpName, String bowerKitName) {
        super(name, warpName, 1f, Float.MAX_VALUE, countdownLength, Arrays.asList("runners", "bowers"));

        this.runnerWarpName = runnerWarpName;
        this.bowerWarpName = bowerWarpName;

        this.bowerKitName = bowerKitName;
    }

    public String getRunnerWarpName() {
        return this.runnerWarpName;
    }

    public String getBowerWarpName() {
        return this.bowerWarpName;
    }

    public String getBowerKitName() {
        return this.bowerKitName;
    }

    @Override
    public void startNoCountdown() {
        super.startNoCountdown();

        for (Player player : this.players) {
            if (!this.playerTeams.containsKey(player)) {
                this.playerTeams.put(player, "runners");
            }

            if (this.playerTeams.get(player).equalsIgnoreCase("runners")) {
                player.teleport(SpigotMain.getInstance().getWarps().get().warps.get(runnerWarpName).toBukkitLocation());
            } else if (this.playerTeams.get(player).equalsIgnoreCase("bowers")) {
                player.teleport(SpigotMain.getInstance().getWarps().get().warps.get(bowerWarpName).toBukkitLocation());

                new KitCommand("kit", null, Arrays.asList(), null).run(player, new String[] { bowerKitName, "true" });
            }
        }
    }

    @Override
    public void leave(Player player, LeaveReason leaveReason) {
        super.leave(player, leaveReason);

        if (leaveReason != LeaveReason.GAMEOVER) {
            if (this.started) {
                Integer runners = 0;
                for (Player player2 : this.players) {
                    if (this.playerTeams.get(player2).equalsIgnoreCase("runners")) {
                        runners++;
                    }
                }

                if (runners == 0) {
                    this.stop();
                }
            }
        }
    }

    @Override
    public void joinTeam(Player player, String team) {
        super.joinTeam(player, team);

        if (!this.started) {
            Integer runners = 0;
            Integer bowers = 0;

            for (Player player2 : this.players) {
                if (this.playerTeams.containsKey(player2)) {
                    if (this.playerTeams.get(player2).equalsIgnoreCase("runners")) {
                        runners++;
                    } else if (this.playerTeams.get(player2).equalsIgnoreCase("bowers")) {
                        bowers++;
                    }
                }
            }

            if (runners > 0 && bowers > 0) {
                this.start();
            }
        }
    }
}