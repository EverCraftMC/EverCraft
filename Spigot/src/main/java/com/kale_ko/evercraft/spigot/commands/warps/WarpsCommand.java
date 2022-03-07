package com.kale_ko.evercraft.spigot.commands.warps;

import java.util.Collection;
import java.util.List;
import com.kale_ko.evercraft.spigot.SpigotPlugin;
import com.kale_ko.evercraft.spigot.Util;
import com.kale_ko.evercraft.spigot.commands.SpigotCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpsCommand extends SpigotCommand {
    public WarpsCommand(String name, String description, List<String> aliases, String usage, String permission) {
        super(name, description, aliases, usage, permission);
    }

    @Override
    public void run(CommandSender sender, String label, String[] args) {
        StringBuilder warps = new StringBuilder();

        Collection<String> keys = SpigotPlugin.Instance.warps.getKeys();
        for (String key : keys) {
            warps.append(key + "\n");
        }

        if (args.length == 0) {
            Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.warps").replace("{warpList}", warps.toString()));
        } else {
            if (Util.hasPermission(sender, "evercraft.commands.staff.sudo")) {
                Player player = SpigotPlugin.Instance.getServer().getPlayer(args[0]);

                if (player != null) {
                    Util.sendMessage(player, SpigotPlugin.Instance.config.getString("messages.warps").replace("{warpList}", warps.toString()));
                } else {
                    Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.playernotfound").replace("{player}", args[0]));
                }
            } else {
                Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.noperms").replace("{permission}", "evercraft.commands.staff.sudo"));
            }
        }
    }
}