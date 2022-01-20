package com.kale_ko.kalesutilities.spigot.commands.warps;

import com.kale_ko.kalesutilities.spigot.Main;
import com.kale_ko.kalesutilities.spigot.Util;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetWarpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            Main.Instance.warps.set(args[0], player.getLocation());

            Main.Instance.warps.save();

            Util.sendMessage(sender, Main.Instance.config.getString("messages.setwarp").replace("{warp}", args[0]));
        } else {
            Util.sendMessage(sender, Main.Instance.config.getString("messages.noconsole"));
        }

        return true;
    }
}