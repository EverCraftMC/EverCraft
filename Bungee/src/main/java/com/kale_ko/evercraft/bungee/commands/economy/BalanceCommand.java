package com.kale_ko.evercraft.bungee.commands.economy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.kale_ko.evercraft.bungee.BungeeMain;
import com.kale_ko.evercraft.bungee.commands.BungeeCommand;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BalanceCommand extends BungeeCommand {
    public static final String name = "balance";
    public static final String description = "Get your current balance";

    public static final List<String> aliases = Arrays.asList("bal");

    public static final String permission = "evercraft.commands.economy.balance";

    @Override
    public void run(CommandSender sender, String[] args) {
        if (args.length > 0) {
            ProxiedPlayer player = BungeeMain.getInstance().getProxy().getPlayer(args[0]);

            if (player != null) {
                sender.sendMessage(new TextComponent(BungeeMain.getInstance().getPluginMessages().getString("economy.balance").replace("{balance}", BungeeMain.getInstance().getEconomy().getBalance(player.getUniqueId()).toString())));
            } else {
                sender.sendMessage(new TextComponent(BungeeMain.getInstance().getPluginMessages().getString("error.playerNotFound").replace("{player}", args[0])));
            }
        } else {
            if (sender instanceof ProxiedPlayer player) {
                player.sendMessage(new TextComponent(BungeeMain.getInstance().getPluginMessages().getString("economy.balance").replace("{balance}", BungeeMain.getInstance().getEconomy().getBalance(player.getUniqueId()).toString())));
            }
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<String>();

        if (args.length == 0) {
            for (ProxiedPlayer player : BungeeMain.getInstance().getProxy().getPlayers()) {
                list.add(player.getName());
            }
        } else {
            return Arrays.asList();
        }

        return BungeeCommand.StringUtil.copyPartialMatches(args[args.length - 1], list);
    }
}