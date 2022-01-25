package com.kale_ko.kalesutilities.spigot.commands.moderation;

import com.kale_ko.kalesutilities.spigot.Main;
import com.kale_ko.kalesutilities.spigot.Util;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class UnmuteCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (args.length > 0) {
            Main.Instance.players.set(args[0] + ".muted", null);
            Main.Instance.players.set(args[0] + ".mutedMessage", null);

            Util.broadcastMessage(Main.Instance.config.getString("messages.unmute").replace("{player}", args[0]).replace("{moderator}", Util.getPlayerName(sender)));
        } else {
            Util.sendMessage(sender, Main.Instance.config.getString("messages.usage").replace("{usage}", Main.Instance.getCommand("unmute").getUsage()));
        }

        return true;
    }
}