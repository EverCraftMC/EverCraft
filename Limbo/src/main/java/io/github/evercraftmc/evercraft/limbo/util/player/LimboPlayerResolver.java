package io.github.evercraftmc.evercraft.limbo.util.player;

import java.util.UUID;
import com.loohp.limbo.player.Player;
import io.github.evercraftmc.evercraft.limbo.LimboMain;
import io.github.evercraftmc.evercraft.shared.config.MySQLConfig;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import io.github.evercraftmc.evercraft.shared.util.player.SimplePlayer;

public class LimboPlayerResolver {
    public static SimplePlayer getPlayer(MySQLConfig config, UUID uuid) {
        if (getNameFromUUID(config, uuid) != null) {
            return new SimplePlayer(uuid, getNameFromUUID(config, uuid), getNickname(config, uuid), getPrefix(uuid));
        } else {
            return null;
        }
    }

    public static SimplePlayer getPlayer(MySQLConfig config, String name) {
        if (getUUIDFromName(config, name) != null) {
            return new SimplePlayer(getUUIDFromName(config, name), name, getNickname(config, getUUIDFromName(config, name)), getPrefix(getUUIDFromName(config, name)));
        } else {
            return null;
        }
    }

    public static UUID getUUIDFromName(MySQLConfig config, String name) {
        for (String key : config.getKeys("players", false)) {
            if (config.getString(key + ".lastname").equalsIgnoreCase(name)) {
                return UUID.fromString(key.split("\\.")[1]);
            }
        }

        return null;
    }

    public static String getNameFromUUID(MySQLConfig config, UUID uuid) {
        return config.getString("players." + uuid.toString() + ".lastname");
    }

    public static String getDisplayName(MySQLConfig config, UUID uuid) {
        return getPrefix(uuid) + getNickname(config, uuid);
    }

    public static String getNickname(MySQLConfig config, UUID uuid) {
        if (config.getString("players." + uuid.toString() + ".nickname") != null) {
            Boolean needsStar = true;
            for (String string : TextFormatter.removeColors(config.getString("players." + uuid.toString() + ".nickname")).replace("_", "-").split("-")) {
                if (getNameFromUUID(config, uuid).toLowerCase().contains(string.toLowerCase())) {
                    needsStar = false;
                }
            }

            return config.getString("players." + uuid.toString() + ".nickname") + (needsStar ? "*" : "");
        } else {
            return getNameFromUUID(config, uuid);
        }
    }

    public static String getPrefix(UUID uuid) {
        return "";
    }

    public static Player limboPlayerFromPlayer(SimplePlayer player) {
        return LimboMain.getInstance().getServer().getPlayer(player.getUniqueId());
    }
}