package io.github.evercraftmc.moderation.commands;

import io.github.evercraftmc.core.api.commands.ECCommand;
import io.github.evercraftmc.core.api.server.player.ECPlayer;
import io.github.evercraftmc.core.impl.util.ECTextFormatter;
import io.github.evercraftmc.moderation.ModerationModule;
import java.util.ArrayList;
import java.util.List;

public class ClearChatCommand implements ECCommand {
    protected final ModerationModule parent;

    public ClearChatCommand(ModerationModule parent) {
        this.parent = parent;
    }

    @Override
    public String getName() {
        return "clearChat";
    }

    @Override
    public String getDescription() {
        return "Clear the chat";
    }

    @Override
    public List<String> getAlias() {
        return List.of("cleanChat");
    }

    @Override
    public String getPermission() {
        return "evercraft.moderation.commands.clearChat";
    }

    @Override
    public void run(ECPlayer player, String[] args, boolean sendFeedback) {
        if (sendFeedback) {
            for (ECPlayer player2 : parent.getPlugin().getServer().getOnlinePlayers()) {
                if (!player2.hasPermission(this.getPermission())) {
                    player2.sendMessage("\n".repeat(1200));
                } else {
                    player2.sendMessage(ECTextFormatter.translateColors("&aThe chat has been cleared."));
                }
            }
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