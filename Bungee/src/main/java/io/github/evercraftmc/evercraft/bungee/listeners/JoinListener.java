package io.github.evercraftmc.evercraft.bungee.listeners;

import java.net.InetSocketAddress;
import java.time.Instant;
import java.util.Date;
import io.github.evercraftmc.evercraft.bungee.BungeeMain;
import io.github.evercraftmc.evercraft.bungee.util.formatting.ComponentFormatter;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import net.luckperms.api.LuckPermsProvider;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.event.ServerConnectEvent.Reason;
import net.md_5.bungee.event.EventHandler;

public class JoinListener extends BungeeListener {
    @EventHandler
    public void onPlayerConnect(ServerConnectEvent event) {
        if (event.getReason() == Reason.JOIN_PROXY) {
            if (event.getPlayer().getPendingConnection().getVirtualHost() != null && BungeeMain.getInstance().getProxy().getServerInfo(event.getPlayer().getPendingConnection().getVirtualHost().getHostName().split("\\.")[0]) != null) {
                event.setTarget(BungeeMain.getInstance().getProxy().getServerInfo(event.getPlayer().getPendingConnection().getVirtualHost().getHostName().split("\\.")[0]));
            } else {
                event.setTarget(BungeeMain.getInstance().getProxy().getServerInfo(BungeeMain.getInstance().getProxy().getConfigurationAdapter().getListeners().iterator().next().getServerPriority().get(0)));
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PostLoginEvent event) {
        if (BungeeMain.getInstance().getData().getBoolean("players." + event.getPlayer().getUniqueId() + ".ban.banned")) {
            String time = BungeeMain.getInstance().getData().getString("players." + event.getPlayer().getUniqueId() + ".ban.until");

            if (time.equalsIgnoreCase("forever") || Instant.parse(time).isAfter(Instant.now())) {
                if (BungeeMain.getInstance().getData().getString("players." + event.getPlayer().getUniqueId() + ".ban.reason") != null) {
                    event.getPlayer().disconnect(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("moderation.ban.reason").replace("{reason}", BungeeMain.getInstance().getData().getString("players." + event.getPlayer().getUniqueId() + ".ban.reason")).replace("{moderator}", BungeeMain.getInstance().getData().getString("players." + event.getPlayer().getUniqueId() + ".ban.by")).replace("{time}", time))));
                } else {
                    event.getPlayer().disconnect(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("moderation.ban.noreason").replace("{moderator}", BungeeMain.getInstance().getData().getString("players." + event.getPlayer().getUniqueId() + ".ban.by")).replace("{time}", time))));
                }
            } else if (Instant.parse(time).isBefore(Instant.now())) {
                BungeeMain.getInstance().getData().set("players." + event.getPlayer().getUniqueId() + ".ban.banned", null);
                BungeeMain.getInstance().getData().set("players." + event.getPlayer().getUniqueId() + ".ban.reason", null);
                BungeeMain.getInstance().getData().set("players." + event.getPlayer().getUniqueId() + ".ban.by", null);
                BungeeMain.getInstance().getData().set("players." + event.getPlayer().getUniqueId() + ".ban.until", null);
            }

            return;
        } else if (BungeeMain.getInstance().getData().getBoolean("maintenance")) {
            if (!event.getPlayer().hasPermission("evercraft.commands.moderation.bypassMaintenance")) {
                event.getPlayer().disconnect(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("moderation.maintenance.kick"))));

                return;
            }
        }

        BungeeMain.getInstance().getData().set("players." + event.getPlayer().getUniqueId() + ".lastname", event.getPlayer().getName());
        BungeeMain.getInstance().getData().set("players." + event.getPlayer().getUniqueId() + ".lastip", ((InetSocketAddress) event.getPlayer().getSocketAddress()).getHostString());

        if (BungeeMain.getInstance().getData().getString("players." + event.getPlayer().getUniqueId() + ".nickname") == null) {
            BungeeMain.getInstance().getData().set("players." + event.getPlayer().getUniqueId() + ".nickname", event.getPlayer().getName());
        }

        event.getPlayer().setDisplayName(TextFormatter.translateColors(LuckPermsProvider.get().getUserManager().getUser(event.getPlayer().getUniqueId()).getCachedData().getMetaData().getPrefix() + BungeeMain.getInstance().getData().getString("players." + event.getPlayer().getUniqueId() + ".nickname")));

        if (!BungeeMain.getInstance().getData().getBoolean("players." + event.getPlayer().getUniqueId() + ".joinedBefore")) {
            BungeeMain.getInstance().getData().set("players." + event.getPlayer().getUniqueId() + ".joinedBefore", true);

            BungeeMain.getInstance().getProxy().broadcast(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("welcome.firstJoin").replace("{player}", event.getPlayer().getDisplayName()))));

            BungeeMain.getInstance().getDiscordBot().getGuild().getTextChannelById(BungeeMain.getInstance().getPluginConfig().getString("discord.channelId")).sendMessage(TextFormatter.discordFormat(BungeeMain.getInstance().getPluginMessages().getString("welcome.firstJoin").replace("{player}", event.getPlayer().getDisplayName()))).queue();
        }

        BungeeMain.getInstance().getProxy().broadcast(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("welcome.join").replace("{player}", event.getPlayer().getDisplayName()))));

        BungeeMain.getInstance().getDiscordBot().getGuild().getTextChannelById(BungeeMain.getInstance().getPluginConfig().getString("discord.channelId")).sendMessage(TextFormatter.discordFormat(BungeeMain.getInstance().getPluginMessages().getString("welcome.join").replace("{player}", event.getPlayer().getDisplayName()))).queue();

        if (BungeeMain.getInstance().getData().getFloat("votes." + event.getPlayer().getName() + ".toProcess") != null) {
            for (int i = BungeeMain.getInstance().getData().getInteger("votes." + event.getPlayer().getName() + ".toProcess"); i > 0; i--) {
                VoteListener.process(event.getPlayer());
            }

            BungeeMain.getInstance().getData().set("votes." + event.getPlayer().getName() + ".toProcess", null);
        }
    }

    @EventHandler
    public void onPlayerConnect(ServerConnectedEvent event) {
        BungeeMain.getInstance().getProxy().broadcast(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("welcome.move").replace("{player}", event.getPlayer().getDisplayName())).replace("{server}", event.getServer().getInfo().getName())));

        BungeeMain.getInstance().getDiscordBot().getGuild().getTextChannelById(BungeeMain.getInstance().getPluginConfig().getString("discord.channelId")).sendMessage(TextFormatter.discordFormat(BungeeMain.getInstance().getPluginMessages().getString("welcome.move").replace("{player}", event.getPlayer().getDisplayName()).replace("{server}", event.getServer().getInfo().getName()))).queue();
    }

    @EventHandler
    public void onPlayerQuit(PlayerDisconnectEvent event) {
        BungeeMain.getInstance().getData().set("players." + event.getPlayer().getUniqueId() + ".lastseen", new Date().getTime());

        BungeeMain.getInstance().getProxy().broadcast(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("welcome.quit").replace("{player}", event.getPlayer().getDisplayName()))));

        BungeeMain.getInstance().getDiscordBot().getGuild().getTextChannelById(BungeeMain.getInstance().getPluginConfig().getString("discord.channelId")).sendMessage(TextFormatter.discordFormat(BungeeMain.getInstance().getPluginMessages().getString("welcome.quit").replace("{player}", event.getPlayer().getDisplayName()))).queue();
    }
}