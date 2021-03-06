package io.github.evercraftmc.evercraft.spigot.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import io.github.evercraftmc.evercraft.spigot.SpigotMain;
import io.github.evercraftmc.evercraft.spigot.util.formatting.ComponentFormatter;
import io.github.evercraftmc.evercraft.spigot.util.player.SpigotPlayerResolver;
import net.kyori.adventure.text.Component;

public class JoinListener extends SpigotListener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("GetServer");
        event.getPlayer().sendPluginMessage(SpigotMain.getInstance(), "BungeeCord", out.toByteArray());

        event.joinMessage(Component.empty());

        event.getPlayer().displayName(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotPlayerResolver.getDisplayName(SpigotMain.getInstance().getPluginData(), event.getPlayer().getUniqueId()))));
        event.getPlayer().playerListName(event.getPlayer().displayName());
    }

    @EventHandler
    public void onPlayerSpawn(PlayerSpawnLocationEvent event) {
        if (SpigotMain.getInstance().getPluginConfig().getParsed().warp.overrideSpawn) {
            event.setSpawnLocation(SpigotMain.getInstance().getWarps().getParsed().warps.get("spawn").toBukkitLocation());

            if (SpigotMain.getInstance().getPluginConfig().getParsed().warp.clearOnWarp) {
                event.getPlayer().getInventory().clear();
                event.getPlayer().getActivePotionEffects().clear();
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (SpigotMain.getInstance().getPluginConfig().getParsed().warp.overrideSpawn) {
            event.setRespawnLocation(SpigotMain.getInstance().getWarps().getParsed().warps.get("spawn").toBukkitLocation());

            if (SpigotMain.getInstance().getPluginConfig().getParsed().warp.clearOnWarp) {
                event.getPlayer().getInventory().clear();
                event.getPlayer().getActivePotionEffects().clear();
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.quitMessage(Component.empty());
    }
}