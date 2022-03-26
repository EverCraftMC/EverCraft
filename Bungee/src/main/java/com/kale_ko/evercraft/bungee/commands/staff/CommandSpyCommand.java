package com.kale_ko.evercraft.bungee.commands.staff;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.kale_ko.evercraft.bungee.BungeeMain;
import com.kale_ko.evercraft.bungee.commands.BungeeCommand;
import com.kale_ko.evercraft.shared.util.formatting.TextFormatter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class CommandSpyCommand extends BungeeCommand {
    public static final String name = "commandspy";
    public static final String description = "Toggles your command spy";

    public static final List<String> aliases = Arrays.asList("cs");

    public static final String permission = "evercraft.commands.staff.commandspy";

    @Override
    public void run(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer player) {
            if (args.length == 0) {
                if (BungeeMain.getInstance().getPlayerData().getBoolean("players." + player.getUniqueId() + ".commandspy")) {
                    BungeeMain.getInstance().getPlayerData().set("players." + player.getUniqueId() + ".commandspy", false);

                    player.sendMessage(TextComponent.fromLegacyText(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("commandspy").replace("{value}", "off"))));
                } else {
                    BungeeMain.getInstance().getPlayerData().set("players." + player.getUniqueId() + ".commandspy", true);

                    player.sendMessage(TextComponent.fromLegacyText(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("commandspy").replace("{value}", "on"))));
                }
            } else {
                if (args[0].equalsIgnoreCase("on")) {
                    BungeeMain.getInstance().getPlayerData().set("players." + player.getUniqueId() + ".commandspy", true);

                    player.sendMessage(TextComponent.fromLegacyText(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("commandspy").replace("{value}", "on"))));
                } else if (args[0].equalsIgnoreCase("off")) {
                    BungeeMain.getInstance().getPlayerData().set("players." + player.getUniqueId() + ".commandspy", false);

                    player.sendMessage(TextComponent.fromLegacyText(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("commandspy").replace("{value}", "off"))));
                }
            }
        } else {
            sender.sendMessage(TextComponent.fromLegacyText(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("error.noConsole"))));
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<String>();

        if (args.length == 0) {
            list.add("on");
            list.add("off");
        } else {
            return Arrays.asList();
        }

        if (args.length > 0) {
            return BungeeCommand.StringUtil.copyPartialMatches(args[args.length - 1], list);
        } else {
            return list;
        }
    }
}