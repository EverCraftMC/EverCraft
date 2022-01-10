package com.kale_ko.kalesutilities.commands.kits;

import com.kale_ko.kalesutilities.Main;
import com.kale_ko.kalesutilities.Util;
import java.util.Set;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (Util.hasPermission(sender, "kalesutilities.kits")) {
            StringBuilder kits = new StringBuilder();

            Set<String> keys = Main.Instance.kits.getKeys();
            for (String key : keys) {
                kits.append(key + "\n");
            }

            if (args.length == 0) {
                Util.sendMessage(sender, Main.Instance.config.getString("messages.kits").replace("{kitList}", kits.toString()));
            } else {
                if (Util.hasPermission(sender, "kalesutilities.sudo")) {
                    Player player = Main.Instance.getServer().getPlayer(args[0]);

                    if (player != null) {
                        Util.sendMessage(player, Main.Instance.config.getString("messages.kits").replace("{kitList}", kits.toString()));
                    } else {
                        Util.sendMessage(sender, Main.Instance.config.getString("messages.playernotfound").replace("{player}", args[0]));
                    }
                } else {
                    Util.sendMessage(sender, Main.Instance.config.getString("messages.noperms").replace("{permission}", "kalesutilities.sudo"));
                }
            }
        } else {
            Util.sendMessage(sender, Main.Instance.config.getString("messages.noperms").replace("{permission}", "kalesutilities.kits"));
        }

        return true;
    }
}