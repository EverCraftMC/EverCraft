package com.kale_ko.kalesutilities;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class Util {
    public static void sendMessage(CommandSender user, String message) {
        user.sendMessage(formatMessage(Main.Instance.config.getConfig().getString("config.prefix") + " " + message));
    }

    public static void sendMessage(CommandSender user, String message, Boolean noprefix) {
        if (!noprefix) {
            user.sendMessage(formatMessage(Main.Instance.config.getConfig().getString("config.prefix") + " " + message));
        } else {
            user.sendMessage(formatMessage(message));
        }
    }

    public static void broadcastMessage(String message) {
        Main.Instance.getServer().broadcastMessage(formatMessage(Main.Instance.config.getConfig().getString("config.prefix") + " " + message));
    }

    public static void broadcastMessage(String message, Boolean noprefix) {
        if (!noprefix) {
            Main.Instance.getServer().broadcastMessage(formatMessage(Main.Instance.config.getConfig().getString("config.prefix") + " " + message));
        } else {
            Main.Instance.getServer().broadcastMessage(formatMessage(message));
        }
    }

    public static String formatMessage(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String unFormatMessage(String message) {
        char[] chars = message.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == ChatColor.COLOR_CHAR && "0123456789abcdefklmnorx".indexOf(chars[i + 1]) > -1) {
                chars[i] = '&';
            }
        }

        return new String(chars);
    }

    public static String stripFormating(String message) {
        return ChatColor.stripColor(message);
    }

    public static Boolean hasPermission(Player player, String permission) {
        return player.hasPermission(permission) || player.isOp();
    }

    public static Boolean hasPermission(CommandSender sender, String permission) {
        if (sender instanceof ConsoleCommandSender) {
            return true;
        } else {
            return sender.hasPermission(permission) || sender.isOp();
        }
    }

    public static String getPlayerNickName(Player player) {
        String name = player.getName();

        if (Main.Instance.players.getConfig().getString("players." + player.getName() + ".nickname") != null && !Main.Instance.players.getConfig().getString("players." + player.getName() + ".nickname").equalsIgnoreCase("") && !Main.Instance.players.getConfig().getString("players." + player.getName() + ".nickname").equalsIgnoreCase(" ")) {
            if (Util.hasPermission(player, "kalesutilities.nonickstar") || Main.Instance.players.getConfig().getString("players." + player.getName() + ".nickname").equalsIgnoreCase(name) || Util.stripFormating(Util.formatMessage(Main.Instance.players.getConfig().getString("players." + player.getName() + ".nickname") + "&r")).equalsIgnoreCase(name)) {
                name = Util.formatMessage(Main.Instance.players.getConfig().getString("players." + player.getName() + ".nickname") + "&r");
            } else {
                name = Util.formatMessage("*" + Main.Instance.players.getConfig().getString("players." + player.getName() + ".nickname") + "&r");
            }
        }

        return name;
    }

    public static String getPlayerPrefix(Player player) {
        String prefix = "";

        if (Main.Instance.players.getConfig().getString("players." + player.getName() + ".prefix") != null && !Main.Instance.players.getConfig().getString("players." + player.getName() + ".prefix").equalsIgnoreCase("") && !Main.Instance.players.getConfig().getString("players." + player.getName() + ".prefix").equalsIgnoreCase(" ")) {
            prefix = Util.formatMessage(Main.Instance.players.getConfig().getString("players." + player.getName() + ".prefix") + "&r") + " ";
        }

        return prefix;
    }

    public static String getPlayerName(Player player) {
        return getPlayerPrefix(player) + getPlayerNickName(player);
    }

    public static String getPlayerName(CommandSender sender) {
        if (sender instanceof Player player) {
            return getPlayerName(player);
        } else {
            return "CONSOLE";
        }
    }

    public static void updatePlayerName(Player player) {
        player.setCustomName(getPlayerName(player));
        player.setCustomNameVisible(true);
        player.setDisplayName(getPlayerName(player));
        player.setPlayerListName(getPlayerName(player));
    }
}