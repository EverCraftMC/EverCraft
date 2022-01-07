package com.kale_ko.kalesutilities.commands.warps;

import com.kale_ko.kalesutilities.Main;
import com.kale_ko.kalesutilities.Util;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (Util.hasPermission(sender, "kalesutilities.setspawn")) {
            if (sender instanceof Player player) {
                if (Main.Instance.spawn.getConfig().get("overidespawn") == null) Main.Instance.spawn.getConfig().set("overidespawn", true);

                Main.Instance.spawn.getConfig().set(player.getWorld().getName() + ".x", player.getLocation().getX());
                Main.Instance.spawn.getConfig().set(player.getWorld().getName() + ".y", player.getLocation().getY());
                Main.Instance.spawn.getConfig().set(player.getWorld().getName() + ".z", player.getLocation().getZ());
                Main.Instance.spawn.getConfig().set(player.getWorld().getName() + ".pitch", player.getLocation().getPitch());
                Main.Instance.spawn.getConfig().set(player.getWorld().getName() + ".yaw", player.getLocation().getYaw());

                Main.Instance.spawn.save();

                Util.sendMessage(sender, Main.Instance.config.getConfig().getString("messages.setspawn"));
            } else {
                Util.sendMessage(sender, Main.Instance.config.getConfig().getString("messages.noconsole"));
            }
        } else {
            Util.sendMessage(sender, Main.Instance.config.getConfig().getString("messages.usage").replace("{usage}", Main.Instance.getCommand("setspawn").getUsage()));
        }

        return true;
    }
}