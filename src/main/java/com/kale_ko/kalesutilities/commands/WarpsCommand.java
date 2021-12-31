package com.kale_ko.kalesutilities.commands;

import com.kale_ko.kalesutilities.Main;
import com.kale_ko.kalesutilities.Util;
import java.io.File;
import java.nio.file.Paths;
import java.util.Set;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

public class WarpsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (Util.hasPermission(sender, "kalesutilities.warps")) {
            File dataFolder = Main.Instance.getDataFolder();
            if (!dataFolder.exists()) {
                dataFolder.mkdir();
            }

            File dataFile = Paths.get(dataFolder.getAbsolutePath(), "warps.yml").toFile();

            YamlConfiguration data = YamlConfiguration.loadConfiguration(dataFile);

            StringBuilder warps = new StringBuilder();

            Set<String> keys = data.getKeys(false);
            for (String key : keys) {
                warps.append(key + "\n");
            }
            
            Util.sendMessage(sender, Main.Instance.config.getString("messages.warps").replace("{warpList}", warps.toString()));
        } else {
            Util.sendMessage(sender, Main.Instance.config.getString("messages.noperms").replace("{permission}", "kalesutilities.warps"));
        }

        return true;
    }
}