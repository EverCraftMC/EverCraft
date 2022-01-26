package com.kale_ko.kalesutilities.bungee.commands.info;

import java.util.List;
import com.kale_ko.kalesutilities.bungee.Main;
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
                Main.Instance.getProxy().getPluginManager().dispatchCommand(sender, "help");
            } else if (args[0].equalsIgnoreCase("reload")) {
                Main.Instance.reload();

                Util.sendMessage(sender, Main.Instance.config.getString("messages.reload"));
            } else {
                Util.sendMessage(sender, Main.Instance.config.getString("messages.invalidCommand").replace("{command}", "/" + this.getName() + " " + String.join(" ", args)));
            }
        } else {
            Util.sendMessage(sender, Main.Instance.config.getString("messages.usage").replace("{usage}", ""));
        }
    }
}