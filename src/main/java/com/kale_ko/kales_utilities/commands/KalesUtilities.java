package com.kale_ko.kales_utilities.commands;

import com.kale_ko.kales_utilities.Main;
import com.kale_ko.kales_utilities.Util;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommandYamlParser;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.command.Commands;

@Commands({ @Command(name = "kalesutilities", desc = "The main plugin command for Kales Utilities", aliases = { "ks" }, usage = "/kalesutilities [help,reload]") })
public class KalesUtilities implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("help")) {
                StringBuilder help = new StringBuilder();

                for (org.bukkit.command.Command pluginCommand : PluginCommandYamlParser.parse(Main.Instance)) {
                    help.append(pluginCommand.getName() + " - " + pluginCommand.getDescription() + " - " + pluginCommand.getUsage() + "\n");
                }

                Util.sendMessage(sender, Main.Instance.config.getString("messages.help").replace("{commandList}", help.toString()));
            } else if (args[0].equalsIgnoreCase("reload")) {
                Main.Instance.reloadConfig();

                Util.sendMessage(sender, Main.Instance.config.getString("messages.reload"));
            } else {
                Util.sendMessage(sender, Main.Instance.config.getString("messages.invalidCommand").replace("{command}", "/" + label + " " + String.join(" ", args)));
            }
        } else {
            Util.sendMessage(sender, Main.Instance.config.getString("messages.usage").replace("{usage}", Main.Instance.getCommand("kalesutilities").getUsage()));
        }

        return true;
    }
}