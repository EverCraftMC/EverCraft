package io.github.evercraftmc.moderation.commands;

import io.github.evercraftmc.core.api.commands.ECCommand;
import io.github.evercraftmc.core.api.server.player.ECPlayer;
import io.github.evercraftmc.core.impl.util.ECTextFormatter;
import io.github.evercraftmc.moderation.ModerationModule;
import java.util.List;

public class StaffChatCommand implements ECCommand {
    protected final ModerationModule parent;

    public StaffChatCommand(ModerationModule parent) {
        this.parent = parent;
    }

    @Override
    public String getName() {
        return "staffChat";
    }

    @Override
    public String getDescription() {
        return "Message the staffchat";
    }

    @Override
    public List<String> getAlias() {
        return List.of("sc");
    }

    @Override
    public String getPermission() {
        return "evercraft.moderation.commands.staffChat";
    }

    @Override
    public void run(ECPlayer player, String[] args, boolean sendFeedback) {
        if (args.length == 0) {
            parent.getPlugin().getPlayerData().players.get(player.getUuid().toString()).staffchat = !parent.getPlugin().getPlayerData().players.get(player.getUuid().toString()).staffchat;
            parent.getPlugin().saveData();
        } else {
            if (args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("true")) {
                parent.getPlugin().getPlayerData().players.get(player.getUuid().toString()).staffchat = true;
                parent.getPlugin().saveData();
            } else if (args[0].equalsIgnoreCase("off") || args[0].equalsIgnoreCase("false")) {
                parent.getPlugin().getPlayerData().players.get(player.getUuid().toString()).staffchat = false;
                parent.getPlugin().saveData();
            } else {
                if (sendFeedback) {
                    StringBuilder message = new StringBuilder();

                    for (int i = 0; i < args.length; i++) {
                        message.append(args[i]).append(" ");
                    }

                    for (ECPlayer player2 : parent.getPlugin().getServer().getOnlinePlayers()) {
                        if (player2.hasPermission(this.getPermission())) {
                            player2.sendMessage(ECTextFormatter.translateColors("&d&l[Staffchat] &r" + player.getDisplayName() + " &r> " + ECTextFormatter.stripColors(message.toString().trim())));
                        }
                    }
                }

                return;
            }
        }

        if (sendFeedback) {
            if (parent.getPlugin().getPlayerData().players.get(player.getUuid().toString()).staffchat) {
                player.sendMessage(ECTextFormatter.translateColors("&aTurned staffchat on"));
            } else {
                player.sendMessage(ECTextFormatter.translateColors("&aTurned staffchat off"));
            }
        }
    }

    @Override
    public List<String> tabComplete(ECPlayer player, String[] args) {
        if (args.length == 1) {
            return List.of("on", "off");
        } else {
            return List.of();
        }
    }
}