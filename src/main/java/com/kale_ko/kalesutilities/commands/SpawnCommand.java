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

public class SpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (Util.hasPermission(sender, "kalesutilities.spawn")) {
            File dataFolder = Main.Instance.getDataFolder();
            if (!dataFolder.exists()) {
                dataFolder.mkdir();
            }

            File dataFile = Paths.get(dataFolder.getAbsolutePath(), "spawn.yml").toFile();

            YamlConfiguration data = YamlConfiguration.loadConfiguration(dataFile);

            if (args.length == 0 && sender instanceof Player player) {
                player.teleport(new Location(player.getWorld(), data.getDouble(player.getWorld().getName() + ".x"), data.getDouble(player.getWorld().getName() + ".y"), data.getDouble(player.getWorld().getName() + ".z"), Float.parseFloat(data.getString(player.getWorld().getName() + ".pitch")), Float.parseFloat(data.getString(player.getWorld().getName() + ".yaw"))));

                Util.sendMessage(sender, Main.Instance.config.getString("messages.spawned"));
            } else if (args.length == 0) {
                Util.sendMessage(sender, Main.Instance.config.getString("messages.noconsole"));
            } else {
                Player player = Main.Instance.getServer().getPlayer(args[0]);
                player.teleport(new Location(player.getWorld(), data.getDouble(player.getWorld().getName() + ".x"), data.getDouble(player.getWorld().getName() + ".y"), data.getDouble(player.getWorld().getName() + ".z"), Float.parseFloat(data.getString(player.getWorld().getName() + ".pitch")), Float.parseFloat(data.getString(player.getWorld().getName() + ".yaw"))));

                Util.sendMessage(sender, Main.Instance.config.getString("messages.spawnedplayer").replace("{player}", args[0]));
                Util.sendMessage(player, Main.Instance.config.getString("messages.spawned"));
            }
        } else {
            Util.sendMessage(sender, Main.Instance.config.getString("messages.noperms").replace("{permission}", "kalesutilities.spawn"));
        }

        return true;
    }
}