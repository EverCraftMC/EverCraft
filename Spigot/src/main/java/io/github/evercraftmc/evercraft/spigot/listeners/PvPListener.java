package io.github.evercraftmc.evercraft.spigot.listeners;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import io.github.evercraftmc.evercraft.spigot.SpigotMain;

public class PvPListener extends SpigotListener {
    @EventHandler
    public void onPlayerPvP(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (event.getDamager() instanceof Player player2) {
                if (SpigotMain.getInstance().getPluginData().getBoolean("players." + player.getUniqueId() + ".passive." + SpigotMain.getInstance().getServerName()) || SpigotMain.getInstance().getPluginData().getBoolean("players." + player2.getUniqueId() + ".passive." + SpigotMain.getInstance().getServerName())) {
                    event.setCancelled(true);
                }
            } else if (event.getDamager() instanceof Projectile projectile && projectile.getShooter() instanceof Player player2) {
                if (SpigotMain.getInstance().getPluginData().getBoolean("players." + player.getUniqueId() + ".passive." + SpigotMain.getInstance().getServerName()) || SpigotMain.getInstance().getPluginData().getBoolean("players." + player2.getUniqueId() + ".passive." + SpigotMain.getInstance().getServerName())) {
                    event.setCancelled(true);
                }
            } else if (event.getDamager() instanceof Tameable tameable && (tameable.getOwner() != null && tameable.getOwner() instanceof Player player2)) {
                if (SpigotMain.getInstance().getPluginData().getBoolean("players." + player.getUniqueId() + ".passive." + SpigotMain.getInstance().getServerName()) || SpigotMain.getInstance().getPluginData().getBoolean("players." + player2.getUniqueId() + ".passive." + SpigotMain.getInstance().getServerName())) {
                    event.setCancelled(true);
                }
            }
        }
    }
}