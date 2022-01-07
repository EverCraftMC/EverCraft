package com.kale_ko.kalesutilities.listeners;

import com.kale_ko.kalesutilities.Main;
import com.kale_ko.kalesutilities.Util;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (Main.Instance.players.getConfig().getString("players." + event.getPlayer().getName() + ".nickname") == null) {
            Main.Instance.players.getConfig().set("players." + event.getPlayer().getName() + ".nickname", event.getPlayer().getName());
        }

        if (Main.Instance.players.getConfig().getString("players." + event.getPlayer().getName() + ".prefix") == null) {
            Main.Instance.players.getConfig().set("players." + event.getPlayer().getName() + ".prefix", "");
        }

        Main.Instance.players.save();

        Util.updatePlayerName(event.getPlayer());
    }
}