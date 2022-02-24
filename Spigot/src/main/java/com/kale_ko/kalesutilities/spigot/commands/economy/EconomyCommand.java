package com.kale_ko.kalesutilities.spigot.commands.economy;

import java.util.List;
import com.kale_ko.kalesutilities.spigot.SpigotPlugin;
import com.kale_ko.kalesutilities.spigot.Util;
import com.kale_ko.kalesutilities.spigot.commands.SpigotCommand;
import org.bukkit.command.CommandSender;

public class EconomyCommand extends SpigotCommand {
    public EconomyCommand(String name, String description, List<String> aliases, String usage, String permission) {
        super(name, description, aliases, usage, permission);
    }

    @Override
    public void run(CommandSender sender, String commandLabel, String[] args) {
        if (args[0].equalsIgnoreCase("give")) {
            SpigotPlugin.Instance.eco.depositPlayer(args[1], Float.parseFloat(args[2]));
        } else if (args[0].equalsIgnoreCase("take")) {
            SpigotPlugin.Instance.eco.withdrawPlayer(args[1], Float.parseFloat(args[2]));
        } else if (args[0].equalsIgnoreCase("set")) {
            SpigotPlugin.Instance.eco.setBalance(args[1], Float.parseFloat(args[2]));
        } else if (args[0].equalsIgnoreCase("reset")) {
            SpigotPlugin.Instance.eco.setBalance(args[1], 0);
        }

        Util.sendMessage(sender, "&aSuccessfully set " + args[1] + "'s balance to " + Util.formatCurrencyMin(SpigotPlugin.Instance.eco.getBalance(args[1])));
    }
}