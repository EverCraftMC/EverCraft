package io.github.evercraftmc.moderation.commands;

import io.github.evercraftmc.core.ECPlayerData;
import io.github.evercraftmc.core.api.commands.ECCommand;
import io.github.evercraftmc.core.api.server.player.ECPlayer;
import io.github.evercraftmc.core.impl.util.ECTextFormatter;
import io.github.evercraftmc.moderation.ModerationModule;
import io.github.evercraftmc.moderation.util.TimeUtil;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class MuteCommand implements ECCommand {
    protected final ModerationModule parent;

    public MuteCommand(ModerationModule parent) {
        this.parent = parent;
    }

    @Override
    public String getName() {
        return "mute";
    }

    @Override
    public String getDescription() {
        return "Mute a player";
    }

    @Override
    public List<String> getAlias() {
        return List.of();
    }

    @Override
    public String getPermission() {
        return "evercraft.moderation.commands.mute";
    }

    @Override
    public void run(ECPlayer player, String[] args, boolean sendFeedback) {
        if (args.length > 0) {
            ECPlayer player2 = parent.getPlugin().getServer().getPlayer(args[0]);

            if (player2 != null) {
                Instant until = TimeUtil.parseFuture(args[1]);

                StringBuilder reasonBuilder = new StringBuilder();
                for (int i = 2; i < args.length; i++) {
                    reasonBuilder.append(args[i]).append(" ");
                }
                String reason = reasonBuilder.toString().trim();

                if (sendFeedback) {
                    if (!reason.isEmpty()) {
                        parent.getPlugin().getServer().broadcastMessage(ECTextFormatter.translateColors("&r" + player2.getDisplayName() + " &r&ahas been muted by &r" + player.getDisplayName() + " &r&cuntil \"" + TimeUtil.stringifyFuture(until) + "\" for \"&r" + reason + "&r&c\"."));

                        parent.getPlugin().getPlayerData().players.get(player2.getUuid().toString()).mute = new ECPlayerData.Player.Moderation();
                        parent.getPlugin().getPlayerData().players.get(player2.getUuid().toString()).mute.moderator = player.getUuid();
                        parent.getPlugin().getPlayerData().players.get(player2.getUuid().toString()).mute.reason = reason;
                        parent.getPlugin().getPlayerData().players.get(player2.getUuid().toString()).mute.date = Instant.now();
                        parent.getPlugin().getPlayerData().players.get(player2.getUuid().toString()).mute.until = until;

                        ECPlayer onlinePlayer2 = parent.getPlugin().getServer().getOnlinePlayer(player2.getUuid());
                        if (onlinePlayer2 != null) {
                            onlinePlayer2.sendMessage(ECTextFormatter.translateColors("&cYou have been muted by &r" + player.getDisplayName() + " &r&cuntil \"" + TimeUtil.stringifyFuture(until) + "\" for \"&r" + reason + "&r&c\"."));
                        }

                        player.sendMessage(ECTextFormatter.translateColors("&aSuccessfully muted player &r" + player.getDisplayName() + " &r&cuntil \"" + TimeUtil.stringifyFuture(until) + "\" for \"&r" + reason + "&r&c\"."));
                    } else {
                        parent.getPlugin().getServer().broadcastMessage(ECTextFormatter.translateColors("&r" + player2.getDisplayName() + " &r&ahas been muted by &r" + player.getDisplayName() + " &r&auntil \"" + TimeUtil.stringifyFuture(until) + "\"."));

                        parent.getPlugin().getPlayerData().players.get(player2.getUuid().toString()).mute = new ECPlayerData.Player.Moderation();
                        parent.getPlugin().getPlayerData().players.get(player2.getUuid().toString()).mute.moderator = player.getUuid();
                        parent.getPlugin().getPlayerData().players.get(player2.getUuid().toString()).mute.reason = reason;
                        parent.getPlugin().getPlayerData().players.get(player2.getUuid().toString()).mute.date = Instant.now();
                        parent.getPlugin().getPlayerData().players.get(player2.getUuid().toString()).mute.until = until;

                        ECPlayer onlinePlayer2 = parent.getPlugin().getServer().getOnlinePlayer(player2.getUuid());
                        if (onlinePlayer2 != null) {
                            onlinePlayer2.sendMessage(ECTextFormatter.translateColors("&cYou have been muted by &r" + player.getDisplayName() + " &r&cuntil \"" + TimeUtil.stringifyFuture(until) + "\"."));
                        }

                        player.sendMessage(ECTextFormatter.translateColors("&aSuccessfully muted player &r" + player2.getDisplayName() + " &r&auntil \"" + TimeUtil.stringifyFuture(until) + "\"."));
                    }
                }
            } else {
                player.sendMessage(ECTextFormatter.translateColors("&cPlayer \"" + args[0] + "\" could not be found."));
            }
        } else if (sendFeedback) {
            player.sendMessage(ECTextFormatter.translateColors("&cYou must pass a username."));
        }
    }

    @Override
    public List<String> tabComplete(ECPlayer player, String[] args) {
        if (args.length == 1) {
            List<String> players = new ArrayList<>();
            for (ECPlayer player2 : parent.getPlugin().getServer().getOnlinePlayers()) {
                players.add(player2.getName());
            }
            return players;
        } else {
            return List.of();
        }
    }
}