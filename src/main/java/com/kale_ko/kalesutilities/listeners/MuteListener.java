package com.kale_ko.kalesutilities.listeners;

import com.kale_ko.kalesutilities.Main;
import com.kale_ko.kalesutilities.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

@SuppressWarnings("deprecation")
public class MuteListener implements Listener {
    @EventHandler
    public void onChatMessage(PlayerChatEvent event) {
        if (Main.Instance.players.getBoolean(event.getPlayer().getName() + ".muted")) {
            event.setCancelled(true);

            Util.sendMessage(event.getPlayer(), Main.Instance.players.getString(event.getPlayer().getName() + ".mutedMessage"));

            for (Player player : Main.Instance.getServer().getOnlinePlayers()) {
                if (player != event.getPlayer() && Util.hasPermission(player, "kalesutilities.mute")) {
                    Util.sendMessage(player, Main.Instance.config.getString("messages.mutedMessage").replace("{player}", Util.getPlayerName(event.getPlayer())).replace("{message}", event.getMessage()));
                }
            }
        }
    }
}