package io.github.evercraftmc.evercraft.spigot.games;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import io.github.evercraftmc.evercraft.spigot.SpigotMain;

public abstract class RoundedGame extends Game {
    protected Boolean started = false;

    protected RoundedGame(String name, String warpName, Float minPlayers, Float maxPlayers) {
        super(name, warpName, minPlayers, maxPlayers);

        this.tickTask.cancel();
    }

    @Override
    public void join(Player player) {
        super.join(player);

        if (this.started) {
            throw new RuntimeException("The game has already started");
        }
    }

    @Override
    public void leave(Player player, LeaveReason leaveReason) {
        super.leave(player, leaveReason);

        if (this.players.size() < this.minPlayers) {
            this.stop();
        }
    }

    public void start() {
        if (!this.started) {
            if (this.players.size() < this.minPlayers) {
                throw new RuntimeException("Not enough players");
            }

            this.started = true;

            this.tickTask = Bukkit.getScheduler().runTaskTimer(SpigotMain.getInstance(), new Runnable() {
                public void run() {
                    tick();
                }
            }, 1, 1);
        } else {
            throw new RuntimeException("Game is already started");
        }
    }

    public void stop() {
        if (this.started) {
            for (Player player : this.players) {
                this.leave(player, LeaveReason.COMMAND);
            }

            this.started = false;

            this.tickTask.cancel();
        } else {
            throw new RuntimeException("Game is not started");
        }
    }
}