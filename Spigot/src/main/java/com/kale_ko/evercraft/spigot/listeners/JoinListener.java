package com.kale_ko.evercraft.spigot.listeners;

import com.kale_ko.evercraft.shared.util.formatting.TextFormatter;
import com.kale_ko.evercraft.spigot.SpigotMain;
import com.kale_ko.evercraft.spigot.util.formatting.ComponentFormatter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import net.kyori.adventure.text.Component;
import net.luckperms.api.LuckPermsProvider;

public class JoinListener extends SpigotListener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.joinMessage(Component.empty());

        event.getPlayer().displayName(ComponentFormatter.stringToComponent(TextFormatter.translateColors(LuckPermsProvider.get().getUserManager().getUser(event.getPlayer().getUniqueId()).getCachedData().getMetaData().getPrefix() + SpigotMain.getInstance().getData().getString("players." + event.getPlayer().getUniqueId() + ".nickname"))));
        event.getPlayer().customName(event.getPlayer().displayName());
        event.getPlayer().setCustomNameVisible(true);
        event.getPlayer().playerListName(event.getPlayer().displayName());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.quitMessage(Component.empty());
    }
}