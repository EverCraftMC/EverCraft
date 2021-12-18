package com.kale_ko.kales_utilities;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Util {
    public static void sendMessage(CommandSender user, String message) {
        user.sendMessage(defaultStyleMessage(Main.Instance.config.getString("config.prefix") + " " + message));
    }

    public static String formatMessage(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String styleMessage(String message, String style) {
        return ChatColor.translateAlternateColorCodes('&', style + message);
    }

    public static String defaultStyleMessage(String message) {
        return ChatColor.translateAlternateColorCodes('&', Main.Instance.config.getString("config.messageFormat") + message);
    }
}