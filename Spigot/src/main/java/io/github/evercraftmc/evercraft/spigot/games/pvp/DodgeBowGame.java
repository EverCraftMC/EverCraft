package io.github.evercraftmc.evercraft.spigot.games.pvp;

import java.util.Arrays;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import io.github.evercraftmc.evercraft.spigot.commands.kit.KitCommand;
import io.github.evercraftmc.evercraft.spigot.commands.warp.WarpCommand;
import io.github.evercraftmc.evercraft.spigot.games.TeamedGame;

public class DodgeBowGame extends TeamedGame {
    protected String runnerWarpName;
    protected String bowerWarpName;

    protected String bowerKitName;

    public DodgeBowGame(String name, String warpName, Integer countdownLength, String runnerWarpName, String bowerWarpName, String bowerKitName) {
        super(name, warpName, 1, Integer.MAX_VALUE, countdownLength, Arrays.asList("runners", "bowers"));

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
                new WarpCommand("kit", null, Arrays.asList(), null, true).run(player, new String[] { runnerWarpName });
            } else if (this.playerTeams.get(player).equalsIgnoreCase("bowers")) {
                new WarpCommand("kit", null, Arrays.asList(), null, true).run(player, new String[] { bowerWarpName });

                new KitCommand("kit", null, Arrays.asList(), null, true).run(player, new String[] { bowerKitName });
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

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player player && event.getDamager() instanceof Arrow arrow && arrow.getShooter() instanceof Player player2) {
            if (this.playerTeams.containsKey(player2) && this.getTeam(player2).equalsIgnoreCase("bowers")) {
                event.setDamage(Integer.MAX_VALUE);
            }
        }
    }
}