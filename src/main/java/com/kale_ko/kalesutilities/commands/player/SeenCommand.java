package com.kale_ko.kalesutilities.commands.player;

import java.io.File;
import java.nio.file.Paths;
import java.util.Date;
import com.kale_ko.kalesutilities.KalesUtilities;
import com.kale_ko.kalesutilities.Util;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

public class SeenCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (Util.hasPermission(sender, "kalesutilities.seen")) {
            if (args.length > 0) {
                if (KalesUtilities.Instance.getServer().getPlayer(args[0]) != null) {
                    Util.sendMessage(sender, KalesUtilities.Instance.config.getString("messages.playeronline").replace("{player}", args[0]));
                } else {
                    File dataFolder = KalesUtilities.Instance.getDataFolder();
                    if (!dataFolder.exists()) {
                        dataFolder.mkdir();
                    }

                    File dataFile = Paths.get(dataFolder.getAbsolutePath(), "players.yml").toFile();

                    YamlConfiguration data = YamlConfiguration.loadConfiguration(dataFile);

                    long lastOnline = data.getLong("players." + args[0] + ".lastOnline");

                    if (lastOnline != 0) {
                        Util.sendMessage(sender, KalesUtilities.Instance.config.getString("messages.lastonline").replace("{player}", args[0]).replace("{time}", new Date(lastOnline).toString()));
                    } else {
                        Util.sendMessage(sender, KalesUtilities.Instance.config.getString("messages.playernotfound").replace("{player}", args[0]));
                    }
                }
            } else {
                Util.sendMessage(sender, KalesUtilities.Instance.config.getString("messages.usage").replace("{usage}", KalesUtilities.Instance.getCommand("seen").getUsage()));
            }
        } else {
            Util.sendMessage(sender, KalesUtilities.Instance.config.getString("messages.noperms").replace("{permission}", "kalesutilities.seen"));
        }

        return true;
    }
}