package com.kale_ko.kalesutilities.spigot.listeners;

import com.kale_ko.kalesutilities.spigot.SpigotPlugin;
import com.kale_ko.kalesutilities.spigot.Util;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class MentionListener implements Listener {
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        for (Player player : SpigotPlugin.Instance.getServer().getOnlinePlayers()) {
            if (event.getMessage().contains("@" + player.getName())) {
                event.setMessage(event.getMessage().replace("@" + player.getName(), Util.formatMessage("&b@") + player.getName() + Util.formatMessage("&r")));
                player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1, 1);
            }
        }
    }
}