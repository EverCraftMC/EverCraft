package io.github.evercraftmc.evercraft.spigot.games;

import org.bukkit.Location;

public abstract class TimedGame extends RoundedGame {
    protected Float timeLimit;

    protected Float currentTime;

    protected TimedGame(String name, Location location, Float minPlayers, Float maxPlayers, Float timeLimit) {
        super(name, location, minPlayers, maxPlayers);

        this.timeLimit = timeLimit;
    }

    public Float getTimeLimit() {
        return this.timeLimit;
    }

    public Float getCurrentTime() {
        return this.currentTime;
    }

    @Override
    public void tick() {
        if (this.currentTime < this.timeLimit) {
            this.currentTime += 1 / 20;
        }

        if (this.currentTime == this.timeLimit) {
            this.stop();

            this.currentTime++;
        }
    }
}