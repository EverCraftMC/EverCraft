package io.github.evercraftmc.evercraft.spigot.games;

import java.util.List;

public abstract class TeamTimedGame extends TeamedGame {
    protected Float timeLimit;

    protected Float currentTime = 0f;

    protected TeamTimedGame(String name, String warpName, Integer minPlayers, Integer maxPlayers, Integer countdownLength, List<String> teamsList, Float timeLimit) {
        super(name, warpName, minPlayers, maxPlayers, countdownLength, teamsList);

        this.timeLimit = timeLimit;
    }

    public Float getTimeLimit() {
        return this.timeLimit;
    }

    public Float getCurrentTime() {
        return this.currentTime;
    }

    @Override
    public void start() {
        super.start();

        this.currentTime = 0f;
    }

    @Override
    public void stop() {
        super.stop();

        this.currentTime = -1f;
    }

    @Override
    public void tick() {
        if (this.currentTime != -1f) {
            if (this.currentTime < this.timeLimit) {
                this.currentTime += 0.05f;
            }

            if (this.currentTime >= this.timeLimit) {
                this.stop();
            }
        }
    }
}