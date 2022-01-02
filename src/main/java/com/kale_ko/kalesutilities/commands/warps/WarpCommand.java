package com.kale_ko.kalesutilities.commands.warps;

import com.kale_ko.kalesutilities.Main;
import com.kale_ko.kalesutilities.Util;
import com.kale_ko.kalesutilities.Config;
import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class WarpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (Util.hasPermission(sender, "kalesutilities.warp")) {
            YamlConfiguration data = Config.load("warps.yml").getConfig();

            if (sender instanceof Player player) {
                player.teleport(new Location(Main.Instance.getServer().getWorld(data.getString(args[0] + ".world")), data.getDouble(args[0] + ".x"), data.getDouble(args[0] + ".y"), data.getDouble(args[0] + ".z"), Float.parseFloat(data.getString(args[0] + ".yaw")), Float.parseFloat(data.getString(args[0] + ".pitch"))));

                Util.sendMessage(sender, Main.Instance.config.getConfig().getString("messages.warped").replace("{warp}", args[0]));
            } else {
                Util.sendMessage(sender, Main.Instance.config.getConfig().getString("messages.noconsole"));
            }
        } else {
            Util.sendMessage(sender, Main.Instance.config.getConfig().getString("messages.noperms").replace("{permission}", "kalesutilities.warp"));
        }

        return true;
    }
}