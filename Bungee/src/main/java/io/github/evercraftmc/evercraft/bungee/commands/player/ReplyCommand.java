package io.github.evercraftmc.evercraft.bungee.commands.player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.github.evercraftmc.evercraft.bungee.BungeeMain;
import io.github.evercraftmc.evercraft.bungee.commands.BungeeCommand;
import io.github.evercraftmc.evercraft.bungee.util.formatting.ComponentFormatter;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ReplyCommand extends BungeeCommand {
    public Map<ProxiedPlayer, ProxiedPlayer> lastMessages = new HashMap<ProxiedPlayer, ProxiedPlayer>();

    public ReplyCommand(String name, String description, List<String> aliases, String permission) {
        super(name, description, aliases, permission);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer player) {
            if (args.length > 0) {
                ProxiedPlayer player2 = MessageCommand.lastMessages.get(player);

                if (player2 != null) {
                    StringBuilder message = new StringBuilder();

                    for (Integer i = 0; i < args.length; i++) {
                        message.append(args[i] + " ");
                    }

                    player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("chat.dm").replace("{player1}", "You").replace("{player2}", player2.getDisplayName()).replace("{message}", message.toString()))));
                    player2.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("chat.dm").replace("{player1}", player.getDisplayName()).replace("{player2}", "You").replace("{message}", message.toString()))));

                    if (lastMessages.containsKey(player)) {
                        lastMessages.remove(player);
                    }
                    lastMessages.put(player, player2);

                    if (lastMessages.containsKey(player2)) {
                        lastMessages.remove(player2);
                    }
                    lastMessages.put(player2, player);
                } else {
                    sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("chat.noReplyTo"))));
                }
            } else {
                sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("error.invalidArgs"))));
            }
        } else {
            sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("error.noConsole"))));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return Arrays.asList();
    }
}