package com.kale_ko.kalesutilities.commands.warps;

import com.kale_ko.kalesutilities.Main;
import com.kale_ko.kalesutilities.Util;
import com.kale_ko.kalesutilities.Config;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class SetSpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (Util.hasPermission(sender, "kalesutilities.setspawn")) {
            Config config = Config.load("spawn.yml");
            YamlConfiguration data = config.getConfig();

            if (sender instanceof Player player) {
                data.set(player.getWorld().getName() + ".x", player.getLocation().getX());
                data.set(player.getWorld().getName() + ".y", player.getLocation().getY());
                data.set(player.getWorld().getName() + ".z", player.getLocation().getZ());
                data.set(player.getWorld().getName() + ".pitch", player.getLocation().getPitch());
                data.set(player.getWorld().getName() + ".yaw", player.getLocation().getYaw());

                config.save();

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