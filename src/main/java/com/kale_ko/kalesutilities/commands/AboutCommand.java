package com.kale_ko.kalesutilities.commands;

import com.kale_ko.kalesutilities.Main;
import com.kale_ko.kalesutilities.Util;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AboutCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (Util.hasPermission(sender, "kalesutilities.about")) {
            Util.sendMessage(sender, Main.Instance.config.getString("config.about"));
        } else {
            Util.sendMessage(sender, Main.Instance.config.getString("messages.noperms").replace("{permission}", "kalesutilities.about"));
        }

        return true;
    }
}