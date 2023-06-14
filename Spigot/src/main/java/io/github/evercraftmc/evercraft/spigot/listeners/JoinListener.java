package io.github.evercraftmc.evercraft.spigot.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent.Cause;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import io.github.evercraftmc.evercraft.spigot.SpigotChests;
import io.github.evercraftmc.evercraft.spigot.SpigotMain;
import io.github.evercraftmc.evercraft.spigot.util.formatting.ComponentFormatter;
import net.kyori.adventure.text.Component;

public class JoinListener extends SpigotListener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("GetServer");
        event.getPlayer().sendPluginMessage(SpigotMain.getInstance(), "BungeeCord", out.toByteArray());

        if (SpigotMain.getInstance().getChests() != null && !SpigotMain.getInstance().getChests().get().players.containsKey(event.getPlayer().getUniqueId().toString())) {
            SpigotMain.getInstance().getChests().get().players.put(event.getPlayer().getUniqueId().toString(), new SpigotChests.Player());
        }

        event.joinMessage(Component.empty());
    }

    @EventHandler
    public void onPlayerSpawn(PlayerSpawnLocationEvent event) {
        if (SpigotMain.getInstance().getPluginConfig().get().warp.overrideSpawn) {
            event.setSpawnLocation(SpigotMain.getInstance().getWarps().get().warps.get("spawn").toBukkitLocation());

            if (SpigotMain.getInstance().getPluginConfig().get().warp.clearOnWarp) {
                event.getPlayer().getInventory().clear();
                event.getPlayer().getActivePotionEffects().clear();
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (SpigotMain.getInstance().getPluginConfig().get().warp.overrideSpawn) {
            event.setRespawnLocation(SpigotMain.getInstance().getWarps().get().warps.get("spawn").toBukkitLocation());

            if (SpigotMain.getInstance().getPluginConfig().get().warp.clearOnWarp) {
                event.getPlayer().getInventory().clear();
                event.getPlayer().getActivePotionEffects().clear();
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.quitMessage(Component.empty());
    }

    @EventHandler
    public void onServerStop(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().startsWith("/stop") || event.getMessage().startsWith("/restart")) {
            event.setCancelled(true);

            for (Player player : SpigotMain.getInstance().getServer().getOnlinePlayers()) {
                player.kick(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().restarting)), Cause.RESTART_COMMAND);
            }

            SpigotMain.getInstance().getServer().shutdown();
        }
    }

    @EventHandler
    public void onServerStop(ServerCommandEvent event) {
        if (event.getCommand().startsWith("stop") || event.getCommand().startsWith("restart")) {
            event.setCancelled(true);

            for (Player player : SpigotMain.getInstance().getServer().getOnlinePlayers()) {
                player.kick(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().restarting)), Cause.RESTART_COMMAND);
            }

            SpigotMain.getInstance().getServer().shutdown();
        }
    }
}