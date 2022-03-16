package com.kale_ko.evercraft.bungee.listeners;

import com.kale_ko.evercraft.bungee.BungeePlugin;
import com.kale_ko.evercraft.bungee.Util;
import com.kale_ko.evercraft.bungee.discord.DiscordBot.MessageType;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class WelcomeListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PostLoginEvent event) {
        BungeePlugin.Instance.bot.sendMessage(MessageType.Chat, "[" + BungeePlugin.Instance.config.getString("config.serverName") + "] " + Util.discordFormating(BungeePlugin.Instance.config.getString("messages.joinMessage").replace("{player}", Util.getPlayerName(event.getPlayer()))));

        Util.broadcastMessage(Util.formatMessage(BungeePlugin.Instance.config.getString("messages.joinMessage").replace("{player}", Util.getPlayerName(event.getPlayer()))), true);

        BungeePlugin.Instance.bot.updateOnline(BungeePlugin.Instance.getProxy().getOnlineCount());
    }

    @EventHandler
    public void onPlayerMove(ServerConnectedEvent event) {
        BungeePlugin.Instance.bot.sendMessage(MessageType.Chat, "[" + BungeePlugin.Instance.config.getString("config.serverName") + "] " + Util.discordFormating(BungeePlugin.Instance.config.getString("messages.moveMessage").replace("{player}", Util.getPlayerName(event.getPlayer())).replace("{server}", event.getServer().getInfo().getName())));

        Util.broadcastMessage(Util.formatMessage(BungeePlugin.Instance.config.getString("messages.moveMessage").replace("{player}", Util.getPlayerName(event.getPlayer())).replace("{server}", event.getServer().getInfo().getName())), true);
    }

    @EventHandler
    public void onPlayerLeave(PlayerDisconnectEvent event) {
        BungeePlugin.Instance.bot.sendMessage(MessageType.Chat, "[" + BungeePlugin.Instance.config.getString("config.serverName") + "] " + Util.discordFormating(BungeePlugin.Instance.config.getString("messages.quitMessage").replace("{player}", Util.getPlayerName(event.getPlayer()))));

        Util.broadcastMessage(Util.formatMessage(BungeePlugin.Instance.config.getString("messages.quitMessage").replace("{player}", Util.getPlayerName(event.getPlayer()))), true);

        BungeePlugin.Instance.bot.updateOnline(BungeePlugin.Instance.getProxy().getOnlineCount() - 1);
    }
}