package com.kale_ko.kalesutilities.spigot.commands.info;

import com.kale_ko.kalesutilities.spigot.Main;
import com.kale_ko.kalesutilities.spigot.Util;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommandYamlParser;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class HelpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (args.length > 0) {
            Player player = Main.Instance.getServer().getPlayer(args[0]);

            if (player != null) {
                StringBuilder help = new StringBuilder();

                for (Plugin plugin : Main.Instance.getServer().getPluginManager().getPlugins()) {
                    for (org.bukkit.command.Command pluginCommand : PluginCommandYamlParser.parse(plugin)) {
                        if (pluginCommand.getPermission() == null || Util.hasPermission(player, pluginCommand.getPermission())) {
                            help.append(pluginCommand.getUsage() + " - " + pluginCommand.getDescription() + "\n");
                        }
                    }
                }

                Util.sendMessage(player, Main.Instance.config.getString("messages.help").replace("{commandList}", help.toString()));
            } else {
                Util.sendMessage(sender, Main.Instance.config.getString("messages.playernotfound").replace("{player}", args[0]));
            }
        } else {
            StringBuilder help = new StringBuilder();

            for (Plugin plugin : Main.Instance.getServer().getPluginManager().getPlugins()) {
                for (org.bukkit.command.Command pluginCommand : PluginCommandYamlParser.parse(plugin)) {
                    if (pluginCommand.getPermission() == null || Util.hasPermission(sender, pluginCommand.getPermission())) {
                        help.append(pluginCommand.getUsage() + " - " + pluginCommand.getDescription() + "\n");
                    }
                }
            }

            Util.sendMessage(sender, Main.Instance.config.getString("messages.help").replace("{commandList}", help.toString()));
        }

        return true;
    }
}