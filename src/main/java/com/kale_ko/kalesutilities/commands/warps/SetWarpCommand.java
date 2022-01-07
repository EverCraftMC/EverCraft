package com.kale_ko.kalesutilities.commands.warps;

import com.kale_ko.kalesutilities.Main;
import com.kale_ko.kalesutilities.Util;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetWarpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (Util.hasPermission(sender, "kalesutilities.setwarp")) {
            if (sender instanceof Player player) {
                Main.Instance.warps.getConfig().set(args[0] + ".world", player.getLocation().getWorld().getName());
                Main.Instance.warps.getConfig().set(args[0] + ".x", player.getLocation().getX());
                Main.Instance.warps.getConfig().set(args[0] + ".y", player.getLocation().getY());
                Main.Instance.warps.getConfig().set(args[0] + ".z", player.getLocation().getZ());
                Main.Instance.warps.getConfig().set(args[0] + ".pitch", player.getLocation().getPitch());
                Main.Instance.warps.getConfig().set(args[0] + ".yaw", player.getLocation().getYaw());

                Main.Instance.warps.save();

                Util.sendMessage(sender, Main.Instance.config.getConfig().getString("messages.setwarp").replace("{warp}", args[0]));
            } else {
                Util.sendMessage(sender, Main.Instance.config.getConfig().getString("messages.noconsole"));
            }
        } else {
            Util.sendMessage(sender, Main.Instance.config.getConfig().getString("messages.noperms").replace("{permission}", "kalesutilities.setwarp"));
        }

        return true;
    }
}