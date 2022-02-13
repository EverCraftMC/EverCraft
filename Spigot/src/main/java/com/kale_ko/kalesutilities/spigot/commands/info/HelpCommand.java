package com.kale_ko.kalesutilities.spigot.commands.info;

import java.util.List;
import com.kale_ko.kalesutilities.spigot.SpigotPlugin;
import com.kale_ko.kalesutilities.spigot.Util;
import com.kale_ko.kalesutilities.spigot.commands.SpigotCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommandYamlParser;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class HelpCommand extends SpigotCommand {
    public HelpCommand(String name, String description, List<String> aliases, String usage, String permission) {
        super(name, description, aliases, usage, permission);
    }

    @Override
    public void run(CommandSender sender, String label, String[] args) {
        if (args.length > 0) {
            Player player = SpigotPlugin.Instance.getServer().getPlayer(args[0]);

            if (player != null) {
                StringBuilder help = new StringBuilder();

                for (Plugin plugin : SpigotPlugin.Instance.getServer().getPluginManager().getPlugins()) {
                    for (org.bukkit.command.Command pluginCommand : PluginCommandYamlParser.parse(plugin)) {
                        if (pluginCommand.getPermission() == null || Util.hasPermission(player, pluginCommand.getPermission())) {
                            help.append(pluginCommand.getUsage() + " - " + pluginCommand.getDescription() + "\n");
                        }
                    }
                }

                Util.sendMessage(player, SpigotPlugin.Instance.config.getString("messages.help").replace("{commandList}", help.toString()));
            } else {
                Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.playernotfound").replace("{player}", args[0]));
            }
        } else {
            StringBuilder help = new StringBuilder();

            for (Plugin plugin : SpigotPlugin.Instance.getServer().getPluginManager().getPlugins()) {
                for (org.bukkit.command.Command pluginCommand : PluginCommandYamlParser.parse(plugin)) {
                    if (pluginCommand.getPermission() == null || Util.hasPermission(sender, pluginCommand.getPermission())) {
                        help.append(pluginCommand.getUsage() + " - " + pluginCommand.getDescription() + "\n");
                    }
                }
            }

            Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.help").replace("{commandList}", help.toString()));
        }
    }
}