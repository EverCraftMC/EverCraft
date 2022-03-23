package com.kale_ko.evercraft.bungee.commands.economy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.kale_ko.evercraft.bungee.BungeeMain;
import com.kale_ko.evercraft.bungee.commands.BungeeCommand;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class EconomyCommand extends BungeeCommand {
    public static final String name = "economy";
    public static final String description = "Modify someones balance";

    public static final List<String> aliases = Arrays.asList("eco");

    public static final String permission = "evercraft.commands.economy.economy";

    @Override
    public void run(CommandSender sender, String[] args) {
        if (args.length > 0) {
            ProxiedPlayer player = BungeeMain.getInstance().getProxy().getPlayer(args[0]);

            if (player != null) {
                if (args[0].equalsIgnoreCase("set")) {
                    if (args.length == 2) {
                        try {
                            BungeeMain.getInstance().getEconomy().setBalance(player.getUniqueId(), Float.parseFloat(args[1]));
                        } catch (NumberFormatException e) {
                            sender.sendMessage(new TextComponent(BungeeMain.getInstance().getPluginMessages().getString("error.invalidArgs")));

                            return;
                        }
                    } else {
                        sender.sendMessage(new TextComponent(BungeeMain.getInstance().getPluginMessages().getString("error.invalidArgs")));

                        return;
                    }
                } else if (args[0].equalsIgnoreCase("deposit")) {
                    if (args.length == 2) {
                        try {
                            BungeeMain.getInstance().getEconomy().deposit(player.getUniqueId(), Float.parseFloat(args[1]));
                        } catch (NumberFormatException e) {
                            sender.sendMessage(new TextComponent(BungeeMain.getInstance().getPluginMessages().getString("error.invalidArgs")));

                            return;
                        }
                    } else {
                        sender.sendMessage(new TextComponent(BungeeMain.getInstance().getPluginMessages().getString("error.invalidArgs")));

                        return;
                    }
                } else if (args[0].equalsIgnoreCase("withdraw")) {
                    if (args.length == 2) {
                        try {
                            BungeeMain.getInstance().getEconomy().withdraw(player.getUniqueId(), Float.parseFloat(args[1]));
                        } catch (NumberFormatException e) {
                            sender.sendMessage(new TextComponent(BungeeMain.getInstance().getPluginMessages().getString("error.invalidArgs")));

                            return;
                        }
                    } else {
                        sender.sendMessage(new TextComponent(BungeeMain.getInstance().getPluginMessages().getString("error.invalidArgs")));

                        return;
                    }
                } else {
                    sender.sendMessage(new TextComponent(BungeeMain.getInstance().getPluginMessages().getString("error.invalidArgs")));

                    return;
                }

                sender.sendMessage(new TextComponent(BungeeMain.getInstance().getPluginMessages().getString("economy.economy").replace("{player}", player.getName()).replace("{balance}", BungeeMain.getInstance().getEconomy().getBalance(player.getUniqueId()).toString())));
            } else {
                sender.sendMessage(new TextComponent(BungeeMain.getInstance().getPluginMessages().getString("error.playerNotFound").replace("{player}", args[0])));
            }
        } else {
            sender.sendMessage(new TextComponent(BungeeMain.getInstance().getPluginMessages().getString("error.invalidArgs")));
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
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
            return BungeeCommand.StringUtil.copyPartialMatches(args[args.length - 1], list);
        } else {
            return list;
        }
    }
}