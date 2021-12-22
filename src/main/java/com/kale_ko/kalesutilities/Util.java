package com.kale_ko.kalesutilities;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Util {
    public static void sendMessage(CommandSender user, String message) {
        user.sendMessage(defaultStyleMessage(Main.Instance.config.getString("config.prefix") + " " + message));
    }

    public static String formatMessage(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String unFormatMessage(String message) {
        char[] chars = message.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == ChatColor.COLOR_CHAR && "0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx".indexOf(chars[i + 1]) > -1) {
                chars[i] = '&';
                chars[i + 1] = Character.toLowerCase(chars[i + 1]);
            }
        }

        return new String(chars);
    }

    public static String stripFormating(String message) {
        return ChatColor.stripColor(message);
    }

    public static String styleMessage(String message, String style) {
        return ChatColor.translateAlternateColorCodes('&', style + message);
    }

    public static String defaultStyleMessage(String message) {
        return ChatColor.translateAlternateColorCodes('&', Main.Instance.config.getString("config.messageFormat") + message);
    }

    public static Boolean hasPermission(Player player, String permission) {
        return player.hasPermission(permission) || player.isOp();
    }

    public static Boolean hasPermission(CommandSender sender, String permission) {
        return sender.hasPermission(permission) || sender.isOp();
    }
}