package com.kale_ko.kalesutilities.spigot.listeners;

import com.kale_ko.kalesutilities.spigot.SpigotPlugin;
import com.kale_ko.kalesutilities.spigot.Util;
import org.bukkit.craftbukkit.v1_18_R1.advancement.CraftAdvancement;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

public class ChatListener implements Listener {
    @EventHandler
    public void onChatMessage(AsyncPlayerChatEvent event) {
        if (!event.isCancelled()) {
            Util.messageBungee("globalChat", SpigotPlugin.Instance.config.getString("config.serverName"), "[" + SpigotPlugin.Instance.config.getString("config.serverName") + "] " + event.getFormat().replace("%2$s", event.getMessage()));

            SpigotPlugin.Instance.bot.sendMessage(Util.discordFormating("[" + SpigotPlugin.Instance.config.getString("config.serverName") + "] " + event.getFormat().replace("%2$s", event.getMessage())));
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Util.messageBungee("globalChat", SpigotPlugin.Instance.config.getString("config.serverName"), "[" + SpigotPlugin.Instance.config.getString("config.serverName") + "] " + event.getDeathMessage());

        SpigotPlugin.Instance.bot.sendMessage(Util.discordFormating("[" + SpigotPlugin.Instance.config.getString("config.serverName") + "] " + event.getDeathMessage()));
    }

    @EventHandler
    public void onAdvancement(PlayerAdvancementDoneEvent event) {
        if (((CraftAdvancement) event.getAdvancement()).getHandle().c().e().a() != "challenge") {
            Util.messageBungee("globalChat", SpigotPlugin.Instance.config.getString("config.serverName"), "[" + SpigotPlugin.Instance.config.getString("config.serverName") + "] " + event.getPlayer().getName() + " has made the advancement [" + ((CraftAdvancement) event.getAdvancement()).getHandle().c().a().getString() + "]");

            SpigotPlugin.Instance.bot.sendMessage(Util.discordFormating("[" + SpigotPlugin.Instance.config.getString("config.serverName") + "] " + event.getPlayer().getName() + " has made the advancement [" + ((CraftAdvancement) event.getAdvancement()).getHandle().c().a().getString() + "]"));
        } else {
            Util.messageBungee("globalChat", SpigotPlugin.Instance.config.getString("config.serverName"), "[" + SpigotPlugin.Instance.config.getString("config.serverName") + "] " + event.getPlayer().getName() + " has completed the challenge [" + ((CraftAdvancement) event.getAdvancement()).getHandle().c().a().getString() + "]");

            SpigotPlugin.Instance.bot.sendMessage(Util.discordFormating("[" + SpigotPlugin.Instance.config.getString("config.serverName") + "] " + event.getPlayer().getName() + " has completed the challenge [" + ((CraftAdvancement) event.getAdvancement()).getHandle().c().a().getString() + "]"));
        }
    }
}