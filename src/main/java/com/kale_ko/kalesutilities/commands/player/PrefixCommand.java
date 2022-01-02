package com.kale_ko.kalesutilities.commands.player;

import com.kale_ko.kalesutilities.KalesUtilities;
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
            if (args.length > 0) {
                File dataFolder = KalesUtilities.Instance.getDataFolder();
                if (!dataFolder.exists()) {
                    dataFolder.mkdir();
                }

                File dataFile = Paths.get(dataFolder.getAbsolutePath(), "players.yml").toFile();

                YamlConfiguration data = YamlConfiguration.loadConfiguration(dataFile);

                if (sender instanceof Player player) {
                    data.set("players." + player.getPlayer().getName() + ".prefix", args[0]);

                    try {
                        data.save(dataFile);

                        Util.sendMessage(sender, KalesUtilities.Instance.config.getString("messages.setprefix"));
                        Util.updatePlayerName(player);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Util.sendMessage(sender, KalesUtilities.Instance.config.getString("messages.noconsole"));
                }
            } else {
                Util.sendMessage(sender, KalesUtilities.Instance.config.getString("messages.usage").replace("{usage}", KalesUtilities.Instance.getCommand("prefix").getUsage()));
            }
        } else {
            Util.sendMessage(sender, KalesUtilities.Instance.config.getString("messages.noperms").replace("{permission}", "kalesutilities.setprefix"));
        }

        return true;
    }
}