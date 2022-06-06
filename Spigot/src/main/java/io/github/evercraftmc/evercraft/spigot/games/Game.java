package io.github.evercraftmc.evercraft.spigot.games;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.scheduler.BukkitTask;
import io.github.evercraftmc.evercraft.spigot.SpigotMain;
import io.github.evercraftmc.evercraft.spigot.commands.warp.SpawnCommand;
import io.github.evercraftmc.evercraft.spigot.commands.warp.WarpCommand;

public abstract class Game implements Listener {
    public enum LeaveReason {
        COMMAND, DEATH, TELEPORT, DISCONNECTED
    }

    protected String name;

    protected String warpName;

    protected Float minPlayers;
    protected Float maxPlayers;

    protected List<Player> players = new ArrayList<Player>();

    protected BukkitTask tickTask;

    protected Game(String name, String warpName, Float minPlayers, Float maxPlayers) {
        this.name = name;

        this.warpName = warpName;

        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;

        this.tickTask = Bukkit.getScheduler().runTaskTimer(SpigotMain.getInstance(), new Runnable() {
            public void run() {
                tick();
            }
        }, 1, 1);

        SpigotMain.getInstance().getServer().getPluginManager().registerEvents(this, SpigotMain.getInstance());
    }

    public String getName() {
        return this.name;
    }

    public String getWarpName() {
        return this.warpName;
    }

    public Float getMinPlayers() {
        return this.minPlayers;
    }

    public Float getMaxPlayers() {
        return this.maxPlayers;
    }

    public void join(Player player) {
        if (this.players.size() < this.maxPlayers) {
            new WarpCommand("warp", null, Arrays.asList(), null).run(player, new String[] { warpName, "true" });

            this.players.add(player);
        } else {
            throw new RuntimeException("Game is full");
        }
    }

    public void leave(Player player, LeaveReason leaveReason) {
        this.players.remove(player);

        if (leaveReason == LeaveReason.COMMAND) {
            new SpawnCommand("spawn", null, Arrays.asList(), null).run(player, new String[] { "true" });
        }
    }

    public void tick() {}

    public Boolean isPlaying(Player player) {
        return this.players.contains(player);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        if (this.players.contains(event.getPlayer())) {
            this.leave(event.getPlayer(), LeaveReason.DISCONNECTED);
        }
    }

    @EventHandler
    public void onPlayerDied(PlayerDeathEvent event) {
        if (this.players.contains(event.getPlayer())) {
            this.leave(event.getPlayer(), LeaveReason.DEATH);
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.getCause() == TeleportCause.COMMAND || event.getCause() == TeleportCause.SPECTATE) {
            if (this.players.contains(event.getPlayer())) {
                this.leave(event.getPlayer(), LeaveReason.TELEPORT);
            }
        }
    }
}