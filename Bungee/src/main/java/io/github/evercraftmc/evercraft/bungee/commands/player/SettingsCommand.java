package io.github.evercraftmc.evercraft.bungee.commands.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import io.github.evercraftmc.evercraft.bungee.BungeeMain;
import io.github.evercraftmc.evercraft.bungee.commands.BungeeCommand;
import io.github.evercraftmc.evercraft.bungee.util.formatting.ComponentFormatter;
import io.github.evercraftmc.evercraft.shared.PluginData;
import io.github.evercraftmc.evercraft.shared.util.StringUtils;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class SettingsCommand extends BungeeCommand {
    public SettingsCommand(String name, String description, List<String> aliases, String permission) {
        super(name, description, aliases, permission);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer player) {
            if (args.length > 1) {
                if (args[0].equalsIgnoreCase("messaging")) {
                    if (args[1].equalsIgnoreCase("everyone")) {
                        BungeeMain.getInstance().getPluginData().getParsed().players.get(player.getUniqueId().toString()).settings.messaging = PluginData.Player.Settings.MessageSetting.EVERYONE;
                        BungeeMain.getInstance().getPluginData().save();

                        player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().settings.replace("{setting}", "messaging").replace("{value}", "everyone"))));
                    } else if (args[1].equalsIgnoreCase("friends")) {
                        BungeeMain.getInstance().getPluginData().getParsed().players.get(player.getUniqueId().toString()).settings.messaging = PluginData.Player.Settings.MessageSetting.FRIENDS;
                        BungeeMain.getInstance().getPluginData().save();

                        player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().settings.replace("{setting}", "messaging").replace("{value}", "friends"))));
                    } else if (args[1].equalsIgnoreCase("noone")) {
                        BungeeMain.getInstance().getPluginData().getParsed().players.get(player.getUniqueId().toString()).settings.messaging = PluginData.Player.Settings.MessageSetting.NOONE;
                        BungeeMain.getInstance().getPluginData().save();

                        player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().settings.replace("{setting}", "messaging").replace("{value}", "noone"))));
                    } else {
                        player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().error.invalidArgs)));
                    }
                } else {
                    player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().error.invalidArgs)));
                }
            } else {
                player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().error.invalidArgs)));
            }
        } else {
            sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().error.noConsole)));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<String>();

        if (args.length == 1) {
            list.add("messaging");
        } else if (args.length == 2 && (args[0].equalsIgnoreCase("messaging"))) {
            list.add("everyone");
            list.add("friends");
            list.add("noone");
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