package io.github.evercraftmc.evercraft.spigot.games;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import io.github.evercraftmc.evercraft.spigot.SpigotMain;
import io.github.evercraftmc.evercraft.spigot.util.formatting.ComponentFormatter;

public abstract class RoundedGame extends Game {
    protected Boolean started = false;

    protected RoundedGame(String name, String warpName, Float minPlayers, Float maxPlayers) {
        super(name, warpName, minPlayers, maxPlayers);

        this.tickTask.cancel();
    }

    @Override
    public void join(Player player) {
        if (!this.started) {
            super.join(player);
        } else {
            player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("games.started"))));
        }
    }

    @Override
    public void leave(Player player, LeaveReason leaveReason) {
        super.leave(player, leaveReason);

        if (this.started && this.players.size() < this.minPlayers) {
            this.stop();
        }
    }

    public void start() {
        if (!this.started) {
            if (this.players.size() < this.minPlayers) {
                throw new RuntimeException("Not enough players");
            }

            this.started = true;

            for (Player player : this.players) {
                player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("games.start"))));
            }

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
            this.started = false;

            for (Player player : this.players) {
                player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("games.stop"))));

                this.leave(player, LeaveReason.GAMEOVER);
            }

            this.tickTask.cancel();
        } else {
            throw new RuntimeException("Game is not started");
        }
    }
}