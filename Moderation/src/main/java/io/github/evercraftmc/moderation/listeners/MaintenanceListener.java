package io.github.evercraftmc.moderation.listeners;

import io.github.evercraftmc.core.api.events.ECHandler;
import io.github.evercraftmc.core.api.events.ECHandlerOrder;
import io.github.evercraftmc.core.api.events.ECListener;
import io.github.evercraftmc.core.api.events.player.PlayerChatEvent;
import io.github.evercraftmc.core.impl.util.ECTextFormatter;
import io.github.evercraftmc.moderation.ModerationModule;

public class MaintenanceListener implements ECListener {
    protected final ModerationModule parent;

    public MaintenanceListener(ModerationModule parent) {
        this.parent = parent;
    }

    @ECHandler(order=ECHandlerOrder.BEFORE)
    public void onPlayerChat(PlayerChatEvent event) {
        if (parent.getPlugin().getPlayerData().maintenance && !event.getPlayer().hasPermission("evercraft.moderation.commands.maintenance.bypass")) {
            event.setCancelled(true);
            event.setCancelReason(ECTextFormatter.translateColors("&cThe server is currently in maintenance mode\nCheck back later!"));
        }
    }
}