package com.kale_ko.evercraft.spigot.listeners;

import com.kale_ko.evercraft.spigot.SpigotPlugin;
import com.kale_ko.evercraft.spigot.Util;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public class BannedJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerLoginEvent event) {
        if (SpigotPlugin.Instance.players.getBoolean(event.getPlayer().getName() + ".banned")) {
            event.setKickMessage(SpigotPlugin.Instance.players.getString(event.getPlayer().getName() + ".banMessage"));
            event.disallow(Result.KICK_BANNED, SpigotPlugin.Instance.players.getString(event.getPlayer().getName() + ".banMessage"));

            Util.sendMessage(SpigotPlugin.Instance.getServer().getConsoleSender(), SpigotPlugin.Instance.config.getString("messages.bannedJoin").replace("{player}", Util.getPlayerName(event.getPlayer())));
        }
    }
}