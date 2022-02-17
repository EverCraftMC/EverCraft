package com.kale_ko.kalesutilities.spigot.listeners;

import com.kale_ko.kalesutilities.spigot.Util;
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
                if ((Util.hasMetadata(player1, "enablePvP") && !Util.getMetadata(player1, "enablePvP").asBoolean()) || (Util.hasMetadata(player2, "enablePvP") && !Util.getMetadata(player2, "enablePvP").asBoolean())) {
                    event.setCancelled(true);
                }
            } else if (event.getDamager() instanceof Projectile player2projectile) {
                if (player2projectile.getShooter() instanceof Player player2) {
                    if ((Util.hasMetadata(player1, "enablePvP") && !Util.getMetadata(player1, "enablePvP").asBoolean()) || (Util.hasMetadata(player2, "enablePvP") && !Util.getMetadata(player2, "enablePvP").asBoolean())) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}