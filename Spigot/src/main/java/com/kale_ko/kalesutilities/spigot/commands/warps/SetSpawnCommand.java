package com.kale_ko.kalesutilities.spigot.commands.warps;

import com.kale_ko.kalesutilities.spigot.Main;
import com.kale_ko.kalesutilities.spigot.Util;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
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