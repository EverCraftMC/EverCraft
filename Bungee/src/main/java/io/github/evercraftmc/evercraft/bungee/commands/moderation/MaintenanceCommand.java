package io.github.evercraftmc.evercraft.bungee.commands.moderation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import io.github.evercraftmc.evercraft.bungee.BungeeMain;
import io.github.evercraftmc.evercraft.bungee.commands.BungeeCommand;
import io.github.evercraftmc.evercraft.bungee.util.formatting.ComponentFormatter;
import io.github.evercraftmc.evercraft.shared.util.StringUtils;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class MaintenanceCommand extends BungeeCommand {
    public MaintenanceCommand(String name, String description, List<String> aliases, String permission) {
        super(name, description, aliases, permission);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (args.length == 0) {
            if (BungeeMain.getInstance().getPluginData().getParsed().maintenance) {
                BungeeMain.getInstance().getPluginData().getParsed().maintenance = false;

                BungeeMain.getInstance().getProxy().broadcast(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().moderation.maintenance.toggle.replace("{value}", "off"))));
            } else {
                BungeeMain.getInstance().getPluginData().getParsed().maintenance = true;

                for (ProxiedPlayer player2 : BungeeMain.getInstance().getProxy().getPlayers()) {
                    if (!player2.hasPermission("evercraft.commands.moderation.bypassMaintenance")) {
                        player2.disconnect(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().moderation.maintenance.kick)));
                    }
                }

                BungeeMain.getInstance().getProxy().broadcast(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().moderation.maintenance.toggle.replace("{value}", "on"))));
            }
        } else {
            if (args[0].equalsIgnoreCase("on")) {
                BungeeMain.getInstance().getPluginData().getParsed().maintenance = true;

                for (ProxiedPlayer player2 : BungeeMain.getInstance().getProxy().getPlayers()) {
                    if (!player2.hasPermission("evercraft.commands.moderation.bypassMaintenance")) {
                        player2.disconnect(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().moderation.maintenance.kick)));
                    }
                }

                BungeeMain.getInstance().getProxy().broadcast(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().moderation.maintenance.toggle.replace("{value}", "on"))));
            } else if (args[0].equalsIgnoreCase("off")) {
                BungeeMain.getInstance().getPluginData().getParsed().maintenance = false;

                BungeeMain.getInstance().getProxy().broadcast(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().moderation.maintenance.toggle.replace("{value}", "off"))));
            }
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<String>();

        if (args.length == 1) {
            list.add("on");
            list.add("off");
        } else {
            return Arrays.asList();
        }

        if (args.length > 0) {
            return StringUtils.matchPartial(args[args.length - 1], list);
        } else {
            return list;
        }
    }
}