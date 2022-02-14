package com.kale_ko.kalesutilities.bungee;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class Util {
    public static void sendMessage(CommandSender user, String message) {
        user.sendMessage(stringToBungeeComponent(formatMessage(BungeePlugin.Instance.config.getString("config.prefix") + " " + message)));
    }

    public static void sendMessage(CommandSender user, String message, Boolean noprefix) {
        if (!noprefix) {
            user.sendMessage(stringToBungeeComponent(formatMessage(BungeePlugin.Instance.config.getString("config.prefix") + " " + message)));
        } else {
            user.sendMessage(stringToBungeeComponent(formatMessage(message)));
        }
    }

    public static void broadcastMessage(String message) {
        BungeePlugin.Instance.getProxy().broadcast(stringToBungeeComponent(formatMessage(BungeePlugin.Instance.config.getString("config.prefix") + " " + message)));
    }

    public static void broadcastMessage(String message, Boolean noprefix) {
        if (!noprefix) {
            BungeePlugin.Instance.getProxy().broadcast(stringToBungeeComponent(formatMessage(BungeePlugin.Instance.config.getString("config.prefix") + " " + message)));
        } else {
            BungeePlugin.Instance.getProxy().broadcast(stringToBungeeComponent(formatMessage(message)));
        }
    }

    public static void messageServers(String... messages) {
        for (Map.Entry<String, ServerInfo> server : BungeePlugin.Instance.getProxy().getServers().entrySet()) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            for (String message : messages) {
                out.writeUTF(message);
            }

            if (!server.getValue().getPlayers().isEmpty()) {
                server.getValue().sendData("BungeeCord", out.toByteArray());
            }
        }
    }

    public static BaseComponent[] stringToBungeeComponent(String string) {
        return TextComponent.fromLegacyText(string);
    }

    public static String formatMessage(String message) {
        if (message != null) {
            return ChatColor.translateAlternateColorCodes('&', message);
        } else {
            return null;
        }
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
        if (message != null) {
            return ChatColor.stripColor(message);
        } else {
            return null;
        }
    }

    public static String discordFormating(String message) {
        if (message != null) {
            return stripFormating(formatMessage(message)).replace("_", "\\_").replace("*", "\\*").replace("~", "\\~").replace("`", "\\`").replace("@everyone", "\\@everyone");
        } else {
            return null;
        }
    }

    public static String getNoPermissionMessage(String permission) {
        return BungeePlugin.Instance.config.getString("messages.noperms").replace("{permission}", permission);
    }

    public static Boolean hasPermission(ProxiedPlayer player, String permission) {
        if (permission != null) {
            return player.hasPermission(permission);
        } else {
            return true;
        }
    }

    public static Boolean hasPermission(CommandSender sender, String permission) {
        if (sender == BungeePlugin.Instance.getProxy().getConsole()) {
            return true;
        } else {
            if (permission != null) {
                return sender.hasPermission(permission);
            } else {
                return true;
            }
        }
    }

    public static String getPlayerNickName(ProxiedPlayer player) {
        String name = player.getName();

        if (BungeePlugin.Instance.players.getString(player.getName() + ".nickname") != null && !BungeePlugin.Instance.players.getString(player.getName() + ".nickname").equalsIgnoreCase("") && !BungeePlugin.Instance.players.getString(player.getName() + ".nickname").equalsIgnoreCase(" ")) {
            if (Util.hasPermission(player, "kalesutilities.nonickstar") || BungeePlugin.Instance.players.getString(player.getName() + ".nickname").equalsIgnoreCase(name) || Util.stripFormating(Util.formatMessage(BungeePlugin.Instance.players.getString(player.getName() + ".nickname") + "&r")).equalsIgnoreCase(name)) {
                name = Util.formatMessage(BungeePlugin.Instance.players.getString(player.getName() + ".nickname") + "&r");
            } else {
                name = Util.formatMessage("*" + BungeePlugin.Instance.players.getString(player.getName() + ".nickname") + "&r");
            }
        }

        return name;
    }

    public static String getPlayerPrefix(ProxiedPlayer player) {
        return Util.formatMessage(BungeePlugin.Instance.luckperms.getPlayerAdapter(ProxiedPlayer.class).getUser(player).getCachedData().getMetaData().getPrefix());
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