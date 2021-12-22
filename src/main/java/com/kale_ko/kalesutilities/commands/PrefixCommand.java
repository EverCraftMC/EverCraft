package com.kale_ko.kalesutilities.commands;

import com.kale_ko.kalesutilities.Main;
import com.kale_ko.kalesutilities.Util;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class PrefixCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (Util.hasPermission(sender, "kalesutilities.setprefix")) {
            File dataFolder = Main.Instance.getDataFolder();
            if (!dataFolder.exists()) {
                dataFolder.mkdir();
            }

            File dataFile = Paths.get(dataFolder.getAbsolutePath(), "players.yml").toFile();

            YamlConfiguration data = YamlConfiguration.loadConfiguration(dataFile);

            if (sender instanceof Player player) {
                data.set("players." + player.getPlayer().getName() + ".prefix", args[0]);

                try {
                    data.save(dataFile);

                    Util.sendMessage(sender, Main.Instance.config.getString("messages.setprefix"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Util.sendMessage(sender, Main.Instance.config.getString("messages.noconsole"));
            }
        } else {
            Util.sendMessage(sender, Main.Instance.config.getString("messages.usage").replace("{usage}", Main.Instance.getCommand("prefix").getUsage()));
        }

        return true;
    }
}