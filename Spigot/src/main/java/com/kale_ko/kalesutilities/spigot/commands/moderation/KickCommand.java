package com.kale_ko.kalesutilities.spigot.commands.moderation;

import java.util.List;
import com.kale_ko.kalesutilities.spigot.Main;
import com.kale_ko.kalesutilities.spigot.Util;
import com.kale_ko.kalesutilities.spigot.commands.SpigotCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KickCommand extends SpigotCommand {
    public KickCommand(String name, String description, List<String> aliases, String usage, String permission) {
        super(name, description, aliases, usage, permission);
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (args.length > 1) {
            Player player = Main.Instance.getServer().getPlayer(args[0]);

            if (player != null) {
                StringBuilder kickMessageBuilder = new StringBuilder();

                for (Integer i = 1; i < args.length; i++) {
                    kickMessageBuilder.append(args[i] + " ");
                }

                String kickMessage = kickMessageBuilder.toString();

                player.kickPlayer(Main.Instance.config.getString("messages.kick").replace("{player}", "You").replace("{moderator}", Util.getPlayerName(sender)).replace("{reason}", kickMessage).replace("was", "where"));

                Util.broadcastMessage(Main.Instance.config.getString("messages.kick").replace("{player}", Util.getPlayerName(player)).replace("{moderator}", Util.getPlayerName(sender)).replace("{reason}", kickMessage));
            } else {
                Util.sendMessage(sender, Main.Instance.config.getString("messages.playernotfound").replace("{player}", args[0]));
            }
        } else {
            Util.sendMessage(sender, Main.Instance.config.getString("messages.usage").replace("{usage}", Main.Instance.getCommand("kick").getUsage()));
        }

        return true;
    }
}