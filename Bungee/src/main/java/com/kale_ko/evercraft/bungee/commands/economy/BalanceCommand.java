package com.kale_ko.evercraft.bungee.commands.economy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.kale_ko.evercraft.bungee.BungeeMain;
import com.kale_ko.evercraft.bungee.commands.BungeeCommand;
import com.kale_ko.evercraft.shared.util.StringUtils;
import com.kale_ko.evercraft.shared.util.formatting.TextFormatter;
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
                sender.sendMessage(TextComponent.fromLegacyText(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("economy.otherBalance").replace("{player}", player.getDisplayName()).replace("{balance}", BungeeMain.getInstance().getEconomy().getBalance(player.getUniqueId()).toString()))));
            } else {
                sender.sendMessage(TextComponent.fromLegacyText(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("error.playerNotFound").replace("{player}", args[0]))));
            }
        } else {
            if (sender instanceof ProxiedPlayer player) {
                player.sendMessage(TextComponent.fromLegacyText(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("economy.yourBalance").replace("{balance}", BungeeMain.getInstance().getEconomy().getBalance(player.getUniqueId()).toString()))));
            } else {
                sender.sendMessage(TextComponent.fromLegacyText(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("error.noConsole"))));
            }
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<String>();

        if (args.length == 0) {
            for (ProxiedPlayer player : BungeeMain.getInstance().getProxy().getPlayers()) {
                list.add(player.getName());
            }
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