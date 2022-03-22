package com.kale_ko.evercraft.spigot.listeners;

import com.kale_ko.evercraft.spigot.SpigotPlugin;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PvPListener implements Listener {
    @EventHandler
    public void onPlayerPvP(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player player1) {
            if (event.getDamager() instanceof Player player2) {
                if ((SpigotPlugin.Instance.players.getBoolean(player1.getName() + ".enablePvP") != null && SpigotPlugin.Instance.players.getBoolean(player1.getName() + ".enablePvP")) || (SpigotPlugin.Instance.players.getBoolean(player2.getName() + ".enablePvP") != null && SpigotPlugin.Instance.players.getBoolean(player2.getName() + ".enablePvP"))) {
                    event.setCancelled(true);
                }
            } else if (event.getDamager() instanceof Projectile player2projectile) {
                if (player2projectile.getShooter() instanceof Player player2) {
                    if ((SpigotPlugin.Instance.players.getBoolean(player1.getName() + ".enablePvP") != null && SpigotPlugin.Instance.players.getBoolean(player1.getName() + ".enablePvP")) || (SpigotPlugin.Instance.players.getBoolean(player2.getName() + ".enablePvP") != null && SpigotPlugin.Instance.players.getBoolean(player2.getName() + ".enablePvP"))) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}