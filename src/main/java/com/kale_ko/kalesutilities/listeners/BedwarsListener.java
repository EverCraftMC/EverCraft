package com.kale_ko.kalesutilities.listeners;

import com.kale_ko.kalesutilities.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

@SuppressWarnings("deprecation")
public class BedwarsListener implements Listener {
    @EventHandler
    public void onChatMessage(PlayerChatEvent event) {
        if (event.getMessage().contains("bed") || event.getMessage().contains("beb") || event.getMessage().contains("wars")) {
            Main.Instance.getServer().dispatchCommand(event.getPlayer(), "kick " + event.getPlayer().getName() + " Bedwars");
        }
    }
}