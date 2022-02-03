package com.kale_ko.kalesutilities.spigot.listeners;

import com.kale_ko.kalesutilities.spigot.SpigotPlugin;
import com.kale_ko.kalesutilities.spigot.Util;
import java.util.UUID;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (Math.abs(event.getTo().getX() - event.getFrom().getX()) > 0 || Math.abs(event.getTo().getY() - event.getFrom().getY()) > 0 || Math.abs(event.getTo().getZ() - event.getFrom().getZ()) > 0) {
            if (Util.hasMetadata(event.getPlayer(), "statusEntityUUID")) {
                SpigotPlugin.Instance.getServer().getEntity(UUID.fromString(Util.getMetadata(event.getPlayer(), "statusEntityUUID").asString())).remove();
                Util.removeMetadata(event.getPlayer(), "statusEntityUUID");
            }
        }
    }
}