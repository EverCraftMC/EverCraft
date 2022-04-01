package io.github.evercraftmc.evercraft.bungee.commands.staff;

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

public class CommandSpyCommand extends BungeeCommand {
    public CommandSpyCommand(String name, String description, List<String> aliases, String permission) {
        super(name, description, aliases, permission);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer player) {
            if (args.length == 0) {
                if (BungeeMain.getInstance().getData().getBoolean("players." + player.getUniqueId() + ".commandspy")) {
                    BungeeMain.getInstance().getData().set("players." + player.getUniqueId() + ".commandspy", false);

                    player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("commandspy").replace("{value}", "off"))));
                } else {
                    BungeeMain.getInstance().getData().set("players." + player.getUniqueId() + ".commandspy", true);

                    player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("commandspy").replace("{value}", "on"))));
                }
            } else {
                if (args[0].equalsIgnoreCase("on")) {
                    BungeeMain.getInstance().getData().set("players." + player.getUniqueId() + ".commandspy", true);

                    player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("commandspy").replace("{value}", "on"))));
                } else if (args[0].equalsIgnoreCase("off")) {
                    BungeeMain.getInstance().getData().set("players." + player.getUniqueId() + ".commandspy", false);

                    player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("commandspy").replace("{value}", "off"))));
                }
            }
        } else {
            sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("error.noConsole"))));
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