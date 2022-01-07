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
                player.teleport(new Location(player.getWorld(), Main.Instance.spawn.getConfig().getDouble(player.getWorld().getName() + ".x"), Main.Instance.spawn.getConfig().getDouble(player.getWorld().getName() + ".y"), Main.Instance.spawn.getConfig().getDouble(player.getWorld().getName() + ".z"), Float.parseFloat(Main.Instance.spawn.getConfig().getString(player.getWorld().getName() + ".yaw")), Float.parseFloat(Main.Instance.spawn.getConfig().getString(player.getWorld().getName() + ".pitch"))));

                Util.sendMessage(sender, Main.Instance.config.getConfig().getString("messages.spawned"));
            } else if (args.length == 0) {
                Util.sendMessage(sender, Main.Instance.config.getConfig().getString("messages.noconsole"));
            } else {
                Player player = Main.Instance.getServer().getPlayer(args[0]);
                player.teleport(new Location(player.getWorld(), Main.Instance.spawn.getConfig().getDouble(player.getWorld().getName() + ".x"), Main.Instance.spawn.getConfig().getDouble(player.getWorld().getName() + ".y"), Main.Instance.spawn.getConfig().getDouble(player.getWorld().getName() + ".z"), Float.parseFloat(Main.Instance.spawn.getConfig().getString(player.getWorld().getName() + ".yaw")), Float.parseFloat(Main.Instance.spawn.getConfig().getString(player.getWorld().getName() + ".pitch"))));

                Util.sendMessage(sender, Main.Instance.config.getConfig().getString("messages.spawnedplayer").replace("{player}", args[0]));
                Util.sendMessage(player, Main.Instance.config.getConfig().getString("messages.spawned"));
            }
        } else {
            Util.sendMessage(sender, Main.Instance.config.getConfig().getString("messages.noperms").replace("{permission}", "kalesutilities.spawn"));
        }

        return true;
    }
}