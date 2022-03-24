package com.kale_ko.evercraft.bungee.commands.player;

import java.util.Arrays;
import java.util.List;
import com.kale_ko.evercraft.bungee.BungeeMain;
import com.kale_ko.evercraft.bungee.commands.BungeeCommand;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class NickNameCommand extends BungeeCommand {
    public static final String name = "nickname";
    public static final String description = "Change your nickname";

    public static final List<String> aliases = Arrays.asList("nick");

    public static final String permission = "evercraft.commands.player.nickname";

    @Override
    public void run(CommandSender sender, String[] args) {
        if (args.length > 0) {
            if (sender instanceof ProxiedPlayer player) {
                StringBuilder nick = new StringBuilder();

                for (String arg : args) {
                    nick.append(arg + " ");
                }

                BungeeMain.getInstance().getPlayerData().set("players." + player.getUniqueId(), nick.substring(0, nick.length() - 1));
            } else {
                sender.sendMessage(new TextComponent(BungeeMain.getInstance().getPluginMessages().getString("error.noConsole")));
            }
        } else {
            sender.sendMessage(new TextComponent(BungeeMain.getInstance().getPluginMessages().getString("error.invalidArgs")));
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        return Arrays.asList();
    }
}