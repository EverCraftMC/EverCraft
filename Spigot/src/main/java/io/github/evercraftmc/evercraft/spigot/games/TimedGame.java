package io.github.evercraftmc.evercraft.spigot.games;

public abstract class TimedGame extends RoundedGame {
    protected Float timeLimit;

    protected Float currentTime = 0f;

    protected TimedGame(String name, String warpName, Float minPlayers, Float maxPlayers, Integer countdownLength, Float timeLimit) {
        super(name, warpName, minPlayers, maxPlayers, countdownLength);

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