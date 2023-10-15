package io.github.evercraftmc.moderation.commands;

import io.github.evercraftmc.core.api.commands.ECCommand;
import io.github.evercraftmc.core.api.server.player.ECPlayer;
import io.github.evercraftmc.core.impl.util.ECTextFormatter;
import io.github.evercraftmc.moderation.ModerationModule;
import java.util.ArrayList;
import java.util.List;

public class UnbanCommand implements ECCommand {
    protected final ModerationModule parent;

    public UnbanCommand(ModerationModule parent) {
        this.parent = parent;
    }

    @Override
    public String getName() {
        return "unban";
    }

    @Override
    public String getDescription() {
        return "Unban a player";
    }

    @Override
    public List<String> getAlias() {
        return List.of();
    }

    @Override
    public String getPermission() {
        return "evercraft.moderation.commands.ban";
    }

    @Override
    public void run(ECPlayer player, String[] args, boolean sendFeedback) {
        if (args.length > 0) {
            ECPlayer player2 = parent.getPlugin().getServer().getPlayer(args[0]);

            if (player2 != null) {
                if (sendFeedback) {
                    parent.getPlugin().getServer().broadcastMessage(ECTextFormatter.translateColors("&r" + player2.getDisplayName() + " &r&ahas been unbanned by &r" + player.getDisplayName() + "&r&a."));

                    parent.getPlugin().getPlayerData().players.get(player2.getUuid().toString()).ban = null;

                    ECPlayer onlinePlayer2 = parent.getPlugin().getServer().getOnlinePlayer(player2.getUuid());
                    if (onlinePlayer2 != null) {
                        onlinePlayer2.sendMessage(ECTextFormatter.translateColors("&aYou have been unbanned by &r" + player.getDisplayName() + "&r&a."));
                    }

                    player.sendMessage(ECTextFormatter.translateColors("&aSuccessfully unbanned player &r" + player2.getDisplayName() + "&r&a."));
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