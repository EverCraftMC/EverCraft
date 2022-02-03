package com.kale_ko.kalesutilities.spigot.commands.staff;

import java.util.List;
import com.kale_ko.kalesutilities.spigot.SpigotPlugin;
import com.kale_ko.kalesutilities.spigot.Util;
import com.kale_ko.kalesutilities.spigot.commands.SpigotCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSpyCommand extends SpigotCommand {
    public CommandSpyCommand(String name, String description, List<String> aliases, String usage, String permission) {
        super(name, description, aliases, usage, permission);
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player player) {
            if (Util.hasMetadata(player, "receiveCommandSpy")) {
                if (Util.getMetadata(player, "receiveCommandSpy").asBoolean()) {
                    Util.setMetadata(player, "receiveCommandSpy", false);

                    Util.sendMessage(player, SpigotPlugin.Instance.config.getString("messages.togglecommandspy").replace("{value}", "off"));
                } else {
                    Util.setMetadata(player, "receiveCommandSpy", true);

                    Util.sendMessage(player, SpigotPlugin.Instance.config.getString("messages.togglecommandspy").replace("{value}", "on"));
                }
            } else {
                Util.setMetadata(player, "receiveCommandSpy", true);

                Util.sendMessage(player, SpigotPlugin.Instance.config.getString("messages.togglecommandspy").replace("{value}", "on"));
            }
        } else {
            Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.noconsole"));
        }

        return true;
    }
}