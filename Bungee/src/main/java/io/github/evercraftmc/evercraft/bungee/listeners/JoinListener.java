package io.github.evercraftmc.evercraft.bungee.listeners;

import java.net.InetSocketAddress;
import java.time.Instant;
import io.github.evercraftmc.evercraft.bungee.BungeeMain;
import io.github.evercraftmc.evercraft.bungee.scoreboard.ScoreBoard;
import io.github.evercraftmc.evercraft.bungee.util.formatting.ComponentFormatter;
import io.github.evercraftmc.evercraft.bungee.util.network.TabListUtil;
import io.github.evercraftmc.evercraft.bungee.util.player.BungeePlayerResolver;
import io.github.evercraftmc.evercraft.shared.PluginData;
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
        if (!BungeeMain.getInstance().getPluginData().getParsed().players.containsKey(event.getPlayer().getUniqueId().toString())) {
            BungeeMain.getInstance().getPluginData().getParsed().players.put(event.getPlayer().getUniqueId().toString(), new PluginData.Player());
        }

        if (!BungeeMain.getInstance().getPluginData().getParsed().votes.containsKey(event.getPlayer().getName())) {
            BungeeMain.getInstance().getPluginData().getParsed().votes.put(event.getPlayer().getName(), new PluginData.Vote());
        }

        BungeeMain.getInstance().getPluginData().save();

        if (BungeeMain.getInstance().getPluginData().getParsed().players.get(event.getPlayer().getUniqueId().toString()).ban.banned) {
            String time = BungeeMain.getInstance().getPluginData().getParsed().players.get(event.getPlayer().getUniqueId().toString()).ban.until;

            if (time.equalsIgnoreCase("forever") || Instant.parse(time).isAfter(Instant.now())) {
                if (BungeeMain.getInstance().getPluginData().getParsed().players.get(event.getPlayer().getUniqueId().toString()).ban.reason != null) {
                    event.getPlayer().disconnect(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().moderation.ban.reason.replace("{reason}", BungeeMain.getInstance().getPluginData().getParsed().players.get(event.getPlayer().getUniqueId().toString()).ban.reason).replace("{moderator}", BungeeMain.getInstance().getPluginData().getParsed().players.get(event.getPlayer().getUniqueId().toString()).ban.by).replace("{time}", time))));
                } else {
                    event.getPlayer().disconnect(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().moderation.ban.noReason.replace("{moderator}", BungeeMain.getInstance().getPluginData().getParsed().players.get(event.getPlayer().getUniqueId().toString()).ban.by).replace("{time}", time))));
                }
            } else if (Instant.parse(time).isBefore(Instant.now())) {
                BungeeMain.getInstance().getPluginData().getParsed().players.get(event.getPlayer().getUniqueId().toString()).ban.banned = false;
                BungeeMain.getInstance().getPluginData().getParsed().players.get(event.getPlayer().getUniqueId().toString()).ban.reason = null;
                BungeeMain.getInstance().getPluginData().getParsed().players.get(event.getPlayer().getUniqueId().toString()).ban.by = null;
                BungeeMain.getInstance().getPluginData().getParsed().players.get(event.getPlayer().getUniqueId().toString()).ban.until = null;
                BungeeMain.getInstance().getPluginData().save();
            }

            return;
        } else if (BungeeMain.getInstance().getPluginData().getParsed().maintenance) {
            if (!event.getPlayer().hasPermission("evercraft.commands.moderation.bypassMaintenance")) {
                event.getPlayer().disconnect(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().moderation.maintenance.kick)));

                return;
            }
        }

        BungeeMain.getInstance().getPluginData().getParsed().players.get(event.getPlayer().getUniqueId().toString()).uuid = event.getPlayer().getUniqueId().toString();
        BungeeMain.getInstance().getPluginData().getParsed().players.get(event.getPlayer().getUniqueId().toString()).lastName = event.getPlayer().getName();
        BungeeMain.getInstance().getPluginData().getParsed().players.get(event.getPlayer().getUniqueId().toString()).lastIP = ((InetSocketAddress) event.getPlayer().getSocketAddress()).getHostString();

        if (BungeeMain.getInstance().getPluginData().getParsed().players.get(event.getPlayer().getUniqueId().toString()).nickname == null) {
            BungeeMain.getInstance().getPluginData().getParsed().players.get(event.getPlayer().getUniqueId().toString()).nickname = event.getPlayer().getName();
        }

        event.getPlayer().setDisplayName(TextFormatter.translateColors(BungeePlayerResolver.getDisplayName(BungeeMain.getInstance().getPluginData(), event.getPlayer().getUniqueId())));

        if (!BungeeMain.getInstance().getPluginData().getParsed().players.get(event.getPlayer().getUniqueId().toString()).joinedBefore) {
            BungeeMain.getInstance().getPluginData().getParsed().players.get(event.getPlayer().getUniqueId().toString()).joinedBefore = true;

            BungeeMain.getInstance().getProxy().broadcast(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().welcome.firstJoin.replace("{player}", event.getPlayer().getDisplayName()))));

            BungeeMain.getInstance().getDiscordBot().getGuild().getTextChannelById(BungeeMain.getInstance().getPluginConfig().getParsed().discord.channelId).sendMessage(TextFormatter.discordFormat(BungeeMain.getInstance().getPluginMessages().getParsed().welcome.firstJoin.replace("{player}", event.getPlayer().getDisplayName()))).queue();
        }

        BungeeMain.getInstance().getPluginData().save();

        BungeeMain.getInstance().getProxy().broadcast(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().welcome.join.replace("{player}", event.getPlayer().getDisplayName()))));

        BungeeMain.getInstance().getDiscordBot().getGuild().getTextChannelById(BungeeMain.getInstance().getPluginConfig().getParsed().discord.channelId).sendMessage(TextFormatter.discordFormat(BungeeMain.getInstance().getPluginMessages().getParsed().welcome.join.replace("{player}", event.getPlayer().getDisplayName()))).queue();

        TabListUtil.removeFromList(event.getPlayer());
        TabListUtil.addToList(event.getPlayer());

        for (ProxiedPlayer player : BungeeMain.getInstance().getProxy().getPlayers()) {
            if (player != event.getPlayer()) {
                TabListUtil.removeFromList(player, event.getPlayer());

                if (player.getServer() != null) {
                    TabListUtil.addToList(player, event.getPlayer(), player.getServer().getInfo());
                } else {
                    TabListUtil.addToList(player, event.getPlayer());
                }
            }
        }

        for (int i = BungeeMain.getInstance().getPluginData().getParsed().votes.get(event.getPlayer().getName()).toProcess; i > 0; i--) {
            VoteListener.process(event.getPlayer());
        }

        BungeeMain.getInstance().getPluginData().getParsed().votes.get(event.getPlayer().getName()).toProcess = 0;
        BungeeMain.getInstance().getPluginData().save();
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
            if (player != event.getPlayer()) {
                TabListUtil.removeFromList(player, event.getPlayer());

                if (player.getServer() != null) {
                    TabListUtil.addToList(player, event.getPlayer(), player.getServer().getInfo());
                } else {
                    TabListUtil.addToList(player, event.getPlayer());
                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerDisconnectEvent event) {
        BungeeMain.getInstance().getPluginData().getParsed().players.get(event.getPlayer().getUniqueId().toString()).lastOnline = Instant.now().getEpochSecond();
        BungeeMain.getInstance().getPluginData().save();

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