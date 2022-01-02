package com.kale_ko.kalesutilities.commands.info;

import com.kale_ko.kalesutilities.KalesUtilities;
import com.kale_ko.kalesutilities.Util;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StaffCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (Util.hasPermission(sender, "kalesutilities.staff")) {
            Util.sendMessage(sender, KalesUtilities.Instance.config.getString("config.staff"));
        } else {
            Util.sendMessage(sender, KalesUtilities.Instance.config.getString("messages.noperms").replace("{permission}", "kalesutilities.staff"));
        }

        return true;
    }
}