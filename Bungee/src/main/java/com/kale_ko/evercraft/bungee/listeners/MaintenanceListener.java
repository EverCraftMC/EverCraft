package com.kale_ko.evercraft.bungee.listeners;

import com.kale_ko.evercraft.bungee.BungeePlugin;
import com.kale_ko.evercraft.bungee.Util;
import com.kale_ko.evercraft.bungee.commands.server.MaintenanceCommand;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class MaintenanceListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PostLoginEvent event) {
        if (MaintenanceCommand.underMaintenance) {
            if (!Util.hasPermission(event.getPlayer(), "evercraft.features.bypassMaintenance")) {
                event.getPlayer().disconnect(Util.stringToBungeeComponent(BungeePlugin.Instance.config.getString("messages.underMaintenance")));
            }
        }
    }
}