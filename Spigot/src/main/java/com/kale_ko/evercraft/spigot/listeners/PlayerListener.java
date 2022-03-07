package com.kale_ko.evercraft.spigot.listeners;

import com.kale_ko.evercraft.spigot.SpigotPlugin;
import com.kale_ko.evercraft.spigot.Util;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (SpigotPlugin.Instance.players.getString(event.getPlayer().getName() + ".nickname") == null) {
            SpigotPlugin.Instance.players.set(event.getPlayer().getName() + ".nickname", event.getPlayer().getName());
        }

        Util.updatePlayerName(event.getPlayer());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        if (SpigotPlugin.Instance.players.getString(event.getPlayer().getName() + ".nickname").equalsIgnoreCase("")) {
            SpigotPlugin.Instance.players.set(event.getPlayer().getName(), null);
        }
    }
}