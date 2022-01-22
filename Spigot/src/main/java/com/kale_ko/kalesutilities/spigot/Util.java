package com.kale_ko.kalesutilities.spigot;

import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import net.minecraft.nbt.MojangsonParser;
import net.minecraft.nbt.NBTTagCompound;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

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
        Main.Instance.getServer().broadcastMessage(formatMessage(Main.Instance.config.getString("config.prefix") + " " + message));
    }

    public static void broadcastMessage(String message, Boolean noprefix) {
        if (!noprefix) {
            Main.Instance.getServer().broadcastMessage(formatMessage(Main.Instance.config.getString("config.prefix") + " " + message));
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

    public static String getNoPermissionMessage(String permission) {
        return Main.Instance.config.getString("messages.noperms").replace("{permission}", permission);
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

        if (Main.Instance.players.getString(player.getName() + ".nickname") != null && !Main.Instance.players.getString(player.getName() + ".nickname").equalsIgnoreCase("") && !Main.Instance.players.getString(player.getName() + ".nickname").equalsIgnoreCase(" ")) {
            if (Util.hasPermission(player, "kalesutilities.nonickstar") || Main.Instance.players.getString(player.getName() + ".nickname").equalsIgnoreCase(name) || Util.stripFormating(Util.formatMessage(Main.Instance.players.getString(player.getName() + ".nickname") + "&r")).equalsIgnoreCase(name)) {
                name = Util.formatMessage(Main.Instance.players.getString(player.getName() + ".nickname") + "&r");
            } else {
                name = Util.formatMessage("*" + Main.Instance.players.getString(player.getName() + ".nickname") + "&r");
            }
        }

        return name;
    }

    public static String getPlayerPrefix(Player player) {
        String prefix = "";

        if (Main.Instance.players.getString(player.getName() + ".prefix") != null && !Main.Instance.players.getString(player.getName() + ".prefix").equalsIgnoreCase("") && !Main.Instance.players.getString(player.getName() + ".prefix").equalsIgnoreCase(" ")) {
            prefix = Util.formatMessage(Main.Instance.players.getString(player.getName() + ".prefix") + "&r") + " ";
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

    public static void resetPlayerName(Player player) {
        player.setCustomName(player.getName());
        player.setCustomNameVisible(false);
        player.setDisplayName(player.getName());
        player.setPlayerListName(player.getName());
    }

    public static boolean hasMetadata(Player player, String key) {
        List<MetadataValue> metadataKey = player.getMetadata(key);

        for (MetadataValue metadata : metadataKey) {
            if (metadata.getOwningPlugin() == Main.Instance) {
                return true;
            }
        }

        return false;
    }

    public static MetadataValue getMetadata(Player player, String key) {
        List<MetadataValue> metadataKey = player.getMetadata(key);

        for (MetadataValue metadata : metadataKey) {
            if (metadata.getOwningPlugin() == Main.Instance) {
                return metadata;
            }
        }

        return null;
    }

    public static void setMetadata(Player player, String key, Object value) {
        player.setMetadata(key, new FixedMetadataValue(Main.Instance, value));
    }

    public static void removeMetadata(Player player, String key) {
        player.removeMetadata(key, Main.Instance);
    }

    public static NBTTagCompound parseNBT(String nbt) {
        try {
            return MojangsonParser.a(nbt);
        } catch (CommandSyntaxException err) {
            err.printStackTrace();
        }

        return null;
    }
}