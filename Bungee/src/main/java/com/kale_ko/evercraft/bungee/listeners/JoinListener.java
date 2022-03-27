package com.kale_ko.evercraft.bungee.listeners;

import java.net.InetSocketAddress;
import java.util.Date;
import com.kale_ko.evercraft.bungee.BungeeMain;
import com.kale_ko.evercraft.shared.util.formatting.TextFormatter;
import net.luckperms.api.LuckPermsProvider;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent.Reason;
import net.md_5.bungee.event.EventHandler;

public class JoinListener extends BungeeListener {
    @EventHandler
    public void onPlayerConnect(ServerConnectEvent event) {
        if (BungeeMain.getInstance().getData().getBoolean("players." + event.getPlayer().getUniqueId() + ".ban.banned")) {
            String time = BungeeMain.getInstance().getData().getString("players." + event.getPlayer().getUniqueId() + ".ban.until");

            if (BungeeMain.getInstance().getData().getString("players." + event.getPlayer().getUniqueId() + ".ban.reason") != null) {
                event.getPlayer().disconnect(TextComponent.fromLegacyText(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("moderation.ban.reason").replace("{reason}", BungeeMain.getInstance().getData().getString("players." + event.getPlayer().getUniqueId() + ".ban.reason")).replace("{moderator}", BungeeMain.getInstance().getData().getString("players." + event.getPlayer().getUniqueId() + ".ban.by")).replace("{time}", time))));
            } else {
                event.getPlayer().disconnect(TextComponent.fromLegacyText(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("moderation.ban.noreason").replace("{moderator}", BungeeMain.getInstance().getData().getString("players." + event.getPlayer().getUniqueId() + ".ban.by")).replace("{time}", time))));
            }

            event.setCancelled(true);
            return;
        } else if (BungeeMain.getInstance().getData().getBoolean("maintenance")) {
            if (!event.getPlayer().hasPermission("evercraft.commands.moderation.bypassMaintenance")) {
                event.getPlayer().disconnect(TextComponent.fromLegacyText(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("moderation.maintenance.kick"))));
            }
        }

        if (event.getReason() == Reason.JOIN_PROXY) {
            if (BungeeMain.getInstance().getProxy().getServerInfo(event.getPlayer().getPendingConnection().getVirtualHost().getHostName().split("\\.")[0]) != null) {
                event.setTarget(BungeeMain.getInstance().getProxy().getServerInfo(event.getPlayer().getPendingConnection().getVirtualHost().getHostName().split("\\.")[0]));
            } else {
                event.setTarget(BungeeMain.getInstance().getProxy().getServerInfo(BungeeMain.getInstance().getProxy().getConfigurationAdapter().getListeners().iterator().next().getServerPriority().get(0)));
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PostLoginEvent event) {
        BungeeMain.getInstance().getData().set("players." + event.getPlayer().getUniqueId() + ".lastname", event.getPlayer().getName());
        BungeeMain.getInstance().getData().set("players." + event.getPlayer().getUniqueId() + ".lastip", ((InetSocketAddress) event.getPlayer().getSocketAddress()).getHostString());

        if (BungeeMain.getInstance().getData().getString("players." + event.getPlayer().getUniqueId() + ".nickname") == null) {
            BungeeMain.getInstance().getData().set("players." + event.getPlayer().getUniqueId() + ".nickname", event.getPlayer().getName());
        }

        event.getPlayer().setDisplayName(LuckPermsProvider.get().getUserManager().getUser(event.getPlayer().getUniqueId()).getCachedData().getMetaData().getPrefix() + BungeeMain.getInstance().getData().getString("players." + event.getPlayer().getUniqueId() + ".nickname"));

        if (!BungeeMain.getInstance().getData().getBoolean("players." + event.getPlayer().getUniqueId() + ".joinedBefore")) {
            BungeeMain.getInstance().getData().set("players." + event.getPlayer().getUniqueId() + ".joinedBefore", true);

            BungeeMain.getInstance().getProxy().broadcast(TextComponent.fromLegacyText(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("welcome.firstJoin").replace("{player}", event.getPlayer().getDisplayName()))));

            BungeeMain.getInstance().getDiscordBot().getGuild().getTextChannelById(BungeeMain.getInstance().getPluginConfig().getString("discord.channelId")).sendMessage(TextFormatter.discordFormat(BungeeMain.getInstance().getPluginMessages().getString("welcome.firstJoin").replace("{player}", event.getPlayer().getDisplayName()))).queue();
        }

        BungeeMain.getInstance().getProxy().broadcast(TextComponent.fromLegacyText(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("welcome.join").replace("{player}", event.getPlayer().getDisplayName()))));

        BungeeMain.getInstance().getDiscordBot().getGuild().getTextChannelById(BungeeMain.getInstance().getPluginConfig().getString("discord.channelId")).sendMessage(TextFormatter.discordFormat(BungeeMain.getInstance().getPluginMessages().getString("welcome.join").replace("{player}", event.getPlayer().getDisplayName()))).queue();
    }

    @EventHandler
    public void onPlayerQuit(PlayerDisconnectEvent event) {
        BungeeMain.getInstance().getData().set("players." + event.getPlayer().getUniqueId() + ".lastseen", new Date().getTime());

        BungeeMain.getInstance().getProxy().broadcast(TextComponent.fromLegacyText(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("welcome.quit").replace("{player}", event.getPlayer().getDisplayName()))));

        BungeeMain.getInstance().getDiscordBot().getGuild().getTextChannelById(BungeeMain.getInstance().getPluginConfig().getString("discord.channelId")).sendMessage(TextFormatter.discordFormat(BungeeMain.getInstance().getPluginMessages().getString("welcome.quit").replace("{player}", event.getPlayer().getDisplayName()))).queue();
    }
}