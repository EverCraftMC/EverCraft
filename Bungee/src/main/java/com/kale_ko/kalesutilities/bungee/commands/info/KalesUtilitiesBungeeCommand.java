package com.kale_ko.kalesutilities.bungee.commands.info;

import java.util.List;
import com.kale_ko.kalesutilities.bungee.BungeePlugin;
import com.kale_ko.kalesutilities.bungee.Util;
import com.kale_ko.kalesutilities.bungee.commands.BungeeCommand;
import net.md_5.bungee.api.CommandSender;

public class KalesUtilitiesBungeeCommand extends BungeeCommand {
    public KalesUtilitiesBungeeCommand(String name, List<String> aliases, String permission) {
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