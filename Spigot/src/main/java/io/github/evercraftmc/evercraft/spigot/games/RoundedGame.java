package io.github.evercraftmc.evercraft.spigot.games;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import io.github.evercraftmc.evercraft.spigot.SpigotMain;
import io.github.evercraftmc.evercraft.spigot.util.formatting.ComponentFormatter;

public abstract class RoundedGame extends Game {
    protected Integer countdownLength;

    protected Boolean started = false;
    protected Integer countdown = -1;

    protected BukkitTask countdownTask;

    protected RoundedGame(String name, String warpName, Integer minPlayers, Integer maxPlayers, Integer countdownLength) {
        super(name, warpName, minPlayers, maxPlayers);

        this.countdownLength = countdownLength;

        this.tickTask.cancel();
    }

    @Override
    public void join(Player player) {
        if (!this.started) {
            super.join(player);
        } else {
            player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().games.started)));
        }
    }

    @Override
    public void leave(Player player, LeaveReason leaveReason) {
        super.leave(player, leaveReason);

        if (this.players.size() < this.minPlayers) {
            if (this.started) {
                this.stop();
            } else if (this.countdownTask != null) {
                this.countdownTask.cancel();
                this.countdownTask = null;
                this.countdown = -1;
            }
        }
    }

    public void start() {
        this.countdownTask = Bukkit.getScheduler().runTaskTimer(SpigotMain.getInstance(), () -> {
            if (countdown >= 0) {
                countdown--;

                for (Player player : players) {
                    player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().games.countdown.replace("{time}", (countdown + 1) + ""))));
                }
            }

            if (countdown == 0) {
                countdownTask.cancel();
                countdownTask = null;
                countdown = -1;

                countdownTask = Bukkit.getScheduler().runTaskLater(SpigotMain.getInstance(), () -> {
                    countdownTask = null;

                    startNoCountdown();
                }, 20);
            }
        }, 0, 20);
        this.countdown = this.countdownLength;
    }

    public void startNoCountdown() {
        if (!this.started) {
            if (this.players.size() < this.minPlayers) {
                throw new RuntimeException("Not enough players");
            }

            this.started = true;

            for (Player player : this.players) {
                player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().games.start)));
            }

            this.tickTask = Bukkit.getScheduler().runTaskTimer(SpigotMain.getInstance(), () -> {
                this.tick();
            }, 1, 1);
        } else {
            throw new RuntimeException("Game is already started");
        }
    }

    public void stop() {
        if (this.started) {
            this.started = false;

            // TODO Winners

            for (Player player : new ArrayList<Player>(this.players)) {
                player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().games.stop)));

                this.leave(player, LeaveReason.GAMEOVER);
            }

            this.tickTask.cancel();
        } else {
            throw new RuntimeException("Game is not started");
        }
    }
}