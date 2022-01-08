package com.kale_ko.kalesutilities.commands.warps;

import com.kale_ko.kalesutilities.Main;
import com.kale_ko.kalesutilities.Util;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (Util.hasPermission(sender, "kalesutilities.warp")) {
            if (sender instanceof Player player) {
                player.teleport(Main.Instance.spawn.getLocation(args[0]));

                Util.sendMessage(sender, Main.Instance.config.getString("messages.warped").replace("{warp}", args[0]));
            } else {
                Util.sendMessage(sender, Main.Instance.config.getString("messages.noconsole"));
            }
        } else {
            Util.sendMessage(sender, Main.Instance.config.getString("messages.noperms").replace("{permission}", "kalesutilities.warp"));
        }

        return true;
    }
}