package io.github.evercraftmc.evercraft.spigot.games.pvp;

import java.util.Arrays;
import org.bukkit.entity.Player;
import io.github.evercraftmc.evercraft.spigot.commands.kit.KitCommand;
import io.github.evercraftmc.evercraft.spigot.commands.warp.WarpCommand;
import io.github.evercraftmc.evercraft.spigot.games.TeamedGame;

public class DodgeBowGame extends TeamedGame {
    protected String runnerWarpName;
    protected String bowerWarpName;

    protected String bowerKitName;

    public DodgeBowGame(String name, String warpName, String runnerWarpName, String bowerWarpName, String bowerKitName) {
        super(name, warpName, 1f, Float.MAX_VALUE);

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
    public void start() {
        super.start();

        for (Player player : this.players) {
            if (this.teams.get(player) == null) {
                this.teams.put(player, "runner");
            }

            if (this.teams.get(player).equalsIgnoreCase("runner")) {
                new WarpCommand("warp", null, Arrays.asList(), null).run(player, new String[] { runnerWarpName, "true" });
            }

            if (this.teams.get(player).equalsIgnoreCase("bower")) {
                new WarpCommand("warp", null, Arrays.asList(), null).run(player, new String[] { bowerWarpName, "true" });

                new KitCommand("kit", null, Arrays.asList(), null).run(player, new String[] { bowerKitName, "true" });
            }
        }
    }

    @Override
    public void leave(Player player, LeaveReason leaveReason) {
        super.leave(player, leaveReason);

        if (this.started) {
            Integer runners = 0;
            for (Player player2 : this.players) {
                if (this.teams.get(player2).equalsIgnoreCase("runner")) {
                    runners++;
                }
            }

            if (runners == 0) {
                this.stop();
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
                if (this.teams.get(player2).equalsIgnoreCase("runner")) {
                    runners++;
                } else if (this.teams.get(player2).equalsIgnoreCase("bower")) {
                    bowers++;
                }
            }

            if (runners > 0 && bowers > 0) {
                this.start();
            }
        }
    }
}