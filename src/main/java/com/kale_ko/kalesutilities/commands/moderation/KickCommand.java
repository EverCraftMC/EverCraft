package com.kale_ko.kalesutilities.commands.moderation;

import com.kale_ko.kalesutilities.Main;
import com.kale_ko.kalesutilities.Util;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KickCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (args.length > 1) {
            Player player = Main.Instance.getServer().getPlayer(args[0]);

            if (player != null) {
                StringBuilder kickMessageBuilder = new StringBuilder();

                for (Integer i = 1; i < args.length; i++) {
                    kickMessageBuilder.append(args[i] + " ");
                }

                String kickMessage = kickMessageBuilder.toString();

                player.kickPlayer(Main.Instance.config.getString("messages.kick").replace("{player}", "You").replace("{moderator}", Util.getPlayerName(sender)).replace("{reason}", kickMessage).replace("was", "where"));

                Util.broadcastMessage(Main.Instance.config.getString("messages.kick").replace("{player}", Util.getPlayerName(player)).replace("{moderator}", Util.getPlayerName(sender)).replace("{reason}", kickMessage));
            } else {
                Util.sendMessage(sender, Main.Instance.config.getString("messages.playernotfound").replace("{player}", args[0]));
            }
        } else {
            Util.sendMessage(sender, Main.Instance.config.getString("messages.usage").replace("{usage}", Main.Instance.getCommand("kick").getUsage()));
        }

        return true;
    }
}