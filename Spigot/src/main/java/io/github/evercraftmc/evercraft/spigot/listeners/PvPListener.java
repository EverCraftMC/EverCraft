package io.github.evercraftmc.evercraft.spigot.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import io.github.evercraftmc.evercraft.spigot.SpigotMain;

public class PvPListener extends SpigotListener {
    @EventHandler
    public void onPlayerPvP(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player player && event.getDamager() instanceof Player player2) {
            if (SpigotMain.getInstance().getData().getBoolean("players." + player.getUniqueId() + ".passive." + SpigotMain.getInstance().getServerName()) || SpigotMain.getInstance().getData().getBoolean("players." + player2.getUniqueId() + ".passive." + SpigotMain.getInstance().getServerName())) {
                event.setCancelled(true);
            }
        }
    }
}
