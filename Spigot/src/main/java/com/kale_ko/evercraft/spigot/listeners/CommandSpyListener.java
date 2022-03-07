package com.kale_ko.evercraft.spigot.listeners;

import com.kale_ko.evercraft.spigot.SpigotPlugin;
import com.kale_ko.evercraft.spigot.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandSpyListener implements Listener {
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (!event.getMessage().startsWith("/sc") && !event.getMessage().startsWith("/staffchat")) {
            Util.messageBungee("globalCommandspy", SpigotPlugin.Instance.config.getString("config.serverName"), SpigotPlugin.Instance.config.getString("messages.commandspy").replace("{player}", Util.getPlayerName(event.getPlayer())).replace("{message}", event.getMessage()));

            for (Player player : SpigotPlugin.Instance.getServer().getOnlinePlayers()) {
                if (player != event.getPlayer() && Util.hasPermission(player, "evercraft.commands.staff.commandspy") && (Util.hasMetadata(player, "receiveCommandSpy") && Util.getMetadata(player, "receiveCommandSpy").asBoolean())) {
                    Util.sendMessage(player, SpigotPlugin.Instance.config.getString("messages.commandspy").replace("{player}", Util.getPlayerName(event.getPlayer())).replace("{message}", event.getMessage()), true);
                }
            }
        }
    }
}