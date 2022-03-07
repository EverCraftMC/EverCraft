package com.kale_ko.evercraft.spigot.commands.moderation;

import java.util.List;
import com.kale_ko.evercraft.spigot.SpigotPlugin;
import com.kale_ko.evercraft.spigot.Util;
import com.kale_ko.evercraft.spigot.commands.SpigotCommand;
import org.bukkit.command.CommandSender;

public class UnbanCommand extends SpigotCommand {
    public UnbanCommand(String name, String description, List<String> aliases, String usage, String permission) {
        super(name, description, aliases, usage, permission);
    }

    @Override
    public void run(CommandSender sender, String label, String[] args) {
        if (args.length > 0) {
            SpigotPlugin.Instance.players.set(args[0] + ".banned", null);
            SpigotPlugin.Instance.players.set(args[0] + ".banMessage", null);

            Util.broadcastMessage(SpigotPlugin.Instance.config.getString("messages.unban").replace("{player}", args[0]).replace("{moderator}", Util.getPlayerName(sender)));
        } else {
            Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.usage").replace("{usage}", SpigotPlugin.Instance.getCommand("unban").getUsage()));
        }
    }
}