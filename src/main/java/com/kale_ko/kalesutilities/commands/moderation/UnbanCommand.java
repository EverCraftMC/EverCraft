package com.kale_ko.kalesutilities.commands.moderation;

import com.kale_ko.kalesutilities.KalesUtilities;
import com.kale_ko.kalesutilities.Util;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

public class UnbanCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (Util.hasPermission(sender, "kalesutilities.ban")) {
            if (args.length > 0) {
                    File dataFolder = KalesUtilities.Instance.getDataFolder();
                    if (!dataFolder.exists()) {
                        dataFolder.mkdir();
                    }

                    File dataFile = Paths.get(dataFolder.getAbsolutePath(), "players.yml").toFile();

                    YamlConfiguration data = YamlConfiguration.loadConfiguration(dataFile);

                    data.set("players." + args[0] + ".banned", null);
                    data.set("players." + args[0] + ".banMessage", null);

                    try {
                        data.save(dataFile);

                        Util.broadcastMessage(KalesUtilities.Instance.config.getString("messages.unban").replace("{player}", args[0]).replace("{moderator}", Util.getPlayerName(sender)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            } else {
                Util.sendMessage(sender, KalesUtilities.Instance.config.getString("messages.usage").replace("{usage}", KalesUtilities.Instance.getCommand("unban").getUsage()));
            }
        } else {
            Util.sendMessage(sender, KalesUtilities.Instance.config.getString("messages.noperms").replace("{permission}", "kalesutilities.ban"));
        }

        return true;
    }
}