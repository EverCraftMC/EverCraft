package com.kale_ko.kalesutilities.spigot.commands.kits;

import com.kale_ko.kalesutilities.spigot.Main;
import com.kale_ko.kalesutilities.spigot.Util;
import com.kale_ko.kalesutilities.spigot.commands.SpigotCommand;
import java.util.List;
import java.util.Set;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitsCommand extends SpigotCommand {
    public KitsCommand(String name, String description, List<String> aliases, String usage, String permission) {
        super(name, description, aliases, usage, permission);
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        StringBuilder kits = new StringBuilder();

        Set<String> keys = Main.Instance.kits.getKeys();
        for (String key : keys) {
            kits.append(key + "\n");
        }

        if (args.length == 0) {
            Util.sendMessage(sender, Main.Instance.config.getString("messages.kits").replace("{kitList}", kits.toString()));
        } else {
            if (Util.hasPermission(sender, "kalesutilities.commands.staff.sudo")) {
                Player player = Main.Instance.getServer().getPlayer(args[0]);

                if (player != null) {
                    Util.sendMessage(player, Main.Instance.config.getString("messages.kits").replace("{kitList}", kits.toString()));
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