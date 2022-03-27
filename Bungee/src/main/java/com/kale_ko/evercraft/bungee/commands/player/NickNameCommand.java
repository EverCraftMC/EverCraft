package com.kale_ko.evercraft.bungee.commands.player;

import java.util.Arrays;
import java.util.List;
import com.kale_ko.evercraft.bungee.BungeeMain;
import com.kale_ko.evercraft.bungee.commands.BungeeCommand;
import com.kale_ko.evercraft.shared.util.formatting.TextFormatter;
import net.luckperms.api.LuckPermsProvider;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class NickNameCommand extends BungeeCommand {
    public NickNameCommand(String name, String description, List<String> aliases, String permission) {
        super(name, description, aliases, permission);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (args.length > 0) {
            if (sender instanceof ProxiedPlayer player) {
                if (args[0].length() < 16) {
                    BungeeMain.getInstance().getData().set("players." + player.getUniqueId() + ".nickname", args[0]);

                    player.setDisplayName(TextFormatter.translateColors(LuckPermsProvider.get().getUserManager().getUser(player.getUniqueId()).getCachedData().getMetaData().getPrefix() + args[0]));

                    player.sendMessage(TextComponent.fromLegacyText(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("nickname").replace("{nickname}", args[0]))));
                } else {
                    sender.sendMessage(TextComponent.fromLegacyText(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("error.invalidArgs"))));
                }
            } else {
                sender.sendMessage(TextComponent.fromLegacyText(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("error.noConsole"))));
            }
        } else {
            sender.sendMessage(TextComponent.fromLegacyText(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("error.invalidArgs"))));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return Arrays.asList();
    }
}