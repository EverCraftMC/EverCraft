package io.github.evercraftmc.evercraft.spigot.games.pvp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitTask;
import io.github.evercraftmc.evercraft.spigot.SpigotMain;
import io.github.evercraftmc.evercraft.spigot.commands.kit.KitCommand;
import io.github.evercraftmc.evercraft.spigot.commands.warp.WarpCommand;
import io.github.evercraftmc.evercraft.spigot.games.TeamedGame;

public class HideAndSeekGame extends TeamedGame {
    protected String startWarpName;
    protected String seekerKitName;

    protected List<BukkitTask> teleportTasks = new ArrayList<BukkitTask>();

    public HideAndSeekGame(String name, String warpName, Integer countdownLength, String startWarpName, String seekerKitName) {
        super(name, warpName, 1, Integer.MAX_VALUE, countdownLength, Arrays.asList("hiders", "seekers"));

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
                new WarpCommand("kit", null, Arrays.asList(), null, true).run(player, new String[] { startWarpName });

                // TODO Hide nametags
            } else if (this.playerTeams.get(player).equalsIgnoreCase("seekers")) {
                this.teleportTasks.add(SpigotMain.getInstance().getServer().getScheduler().runTaskLater(SpigotMain.getInstance(), () -> {
                    if (this.players.contains(player)) {
                        new WarpCommand("kit", null, Arrays.asList(), null, true).run(player, new String[] { startWarpName });

                        new KitCommand("kit", null, Arrays.asList(), null, true).run(player, new String[] { seekerKitName });
                    }
                }, 60 * 20));
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
            } else {
                if (this.countdownTask != null) {
                    this.countdownTask.cancel();
                    this.countdownTask = null;
                    this.countdown = -1;
                }
            }
        }
    }

    @Override
    public void stop() {
        super.stop();

        this.teleportTasks.clear();
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player player && event.getDamager() instanceof Player player2) {
            if (this.playerTeams.containsKey(player2) && this.getTeam(player2).equalsIgnoreCase("seekers")) {
                event.setDamage(Integer.MAX_VALUE);
            }
        }
    }
}