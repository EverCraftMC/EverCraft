package com.kale_ko.kales_utilities.commands;

import com.kale_ko.kales_utilities.Main;
import com.kale_ko.kales_utilities.Util;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SeenCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (args.length > 0) {

        } else {
            Util.sendMessage(sender, Main.Instance.config.getString("messages.usage").replace("{usage}", Main.Instance.getCommand("seen").getUsage()));
        }

        return true;
    }
}