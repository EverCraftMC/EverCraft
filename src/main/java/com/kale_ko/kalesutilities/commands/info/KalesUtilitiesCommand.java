package com.kale_ko.kalesutilities.commands.info;

import com.kale_ko.kalesutilities.KalesUtilities;
import com.kale_ko.kalesutilities.Util;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommandYamlParser;

public class KalesUtilitiesCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("help")) {
                StringBuilder help = new StringBuilder();

                for (org.bukkit.command.Command pluginCommand : PluginCommandYamlParser.parse(KalesUtilities.Instance)) {
                    help.append(pluginCommand.getName() + " - " + pluginCommand.getDescription() + " - " + pluginCommand.getUsage() + "\n");
                }

                Util.sendMessage(sender, KalesUtilities.Instance.config.getString("messages.help").replace("{commandList}", help.toString()));
            } else if (args[0].equalsIgnoreCase("reload")) {
                KalesUtilities.Instance.reloadConfig();

                Util.sendMessage(sender, KalesUtilities.Instance.config.getString("messages.reload"));
            } else {
                Util.sendMessage(sender, KalesUtilities.Instance.config.getString("messages.invalidCommand").replace("{command}", "/" + label + " " + String.join(" ", args)));
            }
        } else {
            Util.sendMessage(sender, KalesUtilities.Instance.config.getString("messages.usage").replace("{usage}", KalesUtilities.Instance.getCommand("kalesutilities").getUsage()));
        }

        return true;
    }
}