package com.kale_ko.kalesutilities.spigot.commands.moderation;

import java.util.List;
import com.kale_ko.kalesutilities.spigot.Main;
import com.kale_ko.kalesutilities.spigot.Util;
import com.kale_ko.kalesutilities.spigot.commands.SpigotCommand;
import org.bukkit.command.CommandSender;

public class UnmuteCommand extends SpigotCommand {
    public UnmuteCommand(String name, String description, List<String> aliases, String usage, String permission) {
        super(name, description, aliases, usage, permission);
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (args.length > 0) {
            Main.Instance.players.set(args[0] + ".muted", null);
            Main.Instance.players.set(args[0] + ".mutedMessage", null);

            Util.broadcastMessage(Main.Instance.config.getString("messages.unmute").replace("{player}", args[0]).replace("{moderator}", Util.getPlayerName(sender)));
        } else {
            Util.sendMessage(sender, Main.Instance.config.getString("messages.usage").replace("{usage}", Main.Instance.getCommand("unmute").getUsage()));
        }

        return true;
    }
}