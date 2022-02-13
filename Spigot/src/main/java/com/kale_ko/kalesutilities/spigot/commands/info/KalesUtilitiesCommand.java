package com.kale_ko.kalesutilities.spigot.commands.info;

import java.util.List;
import com.kale_ko.kalesutilities.spigot.SpigotPlugin;
import com.kale_ko.kalesutilities.spigot.Util;
import com.kale_ko.kalesutilities.spigot.commands.SpigotCommand;
import org.bukkit.command.CommandSender;

public class KalesUtilitiesCommand extends SpigotCommand {
    public KalesUtilitiesCommand(String name, String description, List<String> aliases, String usage, String permission) {
        super(name, description, aliases, usage, permission);
    }

    @Override
    public void run(CommandSender sender, String label, String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("help")) {
                SpigotPlugin.Instance.getServer().dispatchCommand(sender, "help");
            } else if (args[0].equalsIgnoreCase("reload")) {
                SpigotPlugin.Instance.reload();

                Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.reload"));
            } else {
                Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.invalidCommand").replace("{command}", "/" + label + " " + String.join(" ", args)));
            }
        } else {
            Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.usage").replace("{usage}", SpigotPlugin.Instance.getCommand("kalesutilities").getUsage()));
        }
    }
}