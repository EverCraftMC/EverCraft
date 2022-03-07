package com.kale_ko.evercraft.spigot.listeners;

import java.util.Date;
import com.kale_ko.evercraft.spigot.SpigotPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class SeenListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        SpigotPlugin.Instance.players.set(event.getPlayer().getName() + ".lastseen", new Date().getTime());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        SpigotPlugin.Instance.players.set(event.getPlayer().getName() + ".lastseen", new Date().getTime());
    }
}