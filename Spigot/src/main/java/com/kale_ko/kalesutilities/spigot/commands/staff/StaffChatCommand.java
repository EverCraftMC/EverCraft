package com.kale_ko.kalesutilities.spigot.commands.staff;

import java.util.List;
import com.kale_ko.kalesutilities.spigot.SpigotPlugin;
import com.kale_ko.kalesutilities.spigot.Util;
import com.kale_ko.kalesutilities.spigot.commands.SpigotCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffChatCommand extends SpigotCommand {
    public StaffChatCommand(String name, String description, List<String> aliases, String usage, String permission) {
        super(name, description, aliases, usage, permission);
    }

    @Override
    public void run(CommandSender sender, String label, String[] args) {
        StringBuilder messageBuilder = new StringBuilder();

        for (Integer i = 0; i < args.length; i++) {
            messageBuilder.append(args[i] + " ");
        }

        String message = messageBuilder.toString();

        String senderName = "CONSOLE";
        if (sender instanceof Player player) {
            senderName = Util.getPlayerName(player);
        }

        Util.messageBungee("globalStaffChat", SpigotPlugin.Instance.config.getString("config.serverName"), SpigotPlugin.Instance.config.getString("messages.staffchat").replace("{player}", senderName).replace("{message}", message));

        for (Player player : SpigotPlugin.Instance.getServer().getOnlinePlayers()) {
            if (Util.hasPermission(player, "kalesutilities.commands.staff.staffchat")) {
                Util.sendMessage(player, SpigotPlugin.Instance.config.getString("messages.staffchat").replace("{player}", senderName).replace("{message}", message), true);
            }
        }
    }
}