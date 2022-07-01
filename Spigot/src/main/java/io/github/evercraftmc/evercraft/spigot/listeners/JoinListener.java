package io.github.evercraftmc.evercraft.spigot.listeners;

import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import io.github.evercraftmc.evercraft.spigot.SpigotMain;
import io.github.evercraftmc.evercraft.spigot.util.formatting.ComponentFormatter;
import io.github.evercraftmc.evercraft.spigot.util.types.SerializableLocation;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;
import net.kyori.adventure.text.Component;
import net.luckperms.api.LuckPermsProvider;

public class JoinListener extends SpigotListener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("GetServer");
        event.getPlayer().sendPluginMessage(SpigotMain.getInstance(), "BungeeCord", out.toByteArray());

        event.joinMessage(Component.empty());

        event.getPlayer().displayName(ComponentFormatter.stringToComponent(TextFormatter.translateColors(LuckPermsProvider.get().getUserManager().getUser(event.getPlayer().getUniqueId()).getCachedData().getMetaData().getPrefix() + SpigotMain.getInstance().getData().getString("players." + event.getPlayer().getUniqueId() + ".nickname"))));
        event.getPlayer().playerListName(event.getPlayer().displayName());
    }

    @EventHandler
    public void onPlayerSpawn(PlayerSpawnLocationEvent event) {
        if (SpigotMain.getInstance().getPluginConfig().getBoolean("warp.overidespawn")) {
            event.setSpawnLocation(SpigotMain.getInstance().getWarps().getSerializable("spawn", SerializableLocation.class).toBukkitLocation());

            if (SpigotMain.getInstance().getPluginConfig().getBoolean("warp.clearonwarp")) {
                event.getPlayer().getInventory().clear();
                event.getPlayer().getActivePotionEffects().clear();
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (SpigotMain.getInstance().getPluginConfig().getBoolean("warp.overidespawn")) {
            event.setRespawnLocation(SpigotMain.getInstance().getWarps().getSerializable("spawn", SerializableLocation.class).toBukkitLocation());

            if (SpigotMain.getInstance().getPluginConfig().getBoolean("warp.clearonwarp")) {
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