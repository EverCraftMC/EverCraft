package com.kale_ko.kalesutilities.spigot.commands.player;

import com.kale_ko.kalesutilities.spigot.Main;
import com.kale_ko.kalesutilities.spigot.Util;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NicknameCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (args.length > 1) {
            if (Util.hasPermission(sender, "kalesutilities.commands.staff.sudo")) {
                Player player = Main.Instance.getServer().getPlayer(args[0]);

                if (player != null) {
                    Main.Instance.players.set(player.getPlayer().getName() + ".nickname", args[1]);

                    Main.Instance.players.save();

                    Util.sendMessage(sender, Main.Instance.config.getString("messages.setnickname"));
                    Util.updatePlayerName(player);
                } else {
                    Util.sendMessage(sender, Main.Instance.config.getString("messages.playernotfound").replace("{player}", args[0]));
                }
            } else {
                Util.sendMessage(sender, Main.Instance.config.getString("messages.noperms").replace("{permission}", "kalesutilities.commands.staff.sudo"));
            }
        } else if (args.length > 0) {
            if (sender instanceof Player player) {
                Main.Instance.players.set(player.getPlayer().getName() + ".nickname", args[0]);

                Main.Instance.players.save();

                Util.sendMessage(sender, Main.Instance.config.getString("messages.setnickname"));
                Util.updatePlayerName(player);
            } else {
                Util.sendMessage(sender, Main.Instance.config.getString("messages.noconsole"));
            }
        } else if (args.length == 0) {
            if (sender instanceof Player player) {
                Main.Instance.players.set(player.getPlayer().getName() + ".nickname", player.getName());

                Main.Instance.players.save();

                Util.sendMessage(sender, Main.Instance.config.getString("messages.setnickname"));
                Util.updatePlayerName(player);
            } else {
                Util.sendMessage(sender, Main.Instance.config.getString("messages.noconsole"));
            }
        } else {
            Util.sendMessage(sender, Main.Instance.config.getString("messages.usage").replace("{usage}", Main.Instance.getCommand("nickname").getUsage()));
        }

        return true;
    }
}