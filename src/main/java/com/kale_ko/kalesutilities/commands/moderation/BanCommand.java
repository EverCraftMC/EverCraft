package com.kale_ko.kalesutilities.commands.moderation;

import com.kale_ko.kalesutilities.KalesUtilities;
import com.kale_ko.kalesutilities.Util;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class BanCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (Util.hasPermission(sender, "kalesutilities.ban")) {
            if (args.length > 1) {
                Player player = KalesUtilities.Instance.getServer().getPlayer(args[0]);

                if (player != null) {
                    StringBuilder banMessageBuilder = new StringBuilder();

                    for (Integer i = 1; i < args.length; i++) {
                        banMessageBuilder.append(args[i] + " ");
                    }

                    String banMessage = banMessageBuilder.toString();

                    File dataFolder = KalesUtilities.Instance.getDataFolder();
                    if (!dataFolder.exists()) {
                        dataFolder.mkdir();
                    }

                    File dataFile = Paths.get(dataFolder.getAbsolutePath(), "players.yml").toFile();

                    YamlConfiguration data = YamlConfiguration.loadConfiguration(dataFile);

                    data.set("players." + player.getPlayer().getName() + ".banned", true);
                    data.set("players." + player.getPlayer().getName() + ".banMessage", KalesUtilities.Instance.config.getString("messages.ban").replace("{player}", "You").replace("{moderator}", Util.getPlayerName(sender)).replace("{reason}", banMessage).replace("was", "are"));

                    try {
                        data.save(dataFile);

                        player.kickPlayer(KalesUtilities.Instance.config.getString("messages.ban").replace("{player}", "You").replace("{moderator}", Util.getPlayerName(sender)).replace("{reason}", banMessage).replace("was", "where"));

                        Util.broadcastMessage(KalesUtilities.Instance.config.getString("messages.ban").replace("{player}", Util.getPlayerName(player)).replace("{moderator}", Util.getPlayerName(sender)).replace("{reason}", banMessage));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Util.sendMessage(sender, KalesUtilities.Instance.config.getString("messages.playernotfound").replace("{player}", args[0]));
                }
            } else {
                Util.sendMessage(sender, KalesUtilities.Instance.config.getString("messages.usage").replace("{usage}", KalesUtilities.Instance.getCommand("ban").getUsage()));
            }
        } else {
            Util.sendMessage(sender, KalesUtilities.Instance.config.getString("messages.noperms").replace("{permission}", "kalesutilities.ban"));
        }

        return true;
    }
}