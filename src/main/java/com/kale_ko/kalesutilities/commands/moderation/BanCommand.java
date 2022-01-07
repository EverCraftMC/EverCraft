package com.kale_ko.kalesutilities.commands.moderation;

import com.kale_ko.kalesutilities.Main;
import com.kale_ko.kalesutilities.Util;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

@SuppressWarnings("deprecation")
public class BanCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (Util.hasPermission(sender, "kalesutilities.ban")) {
            if (args.length > 1) {
                OfflinePlayer player = Main.Instance.getServer().getOfflinePlayer(args[0]);

                StringBuilder banMessageBuilder = new StringBuilder();

                for (Integer i = 1; i < args.length; i++) {
                    banMessageBuilder.append(args[i] + " ");
                }

                String banMessage = banMessageBuilder.toString();

                Main.Instance.players.getConfig().set("players." + player.getPlayer().getName() + ".banned", true);
                Main.Instance.players.getConfig().set("players." + player.getPlayer().getName() + ".banMessage", Main.Instance.config.getConfig().getString("messages.ban").replace("{player}", "You").replace("{moderator}", Util.getPlayerName(sender)).replace("{reason}", banMessage).replace("was", "are"));

                Main.Instance.players.save();

                if (Main.Instance.getServer().getPlayer(args[0]) != null) {
                    Main.Instance.getServer().getPlayer(args[0]).kickPlayer(Main.Instance.config.getConfig().getString("messages.ban").replace("{player}", "You").replace("{moderator}", Util.getPlayerName(sender)).replace("{reason}", banMessage).replace("was", "where"));

                    Util.broadcastMessage(Main.Instance.config.getConfig().getString("messages.ban").replace("{player}", Util.getPlayerName(Main.Instance.getServer().getPlayer(args[0]))).replace("{moderator}", Util.getPlayerName(sender)).replace("{reason}", banMessage));
                } else {
                    Util.broadcastMessage(Main.Instance.config.getConfig().getString("messages.ban").replace("{player}", args[0]).replace("{moderator}", Util.getPlayerName(sender)).replace("{reason}", banMessage));
                }
            } else {
                Util.sendMessage(sender, Main.Instance.config.getConfig().getString("messages.usage").replace("{usage}", Main.Instance.getCommand("ban").getUsage()));
            }
        } else {
            Util.sendMessage(sender, Main.Instance.config.getConfig().getString("messages.noperms").replace("{permission}", "kalesutilities.ban"));
        }

        return true;
    }
}