package com.kale_ko.kalesutilities.spigot.commands.player;

import java.util.List;
import com.kale_ko.kalesutilities.spigot.SpigotPlugin;
import com.kale_ko.kalesutilities.spigot.Util;
import com.kale_ko.kalesutilities.spigot.commands.SpigotCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PrefixCommand extends SpigotCommand {
    public PrefixCommand(String name, String description, List<String> aliases, String usage, String permission) {
        super(name, description, aliases, usage, permission);
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (args.length > 1) {
            if (Util.hasPermission(sender, "kalesutilities.commands.staff.sudo")) {
                Player player = SpigotPlugin.Instance.getServer().getPlayer(args[0]);

                if (player != null) {
                    SpigotPlugin.Instance.players.set(player.getPlayer().getName() + ".prefix", args[1]);

                    Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.setprefix"));
                    Util.updatePlayerName(player);
                } else {
                    Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.playernotfound").replace("{player}", args[0]));
                }
            } else {
                Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.noperms").replace("{permission}", "kalesutilities.commands.staff.sudo"));
            }
        } else if (args.length > 0) {
            if (sender instanceof Player player) {
                SpigotPlugin.Instance.players.set(player.getPlayer().getName() + ".prefix", args[0]);

                Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.setprefix"));
                Util.updatePlayerName(player);
            } else {
                Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.noconsole"));
            }
        } else if (args.length == 0) {
            if (sender instanceof Player player) {
                SpigotPlugin.Instance.players.set(player.getPlayer().getName() + ".prefix", "");

                Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.setprefix"));
                Util.updatePlayerName(player);
            } else {
                Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.noconsole"));
            }
        } else {
            Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.usage").replace("{usage}", SpigotPlugin.Instance.getCommand("prefix").getUsage()));
        }

        return true;
    }
}