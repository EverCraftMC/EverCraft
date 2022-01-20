package com.kale_ko.kalesutilities.spigot.commands.staff;

import com.kale_ko.kalesutilities.spigot.Main;
import com.kale_ko.kalesutilities.spigot.Util;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSpyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (Util.hasMetadata(player, "receiveCommandSpy")) {
                if (Util.getMetadata(player, "receiveCommandSpy").asBoolean()) {
                    Util.setMetadata(player, "receiveCommandSpy", false);

                    Util.sendMessage(player, Main.Instance.config.getString("messages.togglecommandspy").replace("{value}", "off"));
                } else {
                    Util.setMetadata(player, "receiveCommandSpy", true);

                    Util.sendMessage(player, Main.Instance.config.getString("messages.togglecommandspy").replace("{value}", "on"));
                }
            } else {
                Util.setMetadata(player, "receiveCommandSpy", true);

                Util.sendMessage(player, Main.Instance.config.getString("messages.togglecommandspy").replace("{value}", "on"));
            }
        } else {
            Util.sendMessage(sender, Main.Instance.config.getString("messages.noconsole"));
        }

        return true;
    }
}