package com.kale_ko.kalesutilities.commands;

import com.kale_ko.kalesutilities.Main;
import com.kale_ko.kalesutilities.Util;
import java.io.File;
import java.nio.file.Paths;
import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class WarpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (Util.hasPermission(sender, "kalesutilities.warp")) {
            File dataFolder = Main.Instance.getDataFolder();
            if (!dataFolder.exists()) {
                dataFolder.mkdir();
            }

            File dataFile = Paths.get(dataFolder.getAbsolutePath(), "warps.yml").toFile();

            YamlConfiguration data = YamlConfiguration.loadConfiguration(dataFile);

            if (sender instanceof Player player) {
                player.teleport(new Location(Main.Instance.getServer().getWorld(data.getString(args[0] + ".world")), data.getDouble(args[0] + ".x"), data.getDouble(args[0] + ".y"), data.getDouble(args[0] + ".z"), Float.parseFloat(data.getString(args[0] + ".yaw")), Float.parseFloat(data.getString(args[0] + ".pitch"))));

                Util.sendMessage(sender, Main.Instance.config.getString("messages.warped").replace("{warp}", args[0]));
            } else {
                Util.sendMessage(sender, Main.Instance.config.getString("messages.noconsole"));
            }
        } else {
            Util.sendMessage(sender, Main.Instance.config.getString("messages.noperms").replace("{permission}", "kalesutilities.warp"));
        }

        return true;
    }
}