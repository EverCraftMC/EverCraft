package com.kale_ko.kalesutilities.commands.warps;

import com.kale_ko.kalesutilities.Main;
import com.kale_ko.kalesutilities.Util;
import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (Util.hasPermission(sender, "kalesutilities.spawn")) {
            if (args.length == 0 && sender instanceof Player player) {
                player.teleport(Main.Instance.spawn.getSerializable(player.getWorld().getName(), Location.class));

                Util.sendMessage(sender, Main.Instance.config.getString("messages.spawned"));
            } else if (args.length == 0) {
                Util.sendMessage(sender, Main.Instance.config.getString("messages.noconsole"));
            } else {
                if (Util.hasPermission(sender, "kalesutilities.sudo")) {
                    Player player = Main.Instance.getServer().getPlayer(args[0]);

                    if (player != null) {
                        player.teleport(Main.Instance.spawn.getSerializable(player.getWorld().getName(), Location.class));

                        Util.sendMessage(sender, Main.Instance.config.getString("messages.spawnedplayer").replace("{player}", args[0]));
                        Util.sendMessage(player, Main.Instance.config.getString("messages.spawned"));
                    } else {
                        Util.sendMessage(sender, Main.Instance.config.getString("messages.playernotfound").replace("{player}", args[0]));
                    }
                } else {
                    Util.sendMessage(sender, Main.Instance.config.getString("messages.noperms").replace("{permission}", "kalesutilities.sudo"));
                }
            }
        } else {
            Util.sendMessage(sender, Main.Instance.config.getString("messages.noperms").replace("{permission}", "kalesutilities.spawn"));
        }

        return true;
    }
}