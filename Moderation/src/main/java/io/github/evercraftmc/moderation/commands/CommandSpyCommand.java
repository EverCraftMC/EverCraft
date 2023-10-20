package io.github.evercraftmc.moderation.commands;

import io.github.evercraftmc.core.api.commands.ECCommand;
import io.github.evercraftmc.core.api.server.player.ECPlayer;
import io.github.evercraftmc.core.impl.util.ECTextFormatter;
import io.github.evercraftmc.moderation.ModerationModule;
import java.util.List;

public class CommandSpyCommand implements ECCommand {
    protected final ModerationModule parent;

    public CommandSpyCommand(ModerationModule parent) {
        this.parent = parent;
    }

    @Override
    public String getName() {
        return "commandSpy";
    }

    @Override
    public String getDescription() {
        return "Toggle command spy";
    }

    @Override
    public List<String> getAlias() {
        return List.of("cs");
    }

    @Override
    public String getPermission() {
        return "evercraft.moderation.commands.commandSpy";
    }

    @Override
    public void run(ECPlayer player, String[] args, boolean sendFeedback) {
        if (args.length == 0) {
            parent.getPlugin().getPlayerData().players.get(player.getUuid().toString()).commandSpy = !parent.getPlugin().getPlayerData().players.get(player.getUuid().toString()).commandSpy;
            parent.getPlugin().saveData();
        } else {
            if (args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("true")) {
                parent.getPlugin().getPlayerData().players.get(player.getUuid().toString()).commandSpy = true;
                parent.getPlugin().saveData();
            } else if (args[0].equalsIgnoreCase("off") || args[0].equalsIgnoreCase("false")) {
                parent.getPlugin().getPlayerData().players.get(player.getUuid().toString()).commandSpy = false;
                parent.getPlugin().saveData();
            } else {
                if (sendFeedback) {
                    player.sendMessage(ECTextFormatter.translateColors("&cWas expecting \"on\" or \"off\""));
                }

                return;
            }
        }

        if (sendFeedback) {
            if (parent.getPlugin().getPlayerData().players.get(player.getUuid().toString()).commandSpy) {
                player.sendMessage(ECTextFormatter.translateColors("&aTurned command spy on"));
            } else {
                player.sendMessage(ECTextFormatter.translateColors("&aTurned command spy off"));
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