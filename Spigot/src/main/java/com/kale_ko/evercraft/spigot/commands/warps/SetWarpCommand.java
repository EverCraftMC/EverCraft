package com.kale_ko.evercraft.spigot.commands.warps;

import java.util.List;
import com.kale_ko.evercraft.spigot.SpigotPlugin;
import com.kale_ko.evercraft.spigot.Util;
import com.kale_ko.evercraft.spigot.commands.SpigotCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetWarpCommand extends SpigotCommand {
    public SetWarpCommand(String name, String description, List<String> aliases, String usage, String permission) {
        super(name, description, aliases, usage, permission);
    }

    @Override
    public void run(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player player) {
            SpigotPlugin.Instance.warps.set(args[0], player.getLocation());

            SpigotPlugin.Instance.warps.save();

            Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.setwarp").replace("{warp}", args[0]));
        } else {
            Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.noconsole"));
        }
    }
}