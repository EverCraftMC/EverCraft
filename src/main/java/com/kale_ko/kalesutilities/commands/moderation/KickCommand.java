package com.kale_ko.kalesutilities.commands.moderation;

import com.kale_ko.kalesutilities.KalesUtilities;
import com.kale_ko.kalesutilities.Util;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KickCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (Util.hasPermission(sender, "kalesutilities.kick")) {
            if (args.length > 1) {
                Player player = KalesUtilities.Instance.getServer().getPlayer(args[0]);

                if (player != null) {
                    StringBuilder kickMessageBuilder = new StringBuilder();

                    for (Integer i = 1; i < args.length; i++) {
                        kickMessageBuilder.append(args[i] + " ");
                    }

                    String kickMessage = kickMessageBuilder.toString();

                    player.kickPlayer(KalesUtilities.Instance.config.getString("messages.kick").replace("{player}", "You").replace("{moderator}", Util.getPlayerName(sender)).replace("{reason}", kickMessage).replace("was", "where"));

                    Util.broadcastMessage(KalesUtilities.Instance.config.getString("messages.kick").replace("{player}", Util.getPlayerName(player)).replace("{moderator}", Util.getPlayerName(sender)).replace("{reason}", kickMessage));
                } else {
                    Util.sendMessage(sender, KalesUtilities.Instance.config.getString("messages.playernotfound").replace("{player}", args[0]));
                }
            } else {
                Util.sendMessage(sender, KalesUtilities.Instance.config.getString("messages.usage").replace("{usage}", KalesUtilities.Instance.getCommand("kick").getUsage()));
            }
        } else {
            Util.sendMessage(sender,
                    KalesUtilities.Instance.config.getString("messages.noperms").replace("{permission}", "kalesutilities.kick"));
        }

        return true;
    }
}