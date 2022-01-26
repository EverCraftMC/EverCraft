package com.kale_ko.kalesutilities.bungee.commands.server;

import java.util.List;
import com.kale_ko.kalesutilities.bungee.Main;
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
                ServerInfo target = Main.Instance.getProxy().getServerInfo(Main.Instance.config.getString("config.mainServer"));

                if (player.getServer().getInfo() != target) {
                    player.connect(target);
                } else {
                    Util.sendMessage(player, Main.Instance.config.getString("messages.allreadyconnected"));
                }
            } else {
                Util.sendMessage(sender, Main.Instance.config.getString("messages.noconsole"));
            }
        } else {
            if (Util.hasPermission(sender, "kalesutilities.commands.staff.sudo")) {
                ProxiedPlayer player = Main.Instance.getProxy().getPlayer(args[0]);

                if (player != null) {
                    ServerInfo target = Main.Instance.getProxy().getServerInfo(Main.Instance.config.getString("config.mainServer"));

                    if (player.getServer().getInfo() != target) {
                        player.connect(target);
                    } else {
                        Util.sendMessage(player, Main.Instance.config.getString("messages.allreadyconnected"));
                    }
                } else {
                    Util.sendMessage(sender, Main.Instance.config.getString("messages.playernotfound").replace("{player}", args[0]));
                }
            }
        }
    }
}