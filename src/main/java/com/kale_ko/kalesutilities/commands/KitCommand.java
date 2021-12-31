package com.kale_ko.kalesutilities.commands;

import com.kale_ko.kalesutilities.Main;
import com.kale_ko.kalesutilities.Util;
import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class KitCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (Util.hasPermission(sender, "kalesutilities.kit")) {
            if (args.length > 0) {
                File dataFolder = Main.Instance.getDataFolder();
                if (!dataFolder.exists()) {
                    dataFolder.mkdir();
                }

                File dataFile = Paths.get(dataFolder.getAbsolutePath(), "kits.yml").toFile();

                YamlConfiguration data = YamlConfiguration.loadConfiguration(dataFile);

                if (sender instanceof Player player) {
                    List<String> items = data.getStringList(args[0]);

                    for (String item : items) {
                        player.getInventory().addItem(new ItemStack(Material.getMaterial(item), 1));
                    }

                    Util.sendMessage(player, Main.Instance.config.getString("message.kit"));
                } else {
                    Util.sendMessage(sender, Main.Instance.config.getString("messages.noconsole"));
                }
            } else {
                Util.sendMessage(sender, Main.Instance.config.getString("messages.usage").replace("{usage}", Main.Instance.getCommand("kit").getUsage()));
            }
        } else {
            Util.sendMessage(sender, Main.Instance.config.getString("messages.noperms").replace("{permission}", "kalesutilities.kit"));
        }

        return true;
    }
}