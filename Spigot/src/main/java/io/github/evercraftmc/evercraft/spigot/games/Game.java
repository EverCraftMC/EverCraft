package io.github.evercraftmc.evercraft.spigot.games;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitTask;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import io.github.evercraftmc.evercraft.spigot.SpigotMain;
import io.github.evercraftmc.evercraft.spigot.util.formatting.ComponentFormatter;

public abstract class Game implements Listener {
    public enum LeaveReason {
        GAMEOVER, COMMAND, DEATH, TELEPORT, DISCONNECT
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

        SpigotMain.getInstance().getServer().getPluginManager().registerEvents(this, SpigotMain.getInstance());

        this.tickTask = Bukkit.getScheduler().runTaskTimer(SpigotMain.getInstance(), new Runnable() {
            public void run() {
                tick();
            }
        }, 1, 1);
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

    public List<Player> getPlayers() {
        return new ArrayList<Player>(this.players);
    }

    public Boolean isPlaying(Player player) {
        return this.players.contains(player);
    }

    public void join(Player player) {
        if (!this.players.contains(player)) {
            if (this.players.size() < this.maxPlayers) {
                player.getInventory().clear();
                for (PotionEffect effect : player.getActivePotionEffects()) {
                    player.removePotionEffect(effect.getType());
                }

                player.teleport(SpigotMain.getInstance().getWarps().getParsed().warps.get(warpName).toBukkitLocation());

                this.players.add(player);

                if (this.maxPlayers != Float.MAX_VALUE) {
                    player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getParsed().games.joined.replace("{game}", this.name).replace("{players}", this.players.size() + "").replace("{max}", this.maxPlayers + ""))));
                } else {
                    player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getParsed().games.joinedNoMax.replace("{game}", this.name).replace("{players}", this.players.size() + ""))));
                }

                for (Player player2 : this.players) {
                    if (player2 != player) {
                        if (this.maxPlayers != Float.MAX_VALUE) {
                            player2.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getParsed().games.join.replace("{player}", ComponentFormatter.componentToString(player.displayName())).replace("{players}", this.players.size() + "").replace("{max}", this.maxPlayers + ""))));
                        } else {
                            player2.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getParsed().games.joinNoMax.replace("{player}", ComponentFormatter.componentToString(player.displayName())).replace("{players}", this.players.size() + ""))));
                        }
                    }
                }
            } else {
                player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getParsed().games.full)));
            }
        } else {
            throw new RuntimeException("Player is already in game");
        }
    }

    public void leave(Player player, LeaveReason leaveReason) {
        if (this.players.contains(player)) {
            this.players.remove(player);

            if (leaveReason != LeaveReason.GAMEOVER) {
                if (leaveReason != LeaveReason.DISCONNECT) {
                    player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getParsed().games.left.replace("{game}", this.name).replace("{players}", this.players.size() + "").replace("{max}", this.maxPlayers + ""))));
                }

                for (Player player2 : this.players) {
                    if (this.maxPlayers != Float.MAX_VALUE) {
                        player2.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getParsed().games.leave.replace("{player}", ComponentFormatter.componentToString(player.displayName())).replace("{players}", this.players.size() + "").replace("{max}", this.maxPlayers + ""))));
                    } else {
                        player2.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getParsed().games.leaveNoMax.replace("{player}", ComponentFormatter.componentToString(player.displayName())).replace("{players}", this.players.size() + ""))));
                    }
                }
            }

            if (leaveReason == LeaveReason.COMMAND || leaveReason == LeaveReason.GAMEOVER) {
                player.teleport(SpigotMain.getInstance().getWarps().getParsed().warps.get("spawn").toBukkitLocation());
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