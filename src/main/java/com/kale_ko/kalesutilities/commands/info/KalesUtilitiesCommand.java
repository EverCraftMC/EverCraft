package com.kale_ko.kalesutilities.commands.info;

import com.kale_ko.kalesutilities.Main;
import com.kale_ko.kalesutilities.Util;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class KalesUtilitiesCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("help")) {
                Main.Instance.getServer().dispatchCommand(sender, "help");
            } else if (args[0].equalsIgnoreCase("reload")) {
                Main.Instance.reload();

                Util.sendMessage(sender, Main.Instance.config.getString("messages.reload"));
            } else {
                Util.sendMessage(sender, Main.Instance.config.getString("messages.invalidCommand").replace("{command}", "/" + label + " " + String.join(" ", args)));
            }
        } else {
            Util.sendMessage(sender, Main.Instance.config.getString("messages.usage").replace("{usage}", Main.Instance.getCommand("kalesutilities").getUsage()));
        }

        return true;
    }
}