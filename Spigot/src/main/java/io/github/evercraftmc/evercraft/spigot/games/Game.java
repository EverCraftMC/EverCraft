package io.github.evercraftmc.evercraft.spigot.games;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.scheduler.BukkitTask;
import io.github.evercraftmc.evercraft.spigot.SpigotMain;

public abstract class Game implements Listener {
    protected String name;

    protected Location location;

    protected Float minPlayers;
    protected Float maxPlayers;

    protected List<Player> players = new ArrayList<Player>();

    protected BukkitTask tickTask;

    protected Game(String name, Location location, Float minPlayers, Float maxPlayers) {
        this.name = name;

        this.location = location;

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

    public Location getLocation() {
        return this.location;
    }

    public Float getMinPlayers() {
        return this.minPlayers;
    }

    public Float getMaxPlayers() {
        return this.maxPlayers;
    }

    public void join(Player player) {
        if (this.players.size() < this.maxPlayers) {
            this.players.add(player);

            player.teleport(this.location);
        } else {
            throw new RuntimeException("Game is full");
        }
    }

    public void leave(Player player) {
        this.players.remove(player);
    }

    public void tick() {}

    public Boolean isPlaying(Player player) {
        return this.players.contains(player);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        if (this.players.contains(event.getPlayer())) {
            this.leave(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerDied(PlayerDeathEvent event) {
        if (this.players.contains(event.getPlayer())) {
            this.leave(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.getCause() == TeleportCause.COMMAND || event.getCause() == TeleportCause.PLUGIN || event.getCause() == TeleportCause.SPECTATE) {
            if (this.players.contains(event.getPlayer())) {
                this.leave(event.getPlayer());
            }
        }
    }
}