package com.kale_ko.evercraft.spigot.listeners;

import com.kale_ko.evercraft.shared.util.formatting.TextFormatter;
import com.kale_ko.evercraft.spigot.SpigotMain;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import net.luckperms.api.LuckPermsProvider;

public class JoinListener extends SpigotListener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage("");

        event.getPlayer().setDisplayName(TextFormatter.translateColors(LuckPermsProvider.get().getUserManager().getUser(event.getPlayer().getUniqueId()).getCachedData().getMetaData().getPrefix() + SpigotMain.getInstance().getData().getString("players." + event.getPlayer().getUniqueId() + ".nickname")));
        event.getPlayer().setCustomName(event.getPlayer().getDisplayName());
        event.getPlayer().setCustomNameVisible(true);
        event.getPlayer().setPlayerListName(event.getPlayer().getDisplayName());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage("");
    }
}