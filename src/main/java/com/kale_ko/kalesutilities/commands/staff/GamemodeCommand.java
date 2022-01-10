package com.kale_ko.kalesutilities.commands.staff;

import com.kale_ko.kalesutilities.Main;
import com.kale_ko.kalesutilities.Util;
import org.bukkit.GameMode;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GamemodeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (Util.hasPermission(sender, "kalesutilities.gamemode")) {
            if (args.length == 0) {
                if (sender instanceof Player player) {
                    if (label.equalsIgnoreCase("gmc")) {
                        player.setGameMode(GameMode.CREATIVE);

                        Util.sendMessage(sender, Main.Instance.config.getString("messages.gamemode").replace("{gamemode}", "creative"));
                    } else if (label.equalsIgnoreCase("gms")) {
                        player.setGameMode(GameMode.SURVIVAL);

                        Util.sendMessage(sender, Main.Instance.config.getString("messages.gamemode").replace("{gamemode}", "survival"));
                    } else if (label.equalsIgnoreCase("gma")) {
                        player.setGameMode(GameMode.ADVENTURE);

                        Util.sendMessage(sender, Main.Instance.config.getString("messages.gamemode").replace("{gamemode}", "adventure"));
                    } else if (label.equalsIgnoreCase("gmsp")) {
                        player.setGameMode(GameMode.SPECTATOR);

                        Util.sendMessage(sender, Main.Instance.config.getString("messages.gamemode").replace("{gamemode}", "spectator"));
                    }
                } else {
                    Util.sendMessage(sender, Main.Instance.config.getString("messages.noconsole"));
                }
            } else {
                if (Util.hasPermission(sender, "kalesutilities.sudo")) {
                    Player player = Main.Instance.getServer().getPlayer(args[0]);

                    if (player != null) {
                        if (label.equalsIgnoreCase("gmc")) {
                            player.setGameMode(GameMode.CREATIVE);

                            Util.sendMessage(sender, Main.Instance.config.getString("messages.gamemode").replace("{gamemode}", "creative"));
                        } else if (label.equalsIgnoreCase("gms")) {
                            player.setGameMode(GameMode.SURVIVAL);

                            Util.sendMessage(sender, Main.Instance.config.getString("messages.gamemode").replace("{gamemode}", "survival"));
                        } else if (label.equalsIgnoreCase("gma")) {
                            player.setGameMode(GameMode.ADVENTURE);

                            Util.sendMessage(sender, Main.Instance.config.getString("messages.gamemode").replace("{gamemode}", "adventure"));
                        } else if (label.equalsIgnoreCase("gmsp")) {
                            player.setGameMode(GameMode.SPECTATOR);

                            Util.sendMessage(sender, Main.Instance.config.getString("messages.gamemode").replace("{gamemode}", "spectator"));
                        }
                    } else {
                        Util.sendMessage(sender, Main.Instance.config.getString("messages.playernotfound").replace("{player}", args[0]));
                    }
                } else {
                    Util.sendMessage(sender, Main.Instance.config.getString("messages.noperms").replace("{permission}", "kalesutilities.sudo"));
                }
            }
        } else {
            Util.sendMessage(sender, Main.Instance.config.getString("messages.noperms").replace("{permission}", "kalesutilities.gamemode"));
        }

        return true;
    }
}