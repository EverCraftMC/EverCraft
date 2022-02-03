package com.kale_ko.kalesutilities.spigot.commands.staff;

import java.util.List;
import com.kale_ko.kalesutilities.spigot.SpigotPlugin;
import com.kale_ko.kalesutilities.spigot.Util;
import com.kale_ko.kalesutilities.spigot.commands.SpigotCommand;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GamemodeCommand extends SpigotCommand {
    public GamemodeCommand(String name, String description, List<String> aliases, String usage, String permission) {
        super(name, description, aliases, usage, permission);
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player player) {
                if (label.equalsIgnoreCase("gmc")) {
                    player.setGameMode(GameMode.CREATIVE);

                    Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.gamemode").replace("{gamemode}", "creative"));
                } else if (label.equalsIgnoreCase("gms")) {
                    player.setGameMode(GameMode.SURVIVAL);

                    Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.gamemode").replace("{gamemode}", "survival"));
                } else if (label.equalsIgnoreCase("gma")) {
                    player.setGameMode(GameMode.ADVENTURE);

                    Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.gamemode").replace("{gamemode}", "adventure"));
                } else if (label.equalsIgnoreCase("gmsp")) {
                    player.setGameMode(GameMode.SPECTATOR);

                    Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.gamemode").replace("{gamemode}", "spectator"));
                }
            } else {
                Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.noconsole"));
            }
        } else {
            if (Util.hasPermission(sender, "kalesutilities.commands.staff.sudo")) {
                Player player = SpigotPlugin.Instance.getServer().getPlayer(args[0]);

                if (player != null) {
                    if (label.equalsIgnoreCase("gmc")) {
                        player.setGameMode(GameMode.CREATIVE);

                        Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.gamemode").replace("{gamemode}", "creative"));
                    } else if (label.equalsIgnoreCase("gms")) {
                        player.setGameMode(GameMode.SURVIVAL);

                        Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.gamemode").replace("{gamemode}", "survival"));
                    } else if (label.equalsIgnoreCase("gma")) {
                        player.setGameMode(GameMode.ADVENTURE);

                        Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.gamemode").replace("{gamemode}", "adventure"));
                    } else if (label.equalsIgnoreCase("gmsp")) {
                        player.setGameMode(GameMode.SPECTATOR);

                        Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.gamemode").replace("{gamemode}", "spectator"));
                    }
                } else {
                    Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.playernotfound").replace("{player}", args[0]));
                }
            } else {
                Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.noperms").replace("{permission}", "kalesutilities.commands.staff.sudo"));
            }
        }

        return true;
    }
}