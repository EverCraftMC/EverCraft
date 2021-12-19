package com.kale_ko.kalesutilities.commands;

import java.io.File;
import java.nio.file.Paths;
import java.util.Date;
import com.kale_ko.kalesutilities.Main;
import com.kale_ko.kalesutilities.Util;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

public class SeenCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (args.length > 0) {
            if (Main.Instance.getServer().getPlayer(args[0]) != null) {
                Util.sendMessage(sender, Main.Instance.config.getString("messages.playeronline").replace("{player}", args[0]));
            } else {
                File dataFolder = Main.Instance.getDataFolder();
                if (!dataFolder.exists()) {
                    dataFolder.mkdir();
                }

                File dataFile = Paths.get(dataFolder.getAbsolutePath(), "seen.yml").toFile();

                YamlConfiguration data = YamlConfiguration.loadConfiguration(dataFile);

                long lastOnline = data.getLong("players." + args[0]);

                if (lastOnline != 0) {
                    Util.sendMessage(sender, Main.Instance.config.getString("messages.lastonline").replace("{player}", args[0]).replace("{time}", new Date(lastOnline).toString()));
                } else {
                    Util.sendMessage(sender, Main.Instance.config.getString("messages.playernotfound").replace("{player}", args[0]));
                }
            }
        } else {
            Util.sendMessage(sender, Main.Instance.config.getString("messages.usage").replace("{usage}", Main.Instance.getCommand("seen").getUsage()));
        }

        return true;
    }
}