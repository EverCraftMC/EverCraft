package io.github.evercraftmc.evercraft.spigot.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

public class SitListener extends SpigotListener {
    @EventHandler
    public void onPlayerDismount(EntityDismountEvent event) {
        if (event.getDismounted() instanceof Pig && event.getEntity() instanceof Player player) {
            String tag = null;
            for (String tag2 : event.getDismounted().getScoreboardTags()) {
                if (tag2.startsWith("playerSeat:")) {
                    tag = tag2;
                }
            }

            if (tag != null && player.getUniqueId().toString().equals(tag.split(":")[1])) {
                event.getDismounted().eject();
                event.getDismounted().remove();
            }

            player.teleport(new Location(player.getWorld(), Double.parseDouble(tag.split(":")[2].split(",")[0]), Double.parseDouble(tag.split(":")[2].split(",")[1]), Double.parseDouble(tag.split(":")[2].split(",")[2]), player.getLocation().getYaw(), player.getLocation().getPitch()));
            event.getEntity().teleport(new Location(player.getWorld(), Double.parseDouble(tag.split(":")[2].split(",")[0]), Double.parseDouble(tag.split(":")[2].split(",")[1]), Double.parseDouble(tag.split(":")[2].split(",")[2])));
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        System.out.println(event.getPlayer());

        for (Pig entity : event.getPlayer().getWorld().getEntitiesByClass(Pig.class)) {
            String tag = null;
            for (String tag2 : entity.getScoreboardTags()) {
                if (tag2.startsWith("playerSeat:")) {
                    tag = tag2;
                }
            }

            if (tag != null && event.getPlayer().getUniqueId().toString().equals(tag.split(":")[1])) {
                entity.eject();
                entity.remove();
            }
        }

        if (event.getPlayer().getVehicle() instanceof Pig) {
            String tag = null;
            for (String tag2 : event.getPlayer().getVehicle().getScoreboardTags()) {
                if (tag2.startsWith("playerSeat:")) {
                    tag = tag2;
                }
            }

            if (tag != null && event.getPlayer().getUniqueId().toString().equals(tag.split(":")[1])) {
                event.getPlayer().getVehicle().eject();
                event.getPlayer().getVehicle().remove();
            }
        }
    }
}