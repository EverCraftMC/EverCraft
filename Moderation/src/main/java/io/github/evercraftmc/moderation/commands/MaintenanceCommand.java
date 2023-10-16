package io.github.evercraftmc.moderation.commands;

import io.github.evercraftmc.core.api.commands.ECCommand;
import io.github.evercraftmc.core.api.server.player.ECPlayer;
import io.github.evercraftmc.core.impl.util.ECTextFormatter;
import io.github.evercraftmc.moderation.ModerationModule;
import java.util.List;

public class MaintenanceCommand implements ECCommand {
    protected final ModerationModule parent;

    public MaintenanceCommand(ModerationModule parent) {
        this.parent = parent;
    }

    @Override
    public String getName() {
        return "maintenance";
    }

    @Override
    public String getDescription() {
        return "Turn maintenance on/off";
    }

    @Override
    public List<String> getAlias() {
        return List.of();
    }

    @Override
    public String getPermission() {
        return "evercraft.moderation.commands.maintenance";
    }

    @Override
    public void run(ECPlayer player, String[] args, boolean sendFeedback) {
        if (args.length == 0) {
            parent.getPlugin().getPlayerData().maintenance = !parent.getPlugin().getPlayerData().maintenance;
        } else {
            if (args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("true")) {
                parent.getPlugin().getPlayerData().maintenance = true;
            } else if (args[0].equalsIgnoreCase("off") || args[0].equalsIgnoreCase("false")) {
                parent.getPlugin().getPlayerData().maintenance = false;
            } else {
                if (sendFeedback) {
                    player.sendMessage(ECTextFormatter.translateColors("&cWas expecting \"on\" or \"off\""));
                }

                return;
            }
        }

        if (sendFeedback) {
            if (parent.getPlugin().getPlayerData().maintenance) {
                parent.getPlugin().getServer().broadcastMessage(ECTextFormatter.translateColors("&aMaintenance mode has been enabled"));
            } else {
                parent.getPlugin().getServer().broadcastMessage(ECTextFormatter.translateColors("&aMaintenance mode has been disabled"));
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