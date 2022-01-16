package com.kale_ko.kalesutilities.commands.warps;

import com.kale_ko.kalesutilities.Main;
import com.kale_ko.kalesutilities.Util;
import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (args.length == 1 && sender instanceof Player player) {
            player.teleport(Main.Instance.warps.getSerializable(args[0], Location.class));

            Util.sendMessage(sender, Main.Instance.config.getString("messages.warped").replace("{warp}", args[0]));
        } else if (args.length == 1) {
            Util.sendMessage(sender, Main.Instance.config.getString("messages.noconsole"));
        } else {
            if (Util.hasPermission(sender, "kalesutilities.commands.staff.sudo")) {
                Player player = Main.Instance.getServer().getPlayer(args[0]);

                if (player != null) {
                    player.teleport(Main.Instance.warps.getSerializable(args[1], Location.class));

                    Util.sendMessage(sender, Main.Instance.config.getString("messages.warpedplayer").replace("{warp}", args[1]).replace("{player}", args[0]));
                    Util.sendMessage(player, Main.Instance.config.getString("messages.warped").replace("{warp}", args[1]));
                } else {
                    Util.sendMessage(sender, Main.Instance.config.getString("messages.playernotfound").replace("{player}", args[0]));
                }
            } else {
                Util.sendMessage(sender, Main.Instance.config.getString("messages.noperms").replace("{permission}", "kalesutilities.commands.staff.sudo"));
            }
        }

        return true;
    }
}