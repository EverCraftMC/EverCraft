package com.kale_ko.kalesutilities.commands.moderation;

import com.kale_ko.kalesutilities.Main;
import com.kale_ko.kalesutilities.Util;
import com.kale_ko.kalesutilities.Config;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

public class UnmuteCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (Util.hasPermission(sender, "kalesutilities.mute")) {
            if (args.length > 0) {
                Config config = Config.load("players.yml");
                YamlConfiguration data = config.getConfig();

                data.set("players." + args[0] + ".muted", null);
                data.set("players." + args[0] + ".mutedMessage", null);

                config.save();

                Util.broadcastMessage(Main.Instance.config.getConfig().getString("messages.unmute").replace("{player}", args[0]).replace("{moderator}", Util.getPlayerName(sender)));
            } else {
                Util.sendMessage(sender, Main.Instance.config.getConfig().getString("messages.usage").replace("{usage}", Main.Instance.getCommand("unmute").getUsage()));
            }
        } else {
            Util.sendMessage(sender, Main.Instance.config.getConfig().getString("messages.noperms").replace("{permission}", "kalesutilities.mute"));
        }

        return true;
    }
}