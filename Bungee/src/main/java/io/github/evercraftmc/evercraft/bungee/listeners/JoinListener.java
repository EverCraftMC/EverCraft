package io.github.evercraftmc.evercraft.bungee.listeners;

import java.net.InetSocketAddress;
import java.time.Instant;
import io.github.evercraftmc.evercraft.bungee.BungeeMain;
import io.github.evercraftmc.evercraft.bungee.scoreboard.ScoreBoard;
import io.github.evercraftmc.evercraft.bungee.util.formatting.ComponentFormatter;
import io.github.evercraftmc.evercraft.bungee.util.network.TabListUtil;
import io.github.evercraftmc.evercraft.bungee.util.player.BungeePlayerResolver;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent.Reason;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.event.ServerKickEvent.Cause;
import net.md_5.bungee.event.EventHandler;

public class JoinListener extends BungeeListener {
    @EventHandler
    public void onPlayerConnect(ServerConnectEvent event) {
        if (event.getReason() == Reason.JOIN_PROXY) {
            if (event.getPlayer().getPendingConnection().getVirtualHost() != null && BungeeMain.getInstance().getProxy().getServerInfo(event.getPlayer().getPendingConnection().getVirtualHost().getHostName().split("\\.")[0]) != null) {
                event.setTarget(BungeeMain.getInstance().getProxy().getServerInfo(event.getPlayer().getPendingConnection().getVirtualHost().getHostName().split("\\.")[0]));
            } else {
                event.setTarget(BungeeMain.getInstance().getProxy().getServerInfo(BungeeMain.getInstance().getPluginConfig().getParsed().server.main));
            }

            event.getPlayer().setReconnectServer(BungeeMain.getInstance().getProxy().getServerInfo(BungeeMain.getInstance().getPluginConfig().getParsed().server.fallback));
        }
    }

