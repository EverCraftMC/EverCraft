package com.kale_ko.kalesutilities.commands;

import com.kale_ko.kalesutilities.Main;
import com.kale_ko.kalesutilities.Util;
import java.io.File;
import java.nio.file.Paths;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

public class SetSpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (Util.hasPermission(sender, "kalesutilities.setspawn")) {
            if (args.length > 0) {
                File dataFolder = Main.Instance.getDataFolder();
                if (!dataFolder.exists()) {
                    dataFolder.mkdir();
                }

                File dataFile = Paths.get(dataFolder.getAbsolutePath(), "spawn.yml").toFile();

                YamlConfiguration data = YamlConfiguration.loadConfiguration(dataFile);

            } else {
                Util.sendMessage(sender, Main.Instance.config.getString("messages.usage").replace("{usage}", Main.Instance.getCommand("setspawn").getUsage()));
            }
        } else {
            Util.sendMessage(sender, Main.Instance.config.getString("messages.noperms").replace("{permission}", "kalesutilities.setspawn"));
        }

        return true;
    }
}