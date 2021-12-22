package com.kale_ko.kalesutilities.listeners;

import com.kale_ko.kalesutilities.Main;
import java.util.List;
import java.util.UUID;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.MetadataValue;

public class PlayerMoveListener implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        List<MetadataValue> statusEntityUUID = event.getPlayer().getMetadata("statusEntityUUID");

        for (MetadataValue metadata : statusEntityUUID) {
            if (metadata.getOwningPlugin() == Main.Instance) {
                Main.Instance.getServer().getEntity(UUID.fromString(metadata.asString())).remove();
            }
        }
    }
}