package com.kale_ko.kalesutilities.commands;

import com.kale_ko.kalesutilities.Main;
import com.kale_ko.kalesutilities.Util;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

public class UnmuteCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (Util.hasPermission(sender, "kalesutilities.mute")) {
            if (args.length > 0) {
                    File dataFolder = Main.Instance.getDataFolder();
                    if (!dataFolder.exists()) {
                        dataFolder.mkdir();
                    }

                    File dataFile = Paths.get(dataFolder.getAbsolutePath(), "players.yml").toFile();

                    YamlConfiguration data = YamlConfiguration.loadConfiguration(dataFile);

                    data.set("players." + args[0] + ".muted", null);
                    data.set("players." + args[0] + ".mutedMessage", null);

                    try {
                        data.save(dataFile);

                        Util.broadcastMessage(Main.Instance.config.getString("messages.unmute").replace("{player}", args[0]).replace("{moderator}", Util.getPlayerName(sender)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            } else {
                Util.sendMessage(sender, Main.Instance.config.getString("messages.usage").replace("{usage}", Main.Instance.getCommand("unmute").getUsage()));
            }
        } else {
            Util.sendMessage(sender, Main.Instance.config.getString("messages.noperms").replace("{permission}", "kalesutilities.mute"));
        }

        return true;
    }
}