package com.kale_ko.kalesutilities.bungee;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Util {
    public static void sendMessage(CommandSender user, String message) {
        user.sendMessage(formatMessage(Main.Instance.config.getString("config.prefix") + " " + message));
    }

    public static void sendMessage(CommandSender user, String message, Boolean noprefix) {
        if (!noprefix) {
            user.sendMessage(formatMessage(Main.Instance.config.getString("config.prefix") + " " + message));
        } else {
            user.sendMessage(formatMessage(message));
        }
    }

    public static void broadcastMessage(String message) {
        Main.Instance.getProxy().broadcast(formatMessage(Main.Instance.config.getString("config.prefix") + " " + message));
    }

    public static void broadcastMessage(String message, Boolean noprefix) {
        if (!noprefix) {
            Main.Instance.getProxy().broadcast(formatMessage(Main.Instance.config.getString("config.prefix") + " " + message));
        } else {
            Main.Instance.getProxy().broadcast(formatMessage(message));
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

    public static String getNoPermissionMessage(String permission) {
        return Main.Instance.config.getString("messages.noperms").replace("{permission}", permission);
    }

    public static Boolean hasPermission(ProxiedPlayer player, String permission) {
        return player.hasPermission(permission);
    }

    public static Boolean hasPermission(CommandSender sender, String permission) {
        if (sender == Main.Instance.getProxy().getConsole()) {
            return true;
        } else {
            return sender.hasPermission(permission);
        }
    }

    public static String getPlayerNickName(ProxiedPlayer player) {
        String name = player.getName();

        if (Main.Instance.players.getString(player.getName() + ".nickname") != null && !Main.Instance.players.getString(player.getName() + ".nickname").equalsIgnoreCase("") && !Main.Instance.players.getString(player.getName() + ".nickname").equalsIgnoreCase(" ")) {
            if (Util.hasPermission(player, "kalesutilities.nonickstar") || Main.Instance.players.getString(player.getName() + ".nickname").equalsIgnoreCase(name) || Util.stripFormating(Util.formatMessage(Main.Instance.players.getString(player.getName() + ".nickname") + "&r")).equalsIgnoreCase(name)) {
                name = Util.formatMessage(Main.Instance.players.getString(player.getName() + ".nickname") + "&r");
            } else {
                name = Util.formatMessage("*" + Main.Instance.players.getString(player.getName() + ".nickname") + "&r");
            }
        }

        return name;
    }

    public static String getPlayerPrefix(ProxiedPlayer player) {
        String prefix = "";

        if (Main.Instance.players.getString(player.getName() + ".prefix") != null && !Main.Instance.players.getString(player.getName() + ".prefix").equalsIgnoreCase("") && !Main.Instance.players.getString(player.getName() + ".prefix").equalsIgnoreCase(" ")) {
            prefix = Util.formatMessage(Main.Instance.players.getString(player.getName() + ".prefix") + "&r") + " ";
        }

        return prefix;
    }

    public static String getPlayerName(ProxiedPlayer player) {
        return getPlayerPrefix(player) + getPlayerNickName(player);
    }

    public static String getPlayerName(CommandSender sender) {
        if (sender instanceof ProxiedPlayer player) {
            return getPlayerName(player);
        } else {
            return "CONSOLE";
        }
    }

    public static void updatePlayerName(ProxiedPlayer player) {
        player.setDisplayName(getPlayerName(player));
    }

    public static void resetPlayerName(ProxiedPlayer player) {
        player.setDisplayName(player.getName());
    }

    public static <T, N> Map<T, N> mapFromLists(List<T> keys, List<N> values) {
        Map<T, N> newMap = new HashMap<T, N>();

        for (T key : keys) {
            newMap.put(key, values.get(keys.indexOf(key)));
        }

        return newMap;
    }
}