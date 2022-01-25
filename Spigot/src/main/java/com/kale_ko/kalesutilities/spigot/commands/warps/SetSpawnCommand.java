package com.kale_ko.kalesutilities.spigot.commands.warps;

import java.util.List;
import com.kale_ko.kalesutilities.spigot.Main;
import com.kale_ko.kalesutilities.spigot.Util;
import com.kale_ko.kalesutilities.spigot.commands.SpigotCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCommand extends SpigotCommand {
    public SetSpawnCommand(String name, String description, List<String> aliases, String usage, String permission) {
        super(name, description, aliases, usage, permission);
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player player) {
            if (Main.Instance.spawn.getObject("overidespawn") == null) {
                Main.Instance.spawn.set("overidespawn", true);
            }

            Main.Instance.spawn.set(player.getWorld().getName(), player.getLocation());

            Main.Instance.spawn.save();

            Util.sendMessage(sender, Main.Instance.config.getString("messages.setspawn"));
        } else {
            Util.sendMessage(sender, Main.Instance.config.getString("messages.noconsole"));
        }

        return true;
    }
}