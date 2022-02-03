package com.kale_ko.kalesutilities.spigot.listeners;

import com.kale_ko.kalesutilities.spigot.SpigotPlugin;
import java.util.Date;
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