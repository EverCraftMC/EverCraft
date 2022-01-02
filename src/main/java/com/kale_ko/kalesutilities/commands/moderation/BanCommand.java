package com.kale_ko.kalesutilities.commands.moderation;

import com.kale_ko.kalesutilities.Main;
import com.kale_ko.kalesutilities.Util;
import com.kale_ko.kalesutilities.Config;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class BanCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (Util.hasPermission(sender, "kalesutilities.ban")) {
            if (args.length > 1) {
                Player player = Main.Instance.getServer().getPlayer(args[0]);

                if (player != null) {
                    StringBuilder banMessageBuilder = new StringBuilder();

                    for (Integer i = 1; i < args.length; i++) {
                        banMessageBuilder.append(args[i] + " ");
                    }

                    String banMessage = banMessageBuilder.toString();

                    Config config = Config.load("players.yml");
                    YamlConfiguration data = config.getConfig();

                    data.set("players." + player.getPlayer().getName() + ".banned", true);
                    data.set("players." + player.getPlayer().getName() + ".banMessage", Main.Instance.config.getConfig().getString("messages.ban").replace("{player}", "You").replace("{moderator}", Util.getPlayerName(sender)).replace("{reason}", banMessage).replace("was", "are"));

                    config.save();

                    player.kickPlayer(Main.Instance.config.getConfig().getString("messages.ban").replace("{player}", "You").replace("{moderator}", Util.getPlayerName(sender)).replace("{reason}", banMessage).replace("was", "where"));

                    Util.broadcastMessage(Main.Instance.config.getConfig().getString("messages.ban").replace("{player}", Util.getPlayerName(player)).replace("{moderator}", Util.getPlayerName(sender)).replace("{reason}", banMessage));
                } else {
                    Util.sendMessage(sender, Main.Instance.config.getConfig().getString("messages.playernotfound").replace("{player}", args[0]));
                }
            } else {
                Util.sendMessage(sender, Main.Instance.config.getConfig().getString("messages.usage").replace("{usage}", Main.Instance.getCommand("ban").getUsage()));
            }
        } else {
            Util.sendMessage(sender, Main.Instance.config.getConfig().getString("messages.noperms").replace("{permission}", "kalesutilities.ban"));
        }

        return true;
    }
}