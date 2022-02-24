package com.kale_ko.kalesutilities.spigot.commands.economy;

import java.util.List;
import com.kale_ko.kalesutilities.spigot.SpigotPlugin;
import com.kale_ko.kalesutilities.spigot.Util;
import com.kale_ko.kalesutilities.spigot.commands.SpigotCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BalanceCommand extends SpigotCommand {
    public BalanceCommand(String name, String description, List<String> aliases, String usage, String permission) {
        super(name, description, aliases, usage, permission);
    }

    @Override
    public void run(CommandSender sender, String commandLabel, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player player) {
                Util.sendMessage(sender, "&aYour balance is currently " + Util.formatCurrencyMin(SpigotPlugin.Instance.eco.getBalance(player.getName())));
            } else {
                Util.sendMessage(sender, "&cYou not a player silly");
            }
        } else {
            Util.sendMessage(sender, "&a" + args[0] + "'s balance is currently " + Util.formatCurrencyMin(SpigotPlugin.Instance.eco.getBalance(args[0])));
        }
    }
}