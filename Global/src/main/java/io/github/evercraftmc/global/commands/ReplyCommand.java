package io.github.evercraftmc.global.commands;

import io.github.evercraftmc.core.api.commands.ECCommand;
import io.github.evercraftmc.core.api.server.player.ECPlayer;
import io.github.evercraftmc.core.impl.util.ECTextFormatter;
import io.github.evercraftmc.global.GlobalModule;
import java.util.List;

public class ReplyCommand implements ECCommand {
    protected final GlobalModule parent;

    public ReplyCommand(GlobalModule parent) {
        this.parent = parent;
    }

    @Override
    public String getName() {
        return "reply";
    }

    @Override
    public String getDescription() {
        return "Reply to the last player";
    }

    @Override
    public List<String> getAlias() {
        return List.of("r");
    }

    @Override
    public String getPermission() {
        return "evercraft.global.commands.message";
    }

    @Override
    public void run(ECPlayer player, String[] args, boolean sendFeedback) {
        if (args.length > 0) {
            ECPlayer player2 = parent.getPlugin().getServer().getOnlinePlayer(MessageCommand.lastMessaged.get(player));

            if (player2 != null) {
                String[] args2 = new String[args.length + 1];
                args2[0] = player2.getName();
                for (int i = 0; i < args.length; i++) {
                    args2[i + 1] = args[i];
                }

                parent.getPlugin().getServer().getCommandManager().get("message").run(player, args2, sendFeedback);
            } else {
                player.sendMessage(ECTextFormatter.translateColors("&cYou have not messaged anyone."));
            }
        } else if (sendFeedback) {
            player.sendMessage(ECTextFormatter.translateColors("&cYou must pass a username."));
        }
    }

    @Override
    public List<String> tabComplete(ECPlayer player, String[] args) {
        return List.of();
    }
}