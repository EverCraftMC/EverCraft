package com.kale_ko.evercraft.bungee.commands.warp;

import java.util.Arrays;
import java.util.List;
import com.kale_ko.evercraft.bungee.BungeeMain;
import com.kale_ko.evercraft.shared.PluginCommand;
import com.kale_ko.evercraft.shared.util.formatting.TextFormatter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class ServerCommand extends Command implements PluginCommand, TabExecutor {
    public static final String name = "server";
    public static final String description = "Go the the {serer} server";

    public static final List<String> aliases = Arrays.asList();

    public static final String permission = "evercraft.commands.warp.server.{sever}";

    private String server;

    public ServerCommand(String name) {
        super(name, permission.replace("{server}", name), aliases.toArray(new String[] {}));
        this.setPermissionMessage(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("error.noPerms").replace("{permission}", permission)));

        this.server = name;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission(permission)) {
            this.run(sender, args);
        } else {
            sender.sendMessage(TextComponent.fromLegacyText(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("error.noPerms").replace("{permission}", permission))));
        }
    }

    public void run(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer player) {
            if (player.getServer() != BungeeMain.getInstance().getProxy().getServerInfo(this.server)) {
                player.connect(BungeeMain.getInstance().getProxy().getServerInfo(this.server));

                player.sendMessage(TextComponent.fromLegacyText(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("warp.server").replace("{server}", this.server))));
            } else {
                player.sendMessage(TextComponent.fromLegacyText(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("warp.alreadyConnected"))));
            }
        } else {
            sender.sendMessage(TextComponent.fromLegacyText(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("error.noConsole"))));
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        return tabComplete(sender, args);
    }

    public List<String> tabComplete(CommandSender sender, String[] args) {
        return Arrays.asList();
    }

    public ServerCommand register() {
        BungeeMain.getInstance().getProxy().getPluginManager().registerCommand(BungeeMain.getInstance(), this);

        return this;
    }

    public void unregister() {
        BungeeMain.getInstance().getProxy().getPluginManager().unregisterCommand(this);
    }
}