package com.kale_ko.kalesutilities.spigot.commands.player;

import com.kale_ko.kalesutilities.spigot.Main;
import com.kale_ko.kalesutilities.spigot.Util;
import com.kale_ko.kalesutilities.spigot.commands.SpigotCommand;
import java.util.Date;
import java.util.List;
import org.bukkit.command.CommandSender;

public class SeenCommand extends SpigotCommand {
    public SeenCommand(String name, String description, List<String> aliases, String usage, String permission) {
        super(name, description, aliases, usage, permission);
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (args.length > 0) {
            if (Main.Instance.getServer().getPlayer(args[0]) != null) {
                Util.sendMessage(sender, Main.Instance.config.getString("messages.playeronline").replace("{player}", args[0]));
            } else {
                long lastOnline = Main.Instance.players.getLong(args[0] + ".lastseen");

                if (lastOnline != 0) {
                    Util.sendMessage(sender, Main.Instance.config.getString("messages.lastonline").replace("{player}", args[0]).replace("{time}", new Date(lastOnline).toString()));
                } else {
                    Util.sendMessage(sender, Main.Instance.config.getString("messages.playernotfound").replace("{player}", args[0]));
                }
            }
        } else {
            Util.sendMessage(sender, Main.Instance.config.getString("messages.usage").replace("{usage}", Main.Instance.getCommand("seen").getUsage()));
        }

        return true;
    }
}