package com.kale_ko.kalesutilities.commands.staff;

import com.kale_ko.kalesutilities.Main;
import com.kale_ko.kalesutilities.Util;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffChatCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (Util.hasPermission(sender, "kalesutilities.staffchat")) {
            StringBuilder messageBuilder = new StringBuilder();

            for (Integer i = 0; i < args.length; i++) {
                messageBuilder.append(args[i] + " ");
            }

            String message = messageBuilder.toString();

            if (message.contains("bed") || message.contains("beb") || message.contains("wars")) {
                Main.Instance.getServer().dispatchCommand(sender, "kick " + sender.getName() + " Bedwars");
            }

            String senderName = "CONSOLE";
            if (sender instanceof Player player) {
                senderName = Util.getPlayerName(player);
            }

            for (Player player : Main.Instance.getServer().getOnlinePlayers()) {
                if (Util.hasPermission(player, "kalesutilities.staffchat")) {
                    Util.sendMessage(player, Main.Instance.config.getString("messages.staffchat").replace("{player}", senderName).replace("{message}", message), true);
                }
            }
        } else {
            Util.sendMessage(sender, Main.Instance.config.getString("messages.noperms").replace("{permission}", "kalesutilities.staffchat"));
        }

        return true;
    }
}