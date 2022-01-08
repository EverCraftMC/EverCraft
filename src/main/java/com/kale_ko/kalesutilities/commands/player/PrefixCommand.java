package com.kale_ko.kalesutilities.commands.player;

import com.kale_ko.kalesutilities.Main;
import com.kale_ko.kalesutilities.Util;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PrefixCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (Util.hasPermission(sender, "kalesutilities.setprefix")) {
            if (args.length > 0) {
                if (sender instanceof Player player) {
                    Main.Instance.players.set("players." + player.getPlayer().getName() + ".prefix", args[0]);

                    Main.Instance.players.save();

                    Util.sendMessage(sender, Main.Instance.config.getString("messages.setprefix"));
                    Util.updatePlayerName(player);
                } else {
                    Util.sendMessage(sender, Main.Instance.config.getString("messages.noconsole"));
                }
            } else {
                Util.sendMessage(sender, Main.Instance.config.getString("messages.usage").replace("{usage}", Main.Instance.getCommand("prefix").getUsage()));
            }
        } else {
            Util.sendMessage(sender, Main.Instance.config.getString("messages.noperms").replace("{permission}", "kalesutilities.setprefix"));
        }

        return true;
    }
}