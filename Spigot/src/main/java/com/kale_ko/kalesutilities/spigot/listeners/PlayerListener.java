package com.kale_ko.kalesutilities.spigot.listeners;

import com.kale_ko.kalesutilities.spigot.Main;
import com.kale_ko.kalesutilities.spigot.Util;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (Main.Instance.players.getString(event.getPlayer().getName() + ".nickname") == null) {
            Main.Instance.players.set(event.getPlayer().getName() + ".nickname", event.getPlayer().getName());
        }

        if (Main.Instance.players.getString(event.getPlayer().getName() + ".prefix") == null) {
            Main.Instance.players.set(event.getPlayer().getName() + ".prefix", "");
        }

        Util.updatePlayerName(event.getPlayer());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        if ((Main.Instance.players.getString(event.getPlayer().getName() + ".nickname").equalsIgnoreCase("") || Main.Instance.players.getString(event.getPlayer().getName() + ".nickname").equalsIgnoreCase(event.getPlayer().getName())) && Main.Instance.players.getString(event.getPlayer().getName() + ".prefix").equalsIgnoreCase("")) {
            Main.Instance.players.set(event.getPlayer().getName(), null);
        }
    }
}