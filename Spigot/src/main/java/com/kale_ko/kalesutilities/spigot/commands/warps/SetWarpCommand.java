package com.kale_ko.kalesutilities.spigot.commands.warps;

import java.util.List;
import com.kale_ko.kalesutilities.spigot.Main;
import com.kale_ko.kalesutilities.spigot.Util;
import com.kale_ko.kalesutilities.spigot.commands.SpigotCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetWarpCommand extends SpigotCommand {
    public SetWarpCommand(String name, String description, List<String> aliases, String usage, String permission) {
        super(name, description, aliases, usage, permission);
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player player) {
            Main.Instance.warps.set(args[0], player.getLocation());

            Main.Instance.warps.save();

            Util.sendMessage(sender, Main.Instance.config.getString("messages.setwarp").replace("{warp}", args[0]));
        } else {
            Util.sendMessage(sender, Main.Instance.config.getString("messages.noconsole"));
        }

        return true;
    }
}