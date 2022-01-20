package com.kale_ko.kalesutilities.spigot.commands.moderation;

import com.kale_ko.kalesutilities.spigot.Main;
import com.kale_ko.kalesutilities.spigot.Util;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MuteCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (args.length > 1) {
            Player player = Main.Instance.getServer().getPlayer(args[0]);

            StringBuilder muteMessageBuilder = new StringBuilder();

            for (Integer i = 1; i < args.length; i++) {
                muteMessageBuilder.append(args[i] + " ");
            }

            String muteMessage = muteMessageBuilder.toString();

            Main.Instance.players.set(player.getPlayer().getName() + ".muted", true);
            Main.Instance.players.set(player.getPlayer().getName() + ".mutedMessage", Main.Instance.config.getString("messages.mute").replace("{player}", "You").replace("{moderator}", Util.getPlayerName(sender)).replace("{reason}", muteMessage).replace("was", "are"));

            Main.Instance.players.save();

            if (Main.Instance.getServer().getPlayer(args[0]) != null) {
                Util.broadcastMessage(Main.Instance.config.getString("messages.mute").replace("{player}", Util.getPlayerName(Main.Instance.getServer().getPlayer(args[0]))).replace("{moderator}", Util.getPlayerName(sender)).replace("{reason}", muteMessage));
            } else {
                Util.broadcastMessage(Main.Instance.config.getString("messages.mute").replace("{player}", args[0]).replace("{moderator}", Util.getPlayerName(sender)).replace("{reason}", muteMessage));
            }
        }

        return true;
    }
}