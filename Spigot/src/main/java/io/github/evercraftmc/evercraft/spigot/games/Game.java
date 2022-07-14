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
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import io.github.evercraftmc.evercraft.spigot.SpigotMain;
import io.github.evercraftmc.evercraft.spigot.commands.warp.SpawnCommand;
import io.github.evercraftmc.evercraft.spigot.commands.warp.WarpCommand;
import io.github.evercraftmc.evercraft.spigot.util.formatting.ComponentFormatter;

public abstract class Game implements Listener {
    public enum LeaveReason {
        GAMEOVER, COMMAND, DEATH, TELEPORT, DISCONNECTED
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
        if (!this.players.contains(player)) {
            if (this.players.size() < this.maxPlayers) {
                new WarpCommand("warp", null, Arrays.asList(), null).run(player, new String[] { warpName, "true" });

                this.players.add(player);

                player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("games.joined").replace("{game}", this.name).replace("{players}", this.players.size() + "").replace("{max}", (this.maxPlayers == Float.MAX_VALUE ? "Infinite" : this.maxPlayers + "")))));

                for (Player player2 : this.players) {
                    if (player2 != player) {
                        player2.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("games.join").replace("{player}", ComponentFormatter.componentToString(player.displayName())).replace("{players}", this.players.size() + "").replace("{max}", (this.maxPlayers == Float.MAX_VALUE ? "Infinite" : this.maxPlayers + "")))));
                    }
                }
            } else {
                player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("games.full"))));
            }
        } else {
            throw new RuntimeException("Player is already in game");
        }
    }

    public void leave(Player player, LeaveReason leaveReason) {
        if (this.players.contains(player)) {
            this.players.remove(player);

            if (leaveReason != LeaveReason.GAMEOVER) {
                if (leaveReason != LeaveReason.DISCONNECTED) {
                    player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("games.left").replace("{game}", this.name).replace("{players}", this.players.size() + "").replace("{max}", (this.maxPlayers == Float.MAX_VALUE ? "Infinite" : this.maxPlayers + "")))));
                }

                for (Player player2 : this.players) {
                    if (player2 != player) {
                        player2.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("games.leave").replace("{player}", ComponentFormatter.componentToString(player.displayName())).replace("{players}", this.players.size() + "").replace("{max}", (this.maxPlayers == Float.MAX_VALUE ? "Infinite" : this.maxPlayers + "")))));
                    }
                }
            }

            if (leaveReason == LeaveReason.COMMAND || leaveReason == LeaveReason.GAMEOVER) {
                new SpawnCommand("spawn", null, Arrays.asList(), null).run(player, new String[] { "true" });
            }
        } else {
            throw new RuntimeException("Player is not in game");
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