package com.kale_ko.evercraft.spigot;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import net.minecraft.nbt.MojangsonParser;
import net.minecraft.nbt.NBTTagCompound;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class Util {
    public static void sendMessage(CommandSender user, String message) {
        user.sendMessage(formatMessage(SpigotPlugin.Instance.config.getString("config.prefix") + " " + message));
    }

    public static void sendMessage(CommandSender user, String message, Boolean noprefix) {
        if (!noprefix) {
            user.sendMessage(formatMessage(SpigotPlugin.Instance.config.getString("config.prefix") + " " + message));
        } else {
            user.sendMessage(formatMessage(message));
        }
    }

    public static void broadcastMessage(String message) {
        SpigotPlugin.Instance.getServer().broadcastMessage(formatMessage(SpigotPlugin.Instance.config.getString("config.prefix") + " " + message));
    }

    public static void broadcastMessage(String message, Boolean noprefix) {
        if (!noprefix) {
            SpigotPlugin.Instance.getServer().broadcastMessage(formatMessage(SpigotPlugin.Instance.config.getString("config.prefix") + " " + message));
        } else {
            SpigotPlugin.Instance.getServer().broadcastMessage(formatMessage(message));
        }
    }

    public static void messageBungee(String... messages) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        for (String message : messages) {
            out.writeUTF(message);
        }

        Player player = (Player) SpigotPlugin.Instance.getServer().getOnlinePlayers().toArray()[0];

        player.sendPluginMessage(SpigotPlugin.Instance, "BungeeCord", out.toByteArray());
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
        return SpigotPlugin.Instance.config.getString("messages.noperms").replace("{permission}", permission);
    }

    public static Boolean hasPermission(Player player, String permission) {
        if (permission != null) {
            return player.hasPermission(permission) || player.isOp();
        } else {
            return true;
        }
    }

    public static Boolean hasPermission(CommandSender sender, String permission) {
        if (sender instanceof ConsoleCommandSender) {
            return true;
        } else {
            if (permission != null) {
                return sender.hasPermission(permission) || sender.isOp();
            } else {
                return true;
            }
        }
    }

    public static String getPlayerNickName(Player player) {
        String name = player.getName();

        if (SpigotPlugin.Instance.players.getString(player.getName() + ".nickname") != null && !SpigotPlugin.Instance.players.getString(player.getName() + ".nickname").equalsIgnoreCase("") && !SpigotPlugin.Instance.players.getString(player.getName() + ".nickname").equalsIgnoreCase(" ")) {
            if (Util.hasPermission(player, "kalesutilities.nonickstar") || SpigotPlugin.Instance.players.getString(player.getName() + ".nickname").equalsIgnoreCase(name) || Util.stripFormating(Util.formatMessage(SpigotPlugin.Instance.players.getString(player.getName() + ".nickname") + "&r")).equalsIgnoreCase(name)) {
                name = Util.formatMessage(SpigotPlugin.Instance.players.getString(player.getName() + ".nickname") + "&r");
            } else {
                name = Util.formatMessage("*" + SpigotPlugin.Instance.players.getString(player.getName() + ".nickname") + "&r");
            }
        }

        return name;
    }

    public static String getPlayerPrefix(Player player) {
        return Util.formatMessage(SpigotPlugin.Instance.luckperms.getPlayerAdapter(Player.class).getUser(player).getCachedData().getMetaData().getPrefix());
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
            if (metadata.getOwningPlugin() == SpigotPlugin.Instance) {
                return true;
            }
        }

        return false;
    }

    public static MetadataValue getMetadata(Player player, String key) {
        List<MetadataValue> metadataKey = player.getMetadata(key);

        for (MetadataValue metadata : metadataKey) {
            if (metadata.getOwningPlugin() == SpigotPlugin.Instance) {
                return metadata;
            }
        }

        return null;
    }

    public static void setMetadata(Player player, String key, Object value) {
        player.setMetadata(key, new FixedMetadataValue(SpigotPlugin.Instance, value));
    }

    public static void removeMetadata(Player player, String key) {
        player.removeMetadata(key, SpigotPlugin.Instance);
    }

    public static NBTTagCompound parseNBT(String nbt) {
        try {
            return MojangsonParser.a(nbt);
        } catch (CommandSyntaxException err) {
            err.printStackTrace();
        }

        return null;
    }

    public static <T, N> Map<T, N> mapFromLists(List<T> keys, List<N> values) {
        Map<T, N> newMap = new HashMap<T, N>();

        for (T key : keys) {
            newMap.put(key, values.get(keys.indexOf(key)));
        }

        return newMap;
    }

    private static final String[] currencyLetters = new String[] {
            "",
            "",
            "M",
            "B",
            "T"
    };

    public static String formatCurrencyMin(double currency) {
        String currencyString = NumberFormat.getCurrencyInstance(Locale.US).format(currency);
        String currencyMin = currencyString;
        if (currency > 999999) {
            currencyMin = currencyString.split(",")[0] + "." + currencyString.split(",")[1].substring(0, 2) + currencyLetters[currencyString.split(",").length - 1];
        }

        return currencyMin;
    }

    public static String formatDurationLetters(long durationMillis, boolean suppressLeadingZeroElements, boolean suppressTrailingZeroElements) {
        String duration = DurationFormatUtils.formatDuration(durationMillis, "d'd 'H'h 'm'm 's's'");

        if (suppressLeadingZeroElements) {
            duration = " " + duration;
            String tmp = StringUtils.replaceOnce(duration, " 0d", "");

            if (tmp.length() != duration.length()) {
                duration = tmp;
                tmp = StringUtils.replaceOnce(duration, " 0h", "");

                if (tmp.length() != duration.length()) {
                    duration = tmp;
                    tmp = StringUtils.replaceOnce(duration, " 0m", "");
                    duration = tmp;

                    if (tmp.length() != duration.length()) {
                        duration = StringUtils.replaceOnce(tmp, " 0s", "");
                    }
                }
            }

            if (duration.length() != 0) {
                duration = duration.substring(1);
            }
        }

        if (suppressTrailingZeroElements) {
            String tmp = StringUtils.replaceOnce(duration, " 0s", "");

            if (tmp.length() != duration.length()) {
                duration = tmp;
                tmp = StringUtils.replaceOnce(duration, " 0m", "");

                if (tmp.length() != duration.length()) {
                    duration = tmp;
                    tmp = StringUtils.replaceOnce(duration, " 0h", "");

                    if (tmp.length() != duration.length()) {
                        duration = StringUtils.replaceOnce(tmp, " 0d", "");
                    }
                }
            }
        }

        return duration.trim();
    }
}