    @EventHandler
    public void onPlayerJoin(PostLoginEvent event) {
        if (BungeeMain.getInstance().getPluginData().getBoolean("players." + event.getPlayer().getUniqueId() + ".ban.banned")) {
            String time = BungeeMain.getInstance().getPluginData().getString("players." + event.getPlayer().getUniqueId() + ".ban.until");

            if (time.equalsIgnoreCase("forever") || Instant.parse(time).isAfter(Instant.now())) {
                if (BungeeMain.getInstance().getPluginData().getString("players." + event.getPlayer().getUniqueId() + ".ban.reason") != null) {
                    event.getPlayer().disconnect(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().moderation.ban.reason.replace("{reason}", BungeeMain.getInstance().getPluginData().getString("players." + event.getPlayer().getUniqueId() + ".ban.reason")).replace("{moderator}", BungeeMain.getInstance().getPluginData().getString("players." + event.getPlayer().getUniqueId() + ".ban.by")).replace("{time}", time))));
                } else {
                    event.getPlayer().disconnect(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().moderation.ban.noReason.replace("{moderator}", BungeeMain.getInstance().getPluginData().getString("players." + event.getPlayer().getUniqueId() + ".ban.by")).replace("{time}", time))));
                }
            } else if (Instant.parse(time).isBefore(Instant.now())) {
                BungeeMain.getInstance().getPluginData().set("players." + event.getPlayer().getUniqueId() + ".ban.banned", null);
                BungeeMain.getInstance().getPluginData().set("players." + event.getPlayer().getUniqueId() + ".ban.reason", null);
                BungeeMain.getInstance().getPluginData().set("players." + event.getPlayer().getUniqueId() + ".ban.by", null);
                BungeeMain.getInstance().getPluginData().set("players." + event.getPlayer().getUniqueId() + ".ban.until", null);
            }

            return;
        } else if (BungeeMain.getInstance().getPluginData().getBoolean("maintenance")) {
            if (!event.getPlayer().hasPermission("evercraft.commands.moderation.bypassMaintenance")) {
                event.getPlayer().disconnect(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().moderation.maintenance.kick)));

                return;
            }
        }

        BungeeMain.getInstance().getPluginData().set("players." + event.getPlayer().getUniqueId() + ".lastname", event.getPlayer().getName());
        BungeeMain.getInstance().getPluginData().set("players." + event.getPlayer().getUniqueId() + ".lastip", ((InetSocketAddress) event.getPlayer().getSocketAddress()).getHostString());

        if (BungeeMain.getInstance().getPluginData().getString("players." + event.getPlayer().getUniqueId() + ".nickname") == null) {
            BungeeMain.getInstance().getPluginData().set("players." + event.getPlayer().getUniqueId() + ".nickname", event.getPlayer().getName());
        }

        event.getPlayer().setDisplayName(TextFormatter.translateColors(BungeePlayerResolver.getDisplayName(BungeeMain.getInstance().getPluginData(), event.getPlayer().getUniqueId())));

        if (!BungeeMain.getInstance().getPluginData().getBoolean("players." + event.getPlayer().getUniqueId() + ".joinedBefore")) {
            BungeeMain.getInstance().getPluginData().set("players." + event.getPlayer().getUniqueId() + ".joinedBefore", true);

            BungeeMain.getInstance().getProxy().broadcast(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().welcome.firstJoin.replace("{player}", event.getPlayer().getDisplayName()))));

            BungeeMain.getInstance().getDiscordBot().getGuild().getTextChannelById(BungeeMain.getInstance().getPluginConfig().getParsed().discord.channelId).sendMessage(TextFormatter.discordFormat(BungeeMain.getInstance().getPluginMessages().getParsed().welcome.firstJoin.replace("{player}", event.getPlayer().getDisplayName()))).queue();
        }

        BungeeMain.getInstance().getProxy().broadcast(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().welcome.join.replace("{player}", event.getPlayer().getDisplayName()))));

        BungeeMain.getInstance().getDiscordBot().getGuild().getTextChannelById(BungeeMain.getInstance().getPluginConfig().getParsed().discord.channelId).sendMessage(TextFormatter.discordFormat(BungeeMain.getInstance().getPluginMessages().getParsed().welcome.join.replace("{player}", event.getPlayer().getDisplayName()))).queue();

        TabListUtil.removeFromList(event.getPlayer());
        TabListUtil.addToList(event.getPlayer());

        for (ProxiedPlayer player : BungeeMain.getInstance().getProxy().getPlayers()) {
            if (player.getServer() != null) {
                TabListUtil.addToList(player, event.getPlayer(), player.getServer().getInfo());
            } else {
                TabListUtil.addToList(player, event.getPlayer());
            }
        }

        if (BungeeMain.getInstance().getPluginData().getFloat("votes." + event.getPlayer().getName() + ".toProcess") != null) {
            for (int i = BungeeMain.getInstance().getPluginData().getInteger("votes." + event.getPlayer().getName() + ".toProcess"); i > 0; i--) {
                VoteListener.process(event.getPlayer());
            }

            BungeeMain.getInstance().getPluginData().set("votes." + event.getPlayer().getName() + ".toProcess", null);
        }
    }

    @EventHandler
    public void onPlayerConnected(ServerConnectedEvent event) {
        BungeeMain.getInstance().getProxy().broadcast(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().welcome.move.replace("{player}", event.getPlayer().getDisplayName())).replace("{server}", event.getServer().getInfo().getName())));

        BungeeMain.getInstance().getDiscordBot().getGuild().getTextChannelById(BungeeMain.getInstance().getPluginConfig().getParsed().discord.channelId).sendMessage(TextFormatter.discordFormat(BungeeMain.getInstance().getPluginMessages().getParsed().welcome.move.replace("{player}", event.getPlayer().getDisplayName()).replace("{server}", event.getServer().getInfo().getName()))).queue();

        ScoreBoard.getInstance().getScoreboardMap().remove(event.getPlayer());
        ScoreBoard.getInstance().getLinesMap().remove(event.getPlayer());

        TabListUtil.removeFromList(event.getPlayer());
        TabListUtil.addToList(event.getPlayer(), event.getServer().getInfo());

        for (ProxiedPlayer player : BungeeMain.getInstance().getProxy().getPlayers()) {
            TabListUtil.removeFromList(player, event.getPlayer());

            if (player.getServer() != null) {
                TabListUtil.addToList(player, event.getPlayer(), player.getServer().getInfo());
            } else {
                TabListUtil.addToList(player, event.getPlayer());
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerDisconnectEvent event) {
        BungeeMain.getInstance().getPluginData().set("players." + event.getPlayer().getUniqueId() + ".lastonline", Instant.now().getEpochSecond());

        BungeeMain.getInstance().getProxy().broadcast(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().welcome.quit.replace("{player}", event.getPlayer().getDisplayName()))));

        BungeeMain.getInstance().getDiscordBot().getGuild().getTextChannelById(BungeeMain.getInstance().getPluginConfig().getParsed().discord.channelId).sendMessage(TextFormatter.discordFormat(BungeeMain.getInstance().getPluginMessages().getParsed().welcome.quit.replace("{player}", event.getPlayer().getDisplayName()))).queue();

        TabListUtil.removeFromList(event.getPlayer());
    }

    @EventHandler
    public void onPlayerKicked(ServerKickEvent event) {
        if (event.getPlayer().isConnected()) {
            if (event.getCause() == Cause.LOST_CONNECTION) {
                event.setCancelled(true);
                event.setCancelServer(BungeeMain.getInstance().getProxy().getServerInfo(BungeeMain.getInstance().getPluginConfig().getParsed().server.fallback));

                event.getPlayer().sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().server.disconnectedNoCom)));
            } else if (event.getCause() == Cause.EXCEPTION) {
                event.setCancelled(true);
                event.setCancelServer(BungeeMain.getInstance().getProxy().getServerInfo(BungeeMain.getInstance().getPluginConfig().getParsed().server.fallback));

                event.getPlayer().sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().server.disconnectedError.replace("{error}", ComponentFormatter.componentToString(event.getKickReasonComponent())))));
            }
        }
    }
}