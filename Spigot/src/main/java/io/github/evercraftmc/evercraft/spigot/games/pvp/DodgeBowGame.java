package io.github.evercraftmc.evercraft.spigot.games.pvp;

import java.util.Arrays;
import org.bukkit.entity.Player;
import io.github.evercraftmc.evercraft.spigot.commands.kit.KitCommand;
import io.github.evercraftmc.evercraft.spigot.games.TeamedGame;

public class DodgeBowGame extends TeamedGame {
    protected String kitName;

    public DodgeBowGame(String name, String warpName, String kitName) {
        super(name, warpName, 1f, Float.MAX_VALUE);

        this.kitName = kitName;
    }

    public String getKitName() {
        return this.kitName;
    }

    @Override
    public void start() {
        super.start();

        for (Player player : this.players) {
            if (this.teams.get(player) == null) {
                this.teams.put(player, "runner");
            }

            if (this.teams.get(player).equals("bower")) {
                new KitCommand("kit", null, Arrays.asList(), null).run(player, new String[] { kitName, "true" });
            }
        }
    }

    @Override
    public void leave(Player player, LeaveReason leaveReason) {
        super.leave(player, leaveReason);

        if (this.started) {
            Integer runners = 0;
            for (Player player2 : this.players) {
                if (this.teams.get(player2).equalsIgnoreCase("runner")) {
                    runners++;
                }
            }

            if (runners == 0) {
                this.stop();
            }
        }
    }
}