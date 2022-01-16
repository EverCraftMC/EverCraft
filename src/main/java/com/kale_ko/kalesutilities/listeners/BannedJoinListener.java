package com.kale_ko.kalesutilities.listeners;

import com.kale_ko.kalesutilities.Main;
import com.kale_ko.kalesutilities.Util;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public class BannedJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerLoginEvent event) {
        if (Main.Instance.players.getBoolean(event.getPlayer().getName() + ".banned")) {
            event.setKickMessage(Main.Instance.players.getString(event.getPlayer().getName() + ".banMessage"));
            event.disallow(Result.KICK_BANNED, Main.Instance.players.getString(event.getPlayer().getName() + ".banMessage"));

            Util.sendMessage(Main.Instance.getServer().getConsoleSender(), Main.Instance.config.getString("messages.bannedJoin").replace("{player}", Util.getPlayerName(event.getPlayer())));
        }
    }
}