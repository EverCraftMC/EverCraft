package com.kale_ko.evercraft.bungee.commands.economy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.kale_ko.evercraft.bungee.BungeeMain;
import com.kale_ko.evercraft.bungee.commands.BungeeCommand;
import com.kale_ko.evercraft.shared.util.StringUtils;
import com.kale_ko.evercraft.shared.util.formatting.TextFormatter;
import com.kale_ko.evercraft.shared.util.player.PlayerResolver;
import com.kale_ko.evercraft.shared.util.player.SimplePlayer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class EconomyCommand extends BungeeCommand {
    public EconomyCommand(String name, String description, List<String> aliases, String permission) {
        super(name, description, aliases, permission);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (args.length >= 3) {
            SimplePlayer player = PlayerResolver.getPlayer(BungeeMain.getInstance().getData(), args[1]);

            if (player != null) {
                if (args[0].equalsIgnoreCase("set")) {
                    if (args.length >= 3) {
                        try {
                            BungeeMain.getInstance().getEconomy().setBalance(player.getUniqueId(), Float.parseFloat(args[2]));
                        } catch (NumberFormatException e) {
                            sender.sendMessage(TextComponent.fromLegacyText(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("error.invalidArgs"))));

                            return;
                        }
                    } else {
                        sender.sendMessage(TextComponent.fromLegacyText(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("error.invalidArgs"))));

                        return;
                    }
                } else if (args[0].equalsIgnoreCase("deposit")) {
                    if (args.length >= 3) {
                        try {
                            BungeeMain.getInstance().getEconomy().deposit(player.getUniqueId(), Float.parseFloat(args[2]));
                        } catch (NumberFormatException e) {
                            sender.sendMessage(TextComponent.fromLegacyText(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("error.invalidArgs"))));

                            return;
                        }
                    } else {
                        sender.sendMessage(TextComponent.fromLegacyText(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("error.invalidArgs"))));

                        return;
                    }
                } else if (args[0].equalsIgnoreCase("withdraw")) {
                    if (args.length >= 3) {
                        try {
                            BungeeMain.getInstance().getEconomy().withdraw(player.getUniqueId(), Float.parseFloat(args[2]));
                        } catch (NumberFormatException e) {
                            sender.sendMessage(TextComponent.fromLegacyText(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("error.invalidArgs"))));

                            return;
                        }
                    } else {
                        sender.sendMessage(TextComponent.fromLegacyText(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("error.invalidArgs"))));

                        return;
                    }
                } else {
                    sender.sendMessage(TextComponent.fromLegacyText(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("error.invalidArgs"))));

                    return;
                }

                sender.sendMessage(TextComponent.fromLegacyText(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("economy.economy").replace("{player}", player.getDisplayName()).replace("{balance}", BungeeMain.getInstance().getEconomy().getBalance(player.getUniqueId()).toString()))));
            } else {
                sender.sendMessage(TextComponent.fromLegacyText(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("error.playerNotFound").replace("{player}", args[0]))));
            }
        } else {
            sender.sendMessage(TextComponent.fromLegacyText(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("error.invalidArgs"))));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<String>();

        if (args.length == 1) {
            list.add("set");
            list.add("deposit");
            list.add("withdraw");
        } else if (args.length == 2) {
            for (ProxiedPlayer player : BungeeMain.getInstance().getProxy().getPlayers()) {
                list.add(player.getName());
            }
        } else if (args.length == 3) {
            list.add("0.01");
            list.add("0.1");
            list.add("1");
            list.add("10");
            list.add("100");
            list.add("1000");
            list.add("10000");
            list.add("100000");
            list.add("1000000");
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