package io.github.evercraftmc.moderation.commands;

import io.github.evercraftmc.core.api.commands.ECCommand;
import io.github.evercraftmc.core.api.server.player.ECPlayer;
import io.github.evercraftmc.core.impl.util.ECTextFormatter;
import io.github.evercraftmc.moderation.ModerationModule;
import java.util.List;

public class LockChatCommand implements ECCommand {
    protected final ModerationModule parent;

    public LockChatCommand(ModerationModule parent) {
        this.parent = parent;
    }

    @Override
    public String getName() {
        return "lockChat";
    }

    @Override
    public String getDescription() {
        return "Lock/unlock the chat";
    }

    @Override
    public List<String> getAlias() {
        return List.of();
    }

    @Override
    public String getPermission() {
        return "evercraft.moderation.commands.lockChat";
    }

    @Override
    public void run(ECPlayer player, String[] args, boolean sendFeedback) {
        if (args.length == 0) {
            parent.chatLocked = !parent.chatLocked;
        } else {
            if (args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("true")) {
                parent.chatLocked = true;
            } else if (args[0].equalsIgnoreCase("off") || args[0].equalsIgnoreCase("false")) {
                parent.chatLocked = false;
            } else {
                if (sendFeedback) {
                    player.sendMessage(ECTextFormatter.translateColors("&cWas expecting \"on\" or \"off\""));
                }

                return;
            }
        }

        if (sendFeedback) {
            if (parent.chatLocked) {
                parent.getPlugin().getServer().broadcastMessage(ECTextFormatter.translateColors("&aThe chat has been locked"));
            } else {
                parent.getPlugin().getServer().broadcastMessage(ECTextFormatter.translateColors("&aThe chat has been unlocked"));
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