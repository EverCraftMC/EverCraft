package io.github.evercraftmc.moderation.commands;

import io.github.evercraftmc.core.api.commands.ECCommand;
import io.github.evercraftmc.core.api.server.player.ECPlayer;
import io.github.evercraftmc.core.impl.util.ECTextFormatter;
import io.github.evercraftmc.moderation.ModerationModule;
import java.util.List;

public class KickCommand implements ECCommand {
    protected final ModerationModule parent;

    public KickCommand(ModerationModule parent) {
        this.parent = parent;
    }

    @Override
    public String getName() {
        return "kick";
    }

    @Override
    public String getDescription() {
        return "Kick a player";
    }

    @Override
    public List<String> getAlias() {
        return List.of();
    }

    @Override
    public String getPermission() {
        return "evercraft.moderation.commands.kick";
    }

    @Override
    public void run(ECPlayer player, String[] args, boolean sendFeedback) {
        if (args.length > 0) {
            ECPlayer player2 = parent.getPlugin().getServer().getOnlinePlayer(args[0]);

            if (player2 != null) {
                StringBuilder reasonBuilder = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    reasonBuilder.append(args[i]).append(" ");
                }
                String reason = reasonBuilder.toString().trim();

                if (sendFeedback) {
                    if (!reason.isEmpty()) {
                        parent.getPlugin().getServer().broadcastMessage(ECTextFormatter.translateColors("&r" + player2.getDisplayName() + " &r&ahas been kicked by &r" + player.getDisplayName() + " &r&afor \"&r" + reason + "&r&a\"."));

                        player2.kick("&cYou have been kicked by &r" + player.getDisplayName() + " &r&cfor \"&r" + reason + "&r&c\".");

                        player.sendMessage(ECTextFormatter.translateColors("&aSuccessfully kicked player &r" + player2.getDisplayName() + " &r&afor \"&r" + reason + "&r&a\"."));
                    } else {
                        parent.getPlugin().getServer().broadcastMessage(ECTextFormatter.translateColors("&r" + player2.getDisplayName() + " &r&ahas been kicked by &r" + player.getDisplayName() + "&r&a."));

                        player2.kick("&cYou have been kicked by &r" + player.getDisplayName() + "&r&c.");

                        player.sendMessage(ECTextFormatter.translateColors("&aSuccessfully kicked player &r" + player2.getDisplayName() + "&r&a."));
                    }
                }
            }
        } else if (sendFeedback) {
            player.sendMessage(ECTextFormatter.translateColors("&cMust pass a username."));
        }
    }

    @Override
    public List<String> tabComplete(ECPlayer player, String[] args) {
        if (args.length == 1) {
            return List.of("reset");
        } else {
            return List.of();
        }
    }
}