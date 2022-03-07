package com.kale_ko.evercraft.bungee.commands.info;

import java.util.List;
import com.kale_ko.evercraft.bungee.BungeePlugin;
import com.kale_ko.evercraft.bungee.Util;
import com.kale_ko.evercraft.bungee.commands.BungeeCommand;
import net.md_5.bungee.api.CommandSender;

public class EverCraftBungeeCommand extends BungeeCommand {
    public EverCraftBungeeCommand(String name, List<String> aliases, String permission) {
        super(name, aliases, permission);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("help")) {
                BungeePlugin.Instance.getProxy().getPluginManager().dispatchCommand(sender, "help");
            } else if (args[0].equalsIgnoreCase("reload")) {
                BungeePlugin.Instance.reload();

                Util.sendMessage(sender, BungeePlugin.Instance.config.getString("messages.reload"));
            } else {
                Util.sendMessage(sender, BungeePlugin.Instance.config.getString("messages.invalidCommand").replace("{command}", "/" + this.getName() + " " + String.join(" ", args)));
            }
        } else {
            Util.sendMessage(sender, BungeePlugin.Instance.config.getString("messages.usage").replace("{usage}", ""));
        }
    }
}