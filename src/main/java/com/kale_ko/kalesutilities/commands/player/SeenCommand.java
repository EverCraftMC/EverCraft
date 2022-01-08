package com.kale_ko.kalesutilities.commands.player;

import com.kale_ko.kalesutilities.Main;
import com.kale_ko.kalesutilities.Util;
import java.util.Date;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SeenCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (Util.hasPermission(sender, "kalesutilities.seen")) {
            if (args.length > 0) {
                if (Main.Instance.getServer().getPlayer(args[0]) != null) {
                    Util.sendMessage(sender, Main.Instance.config.getString("messages.playeronline").replace("{player}", args[0]));
                } else {
                    long lastOnline = Main.Instance.players.getLong("players." + args[0] + ".lastOnline");

                    if (lastOnline != 0) {
                        Util.sendMessage(sender, Main.Instance.config.getString("messages.lastonline").replace("{player}", args[0]).replace("{time}", new Date(lastOnline).toString()));
                    } else {
                        Util.sendMessage(sender, Main.Instance.config.getString("messages.playernotfound").replace("{player}", args[0]));
                    }
                }
            } else {
                Util.sendMessage(sender, Main.Instance.config.getString("messages.usage").replace("{usage}", Main.Instance.getCommand("seen").getUsage()));
            }
        } else {
            Util.sendMessage(sender, Main.Instance.config.getString("messages.noperms").replace("{permission}", "kalesutilities.seen"));
        }

        return true;
    }
}