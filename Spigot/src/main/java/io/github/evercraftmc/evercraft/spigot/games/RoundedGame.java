package io.github.evercraftmc.evercraft.spigot.games;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import io.github.evercraftmc.evercraft.spigot.SpigotMain;

public abstract class RoundedGame extends Game {
    protected RoundedGame(String name, Location location, Float minPlayers, Float maxPlayers) {
        super(name, location, minPlayers, maxPlayers);

        this.tickTask.cancel();
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
        this.tickTask.cancel();
    }
}