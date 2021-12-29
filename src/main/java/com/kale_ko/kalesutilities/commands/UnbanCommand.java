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

public class UnbanCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (Util.hasPermission(sender, "kalesutilities.unban")) {
            if (args.length > 0) {
                Player player = Main.Instance.getServer().getPlayer(args[0]);

                if (player != null) {
                    File dataFolder = Main.Instance.getDataFolder();
                    if (!dataFolder.exists()) {
                        dataFolder.mkdir();
                    }

                    File dataFile = Paths.get(dataFolder.getAbsolutePath(), "players.yml").toFile();

                    YamlConfiguration data = YamlConfiguration.loadConfiguration(dataFile);

                    data.set("players." + player.getPlayer().getName() + ".banned", null);
                    data.set("players." + player.getPlayer().getName() + ".banMessage", null);

                    try {
                        data.save(dataFile);

                        Util.broadcastMessage(Main.Instance.config.getString("messages.unban").replace("{player}", Util.getPlayerName(player)).replace("{moderator}", Util.getPlayerName(sender)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Util.sendMessage(sender, Main.Instance.config.getString("messages.playernotfound").replace("{player}", args[0]));
                }
            } else {
                Util.sendMessage(sender, Main.Instance.config.getString("messages.usage").replace("{usage}", Main.Instance.getCommand("unban").getUsage()));
            }
        } else {
            Util.sendMessage(sender, Main.Instance.config.getString("messages.noperms").replace("{permission}", "kalesutilities.unban"));
        }

        return true;
    }
}