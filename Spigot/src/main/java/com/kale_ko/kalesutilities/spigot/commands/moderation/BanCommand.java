package com.kale_ko.kalesutilities.spigot.commands.moderation;

import com.kale_ko.kalesutilities.spigot.Main;
import com.kale_ko.kalesutilities.spigot.Util;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BanCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (args.length > 1) {
            Player player = Main.Instance.getServer().getPlayer(args[0]);

            StringBuilder banMessageBuilder = new StringBuilder();

            for (Integer i = 1; i < args.length; i++) {
                banMessageBuilder.append(args[i] + " ");
            }

            String banMessage = banMessageBuilder.toString();

            Main.Instance.players.set(args[0] + ".banned", true);
            Main.Instance.players.set(args[0] + ".banMessage", Main.Instance.config.getString("messages.ban").replace("{player}", "You").replace("{moderator}", Util.getPlayerName(sender)).replace("{reason}", banMessage).replace("was", "are"));

            if (player != null) {
                player.kickPlayer(Main.Instance.config.getString("messages.ban").replace("{player}", "You").replace("{moderator}", Util.getPlayerName(sender)).replace("{reason}", banMessage).replace("was", "where"));

                Util.broadcastMessage(Main.Instance.config.getString("messages.ban").replace("{player}", Util.getPlayerName(player)).replace("{moderator}", Util.getPlayerName(sender)).replace("{reason}", banMessage));
            } else {
                Util.broadcastMessage(Main.Instance.config.getString("messages.ban").replace("{player}", args[0]).replace("{moderator}", Util.getPlayerName(sender)).replace("{reason}", banMessage));
            }
        } else {
            Util.sendMessage(sender, Main.Instance.config.getString("messages.usage").replace("{usage}", Main.Instance.getCommand("ban").getUsage()));
        }

        return true;
    }
}