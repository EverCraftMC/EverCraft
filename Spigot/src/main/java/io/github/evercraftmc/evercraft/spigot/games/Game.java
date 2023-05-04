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
import io.github.evercraftmc.evercraft.spigot.commands.warp.WarpCommand;
import io.github.evercraftmc.evercraft.spigot.util.formatting.ComponentFormatter;

public abstract class Game implements Listener {
    public enum LeaveReason {
        GAMEOVER, COMMAND, DEATH, TELEPORT, DISCONNECT
    }

    protected String name;

    protected String warpName;

    protected Integer minPlayers;
    protected Integer maxPlayers;

    protected List<Player> players = new ArrayList<Player>();

    protected BukkitTask tickTask;

    protected Game(String name, String warpName, Integer minPlayers, Integer maxPlayers) {
        this.name = name;

        this.warpName = warpName;

        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;

        SpigotMain.getInstance().getServer().getPluginManager().registerEvents(this, SpigotMain.getInstance());

        this.tickTask = Bukkit.getScheduler().runTaskTimer(SpigotMain.getInstance(), () -> {
            this.tick();
        }, 1, 1);
    }

    public String getName() {
        return this.name;
    }

    public String getWarpName() {
        return this.warpName;
    }

    public Integer getMinPlayers() {
        return this.minPlayers;
    }

    public Integer getMaxPlayers() {
        return this.maxPlayers;
    }

    public List<Player> getPlayers() {
        return new ArrayList<Player>(this.players);
    }

    public Boolean isPlaying(Player player) {
        return this.players.contains(player);
    }

    public void join(Player player) {
        if (!this.players.contains(player)) {
            if (this.players.size() < this.maxPlayers) {
                new WarpCommand("kit", null, Arrays.asList(), null, true).run(player, new String[] { warpName });

                this.players.add(player);

                if (this.maxPlayers != Integer.MAX_VALUE) {
                    player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().games.joined.replace("{game}", this.name).replace("{players}", this.players.size() + "").replace("{max}", this.maxPlayers + ""))));
                } else {
                    player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().games.joinedNoMax.replace("{game}", this.name).replace("{players}", this.players.size() + ""))));
                }

                for (Player player2 : this.players) {
                    if (player2 != player) {
                        if (this.maxPlayers != Integer.MAX_VALUE) {
                            player2.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().games.join.replace("{player}", ComponentFormatter.componentToString(player.displayName())).replace("{players}", this.players.size() + "").replace("{max}", this.maxPlayers + ""))));
                        } else {
                            player2.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().games.joinNoMax.replace("{player}", ComponentFormatter.componentToString(player.displayName())).replace("{players}", this.players.size() + ""))));
                        }
                    }
                }
            } else {
                player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().games.full)));
            }
        } else {
            throw new RuntimeException("Player is already in game");
        }
    }

    public void leave(Player player, LeaveReason leaveReason) {
        if (this.players.contains(player)) {
            this.players.remove(player);

            if (leaveReason != LeaveReason.GAMEOVER && leaveReason != LeaveReason.DISCONNECT && leaveReason != LeaveReason.DEATH) {
                player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().games.left.replace("{game}", this.name).replace("{players}", this.players.size() + "").replace("{max}", this.maxPlayers + ""))));

                for (Player player2 : this.players) {
                    if (this.maxPlayers != Integer.MAX_VALUE) {
                        player2.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().games.leave.replace("{player}", ComponentFormatter.componentToString(player.displayName())).replace("{players}", this.players.size() + "").replace("{max}", this.maxPlayers + ""))));
                    } else {
                        player2.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().games.leaveNoMax.replace("{player}", ComponentFormatter.componentToString(player.displayName())).replace("{players}", this.players.size() + ""))));
                    }
                }
            }

            if (leaveReason == LeaveReason.COMMAND || leaveReason == LeaveReason.GAMEOVER) {
                new WarpCommand("kit", null, Arrays.asList(), null, true).run(player, new String[] { "spawn" });
            }
        } else {
            throw new RuntimeException("Player is not in game");
        }
    }

    public void tick() {}

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        if (this.players.contains(event.getPlayer())) {
            this.leave(event.getPlayer(), LeaveReason.DISCONNECT);
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