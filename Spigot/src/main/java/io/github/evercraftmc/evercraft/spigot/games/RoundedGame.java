package io.github.evercraftmc.evercraft.spigot.games;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import io.github.evercraftmc.evercraft.spigot.SpigotMain;

public abstract class RoundedGame extends Game {
    protected RoundedGame(String name, String warpName, Float minPlayers, Float maxPlayers) {
        super(name, warpName, minPlayers, maxPlayers);

        this.tickTask.cancel();
    }

    @Override
    public void leave(Player player, LeaveReason leaveReason) {
        super.leave(player, leaveReason);

        if (this.players.size() < this.minPlayers) {
            this.stop();
        }
    }

    public void start() {
        if (this.players.size() < this.minPlayers) {
            throw new RuntimeException("Not enough players");
        }

        this.tickTask = Bukkit.getScheduler().runTaskTimer(SpigotMain.getInstance(), new Runnable() {
            public void run() {
                tick();
            }
        }, 1, 1);
    }

    public void stop() {
        for (Player player : this.players) {
            this.leave(player, LeaveReason.COMMAND);
        }

        this.tickTask.cancel();
    }
}