package com.kale_ko.evercraft.bungee.listeners;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.kale_ko.evercraft.bungee.BungeeMain;
import com.kale_ko.evercraft.bungee.util.ConnectionStatus;
import com.kale_ko.evercraft.shared.util.formatting.TextFormatter;
import net.luckperms.api.LuckPermsProvider;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.event.ServerConnectEvent.Reason;
import net.md_5.bungee.event.EventHandler;

public class JoinListener extends BungeeListener {
    private Map<ProxiedPlayer, ConnectionStatus> connectionStatuses = new HashMap<ProxiedPlayer, ConnectionStatus>();

    @EventHandler
    public void onPlayerConnect(ServerConnectEvent event) {
        if (event.getReason() == Reason.JOIN_PROXY) {
            connectionStatuses.remove(event.getPlayer());
            connectionStatuses.put(event.getPlayer(), ConnectionStatus.CONNECTING);

            if (BungeeMain.getInstance().getProxy().getServerInfo(event.getPlayer().getPendingConnection().getVirtualHost().getHostName().split("\\.")[0]) != null) {
                event.setTarget(BungeeMain.getInstance().getProxy().getServerInfo(event.getPlayer().getPendingConnection().getVirtualHost().getHostName().split("\\.")[0]));
            } else {
                event.setTarget(BungeeMain.getInstance().getProxy().getServerInfo(BungeeMain.getInstance().getProxy().getConfigurationAdapter().getListeners().iterator().next().getServerPriority().get(0)));
            }
        }
    }

    public void onPlayerConnect(ServerConnectedEvent event) {
        connectionStatuses.remove(event.getPlayer());
        connectionStatuses.put(event.getPlayer(), ConnectionStatus.CONNECTED);
    }

    @EventHandler
    public void onPlayerJoin(PostLoginEvent event) {
        connectionStatuses.put(event.getPlayer(), ConnectionStatus.LOGGEDIN);

        if (BungeeMain.getInstance().getData().getBoolean("players." + event.getPlayer().getUniqueId() + ".ban.banned")) {
            String time = BungeeMain.getInstance().getData().getString("players." + event.getPlayer().getUniqueId() + ".ban.until");

            if (BungeeMain.getInstance().getData().getString("players." + event.getPlayer().getUniqueId() + ".ban.reason") != null) {
                event.getPlayer().disconnect(TextComponent.fromLegacyText(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("moderation.ban.reason").replace("{reason}", BungeeMain.getInstance().getData().getString("players." + event.getPlayer().getUniqueId() + ".ban.reason")).replace("{moderator}", BungeeMain.getInstance().getData().getString("players." + event.getPlayer().getUniqueId() + ".ban.by")).replace("{time}", time))));
            } else {
                event.getPlayer().disconnect(TextComponent.fromLegacyText(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("moderation.ban.noreason").replace("{moderator}", BungeeMain.getInstance().getData().getString("players." + event.getPlayer().getUniqueId() + ".ban.by")).replace("{time}", time))));
            }

            return;
        } else if (BungeeMain.getInstance().getData().getBoolean("maintenance")) {
            if (!event.getPlayer().hasPermission("evercraft.commands.moderation.bypassMaintenance")) {
                event.getPlayer().disconnect(TextComponent.fromLegacyText(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("moderation.maintenance.kick"))));

                return;
            }
        }

        BungeeMain.getInstance().getData().set("players." + event.getPlayer().getUniqueId() + ".lastname", event.getPlayer().getName());
        BungeeMain.getInstance().getData().set("players." + event.getPlayer().getUniqueId() + ".lastip", ((InetSocketAddress) event.getPlayer().getSocketAddress()).getHostString());

        if (BungeeMain.getInstance().getData().getString("players." + event.getPlayer().getUniqueId() + ".nickname") == null) {
            BungeeMain.getInstance().getData().set("players." + event.getPlayer().getUniqueId() + ".nickname", event.getPlayer().getName());
        }

        // TODO Fix unicode

        System.out.println(LuckPermsProvider.get().getUserManager().getUser(event.getPlayer().getUniqueId()).getCachedData().getMetaData().getPrefix() + BungeeMain.getInstance().getData().getString("players." + event.getPlayer().getUniqueId() + ".nickname"));
        System.out.println(TextFormatter.translateColors(LuckPermsProvider.get().getUserManager().getUser(event.getPlayer().getUniqueId()).getCachedData().getMetaData().getPrefix() + BungeeMain.getInstance().getData().getString("players." + event.getPlayer().getUniqueId() + ".nickname")));
        event.getPlayer().setDisplayName(TextFormatter.translateColors(LuckPermsProvider.get().getUserManager().getUser(event.getPlayer().getUniqueId()).getCachedData().getMetaData().getPrefix() + BungeeMain.getInstance().getData().getString("players." + event.getPlayer().getUniqueId() + ".nickname")));

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
        if (connectionStatuses.get(event.getPlayer()) != ConnectionStatus.LOGGEDIN) {
            BungeeMain.getInstance().getData().set("players." + event.getPlayer().getUniqueId() + ".lastseen", new Date().getTime());

            BungeeMain.getInstance().getProxy().broadcast(TextComponent.fromLegacyText(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("welcome.quit").replace("{player}", event.getPlayer().getDisplayName()))));

            BungeeMain.getInstance().getDiscordBot().getGuild().getTextChannelById(BungeeMain.getInstance().getPluginConfig().getString("discord.channelId")).sendMessage(TextFormatter.discordFormat(BungeeMain.getInstance().getPluginMessages().getString("welcome.quit").replace("{player}", event.getPlayer().getDisplayName()))).queue();
        }
    }
}