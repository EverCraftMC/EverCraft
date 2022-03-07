package com.kale_ko.evercraft.spigot.commands.moderation;

import java.util.List;
import com.kale_ko.evercraft.spigot.SpigotPlugin;
import com.kale_ko.evercraft.spigot.Util;
import com.kale_ko.evercraft.spigot.commands.SpigotCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BanCommand extends SpigotCommand {
    public BanCommand(String name, String description, List<String> aliases, String usage, String permission) {
        super(name, description, aliases, usage, permission);
    }

    @Override
    public void run(CommandSender sender, String label, String[] args) {
        if (args.length > 1) {
            Player player = SpigotPlugin.Instance.getServer().getPlayer(args[0]);

            StringBuilder banMessageBuilder = new StringBuilder();

            for (Integer i = 1; i < args.length; i++) {
                banMessageBuilder.append(args[i] + " ");
            }

            String banMessage = banMessageBuilder.toString();

            SpigotPlugin.Instance.players.set(args[0] + ".banned", true);
            SpigotPlugin.Instance.players.set(args[0] + ".banMessage", SpigotPlugin.Instance.config.getString("messages.ban").replace("{player}", "You").replace("{moderator}", Util.getPlayerName(sender)).replace("{reason}", banMessage).replace("was", "are"));

            if (player != null) {
                player.kickPlayer(SpigotPlugin.Instance.config.getString("messages.ban").replace("{player}", "You").replace("{moderator}", Util.getPlayerName(sender)).replace("{reason}", banMessage).replace("was", "where"));

                Util.broadcastMessage(SpigotPlugin.Instance.config.getString("messages.ban").replace("{player}", Util.getPlayerName(player)).replace("{moderator}", Util.getPlayerName(sender)).replace("{reason}", banMessage));
            } else {
                Util.broadcastMessage(SpigotPlugin.Instance.config.getString("messages.ban").replace("{player}", args[0]).replace("{moderator}", Util.getPlayerName(sender)).replace("{reason}", banMessage));
            }
        } else {
            Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.usage").replace("{usage}", SpigotPlugin.Instance.getCommand("ban").getUsage()));
        }
    }
}