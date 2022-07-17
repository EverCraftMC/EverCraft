package io.github.evercraftmc.evercraft.bungee.commands.warp;

import java.util.Arrays;
import java.util.List;
import io.github.evercraftmc.evercraft.bungee.BungeeMain;
import io.github.evercraftmc.evercraft.bungee.commands.BungeeCommand;
import io.github.evercraftmc.evercraft.bungee.util.formatting.ComponentFormatter;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class HubCommand extends BungeeCommand {
    public HubCommand(String name, String description, List<String> aliases, String permission) {
        super(name, description, aliases, permission);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer player) {
            if (!player.getServer().getInfo().getName().equals(BungeeMain.getInstance().getProxy().getServerInfo(BungeeMain.getInstance().getProxy().getConfigurationAdapter().getListeners().iterator().next().getServerPriority().get(0)).getName())) {
                player.connect(BungeeMain.getInstance().getProxy().getServerInfo(BungeeMain.getInstance().getProxy().getConfigurationAdapter().getListeners().iterator().next().getServerPriority().get(0)));

                player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().warp.hub)));
            } else {
                player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().warp.alreadyConnected)));
            }
        } else {
            sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().error.noConsole)));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return Arrays.asList();
    }
}