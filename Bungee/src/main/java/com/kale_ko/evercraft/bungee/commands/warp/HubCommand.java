package com.kale_ko.evercraft.bungee.commands.warp;

import java.util.Arrays;
import java.util.List;
import com.kale_ko.evercraft.bungee.BungeeMain;
import com.kale_ko.evercraft.bungee.commands.BungeeCommand;
import com.kale_ko.evercraft.shared.util.formatting.TextFormatter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class HubCommand extends BungeeCommand {
    public static final String name = "hub";
    public static final String description = "Sends you to the hub/lobby server";

    public static final List<String> aliases = Arrays.asList("lobby");

    public static final String permission = "evercraft.commands.warp.hub";

    @Override
    public void run(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer player) {
            if (player.getServer() != BungeeMain.getInstance().getProxy().getServerInfo(BungeeMain.getInstance().getProxy().getConfigurationAdapter().getListeners().iterator().next().getServerPriority().get(0))) {
                player.connect(BungeeMain.getInstance().getProxy().getServerInfo(BungeeMain.getInstance().getProxy().getConfigurationAdapter().getListeners().iterator().next().getServerPriority().get(0)));

                player.sendMessage(TextComponent.fromLegacyText(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("warp.hub"))));
            } else {
                player.sendMessage(TextComponent.fromLegacyText(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("warp.alreadyConnected"))));
            }
        } else {
            sender.sendMessage(TextComponent.fromLegacyText(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("error.noConsole"))));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return Arrays.asList();
    }
}