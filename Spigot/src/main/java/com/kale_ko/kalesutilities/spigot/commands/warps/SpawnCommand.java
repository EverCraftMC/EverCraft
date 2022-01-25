package com.kale_ko.kalesutilities.spigot.commands.warps;

import java.util.List;
import com.kale_ko.kalesutilities.spigot.Main;
import com.kale_ko.kalesutilities.spigot.Util;
import com.kale_ko.kalesutilities.spigot.commands.SpigotCommand;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand extends SpigotCommand {
    public SpawnCommand(String name, String description, List<String> aliases, String usage, String permission) {
        super(name, description, aliases, usage, permission);
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (args.length == 0 && sender instanceof Player player) {
            player.teleport(Main.Instance.spawn.getSerializable(player.getWorld().getName(), Location.class));

            Util.sendMessage(sender, Main.Instance.config.getString("messages.spawned"));
        } else if (args.length == 0) {
            Util.sendMessage(sender, Main.Instance.config.getString("messages.noconsole"));
        } else {
            if (Util.hasPermission(sender, "kalesutilities.commands.staff.sudo")) {
                Player player = Main.Instance.getServer().getPlayer(args[0]);

                if (player != null) {
                    player.teleport(Main.Instance.spawn.getSerializable(player.getWorld().getName(), Location.class));

                    Util.sendMessage(sender, Main.Instance.config.getString("messages.spawnedplayer").replace("{player}", args[0]));
                    Util.sendMessage(player, Main.Instance.config.getString("messages.spawned"));
                } else {
                    Util.sendMessage(sender, Main.Instance.config.getString("messages.playernotfound").replace("{player}", args[0]));
                }
            } else {
                Util.sendMessage(sender, Main.Instance.config.getString("messages.noperms").replace("{permission}", "kalesutilities.commands.staff.sudo"));
            }
        }

        return true;
    }
}