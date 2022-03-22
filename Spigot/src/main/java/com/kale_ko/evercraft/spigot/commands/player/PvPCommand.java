package com.kale_ko.evercraft.spigot.commands.player;

import java.util.List;
import com.kale_ko.evercraft.spigot.SpigotPlugin;
import com.kale_ko.evercraft.spigot.Util;
import com.kale_ko.evercraft.spigot.commands.SpigotCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PvPCommand extends SpigotCommand {
    public PvPCommand(String name, String description, List<String> aliases, String usage, String permission) {
        super(name, description, aliases, usage, permission);
    }

    @Override
    public void run(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player player) {
            if (SpigotPlugin.Instance.players.getBoolean(args[0] + ".enablePvP")) {
                SpigotPlugin.Instance.players.set(args[0] + ".enablePvP", false);

                Util.sendMessage(player, SpigotPlugin.Instance.config.getString("messages.togglepvp").replace("{value}", "off"));
            } else {
                SpigotPlugin.Instance.players.set(args[0] + ".enablePvP", true);

                Util.sendMessage(player, SpigotPlugin.Instance.config.getString("messages.togglepvp").replace("{value}", "on"));
            }
        } else {
            Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.noconsole"));
        }
    }
}