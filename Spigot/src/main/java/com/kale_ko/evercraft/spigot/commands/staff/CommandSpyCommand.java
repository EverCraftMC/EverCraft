package com.kale_ko.evercraft.spigot.commands.staff;

import java.util.List;
import com.kale_ko.evercraft.spigot.SpigotPlugin;
import com.kale_ko.evercraft.spigot.Util;
import com.kale_ko.evercraft.spigot.commands.SpigotCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSpyCommand extends SpigotCommand {
    public CommandSpyCommand(String name, String description, List<String> aliases, String usage, String permission) {
        super(name, description, aliases, usage, permission);
    }

    @Override
    public void run(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player player) {
            if (SpigotPlugin.Instance.players.getBoolean(args[0] + ".receiveCommandSpy")) {
                SpigotPlugin.Instance.players.set(args[0] + ".receiveCommandSpy", false);

                Util.sendMessage(player, SpigotPlugin.Instance.config.getString("messages.togglecommandspy").replace("{value}", "off"));
            } else {
                SpigotPlugin.Instance.players.set(args[0] + ".receiveCommandSpy", true);

                Util.sendMessage(player, SpigotPlugin.Instance.config.getString("messages.togglecommandspy").replace("{value}", "on"));
            }
        } else {
            Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.noconsole"));
        }
    }
}