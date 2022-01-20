package com.kale_ko.kalesutilities.spigot.commands.player;

import com.kale_ko.kalesutilities.spigot.Main;
import com.kale_ko.kalesutilities.spigot.Util;
import java.util.Date;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SeenCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (args.length > 0) {
            if (Main.Instance.getServer().getPlayer(args[0]) != null) {
                Util.sendMessage(sender, Main.Instance.config.getString("messages.playeronline").replace("{player}", args[0]));
            } else {
                long lastOnline = Main.Instance.seen.getLong(args[0]);

                if (lastOnline != 0) {
                    Util.sendMessage(sender, Main.Instance.config.getString("messages.lastonline").replace("{player}", args[0]).replace("{time}", new Date(lastOnline).toString()));
                } else {
                    Util.sendMessage(sender, Main.Instance.config.getString("messages.playernotfound").replace("{player}", args[0]));
                }
            }
        } else {
            Util.sendMessage(sender, Main.Instance.config.getString("messages.usage").replace("{usage}", Main.Instance.getCommand("seen").getUsage()));
        }

        return true;
    }
}