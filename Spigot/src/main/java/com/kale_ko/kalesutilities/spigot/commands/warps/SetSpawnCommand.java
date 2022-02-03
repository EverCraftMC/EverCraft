package com.kale_ko.kalesutilities.spigot.commands.warps;

import java.util.List;
import com.kale_ko.kalesutilities.spigot.SpigotPlugin;
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
            if (SpigotPlugin.Instance.spawn.getObject("overidespawn") == null) {
                SpigotPlugin.Instance.spawn.set("overidespawn", true);
            }

            SpigotPlugin.Instance.spawn.set(player.getWorld().getName(), player.getLocation());

            SpigotPlugin.Instance.spawn.save();

            Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.setspawn"));
        } else {
            Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.noconsole"));
        }

        return true;
    }
}