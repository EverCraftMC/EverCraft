package com.kale_ko.kalesutilities.commands.moderation;

import com.kale_ko.kalesutilities.Main;
import com.kale_ko.kalesutilities.Util;
import com.kale_ko.kalesutilities.Config;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class MuteCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (Util.hasPermission(sender, "kalesutilities.mute")) {
            if (args.length > 1) {
                Player player = Main.Instance.getServer().getPlayer(args[0]);

                if (player != null) {
                    StringBuilder muteMessageBuilder = new StringBuilder();

                    for (Integer i = 1; i < args.length; i++) {
                        muteMessageBuilder.append(args[i] + " ");
                    }

                    String muteMessage = muteMessageBuilder.toString();

                    Config config = Config.load("players.yml");
                    YamlConfiguration data = config.getConfig();

                    data.set("players." + player.getPlayer().getName() + ".muted", true);
                    data.set("players." + player.getPlayer().getName() + ".mutedMessage", Main.Instance.config.getConfig().getString("messages.mute").replace("{player}", "You") .replace("{moderator}", Util.getPlayerName(sender)).replace("{reason}", muteMessage) .replace("was", "are"));

                    config.save();

                    Util.broadcastMessage(Main.Instance.config.getConfig().getString("messages.mute").replace("{player}", Util.getPlayerName(player)).replace("{moderator}", Util.getPlayerName(sender)).replace("{reason}", muteMessage));
                } else {
                    Util.sendMessage(sender, Main.Instance.config.getConfig().getString("messages.playernotfound").replace("{player}", args[0]));
                }
            } else {
                Util.sendMessage(sender, Main.Instance.config.getConfig().getString("messages.usage").replace("{usage}", Main.Instance.getCommand("mute").getUsage()));
            }
        } else {
            Util.sendMessage(sender, Main.Instance.config.getConfig().getString("messages.noperms").replace("{permission}", "kalesutilities.mute"));
        }

        return true;
    }
}