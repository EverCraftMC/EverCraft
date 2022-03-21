package com.kale_ko.evercraft.bungee.commands.server;

import java.util.List;
import com.kale_ko.evercraft.bungee.BungeePlugin;
import com.kale_ko.evercraft.bungee.Util;
import com.kale_ko.evercraft.bungee.commands.BungeeCommand;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class MaintenanceCommand extends BungeeCommand {
    public static Boolean underMaintenance = false;

    public MaintenanceCommand(String name, List<String> aliases, String permission) {
        super(name, aliases, permission);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!MaintenanceCommand.underMaintenance) {
            MaintenanceCommand.underMaintenance = true;

            for (ProxiedPlayer player : BungeePlugin.Instance.getProxy().getPlayers()) {
                if (!Util.hasPermission(player, "evercraft.features.bypassMaintenance")) {
                    player.disconnect(Util.stringToBungeeComponent(BungeePlugin.Instance.config.getString("messages.underMaintenance")));
                }
            }

            Util.sendMessage(sender, BungeePlugin.Instance.config.getString("messages.toggleMaintenance").replace("{status}", "on"));
        } else {
            MaintenanceCommand.underMaintenance = false;

            Util.sendMessage(sender, BungeePlugin.Instance.config.getString("messages.toggleMaintenance").replace("{status}", "off"));
        }
    }
}