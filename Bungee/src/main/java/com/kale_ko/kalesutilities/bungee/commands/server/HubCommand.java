package com.kale_ko.kalesutilities.bungee.commands.server;

import java.util.List;
import com.kale_ko.kalesutilities.bungee.BungeePlugin;
import com.kale_ko.kalesutilities.bungee.Util;
import com.kale_ko.kalesutilities.bungee.commands.BungeeCommand;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class HubCommand extends BungeeCommand {
    public HubCommand(String name, List<String> aliases, String permission) {
        super(name, aliases, permission);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            if (sender instanceof ProxiedPlayer player) {
                ServerInfo target = BungeePlugin.Instance.getProxy().getServerInfo(BungeePlugin.Instance.config.getString("config.mainServer"));

                if (player.getServer().getInfo() != target) {
                    player.connect(target);
                } else {
                    Util.sendMessage(player, BungeePlugin.Instance.config.getString("messages.allreadyconnected"));
                }
            } else {
                Util.sendMessage(sender, BungeePlugin.Instance.config.getString("messages.noconsole"));
            }
        } else {
            if (Util.hasPermission(sender, "kalesutilities.commands.staff.sudo")) {
                ProxiedPlayer player = BungeePlugin.Instance.getProxy().getPlayer(args[0]);

                if (player != null) {
                    ServerInfo target = BungeePlugin.Instance.getProxy().getServerInfo(BungeePlugin.Instance.config.getString("config.mainServer"));

                    if (player.getServer().getInfo() != target) {
                        player.connect(target);
                    } else {
                        Util.sendMessage(player, BungeePlugin.Instance.config.getString("messages.allreadyconnected"));
                    }
                } else {
                    Util.sendMessage(sender, BungeePlugin.Instance.config.getString("messages.playernotfound").replace("{player}", args[0]));
                }
            }
        }
    }
}