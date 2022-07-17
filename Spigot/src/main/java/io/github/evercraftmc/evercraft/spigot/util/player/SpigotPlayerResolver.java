package io.github.evercraftmc.evercraft.spigot.util.player;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import io.github.evercraftmc.evercraft.shared.config.MySQLConfig;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import io.github.evercraftmc.evercraft.shared.util.player.SimplePlayer;
import io.github.evercraftmc.evercraft.spigot.SpigotMain;
import net.luckperms.api.LuckPermsProvider;

public class SpigotPlayerResolver {
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
        try {
            return LuckPermsProvider.get().getUserManager().loadUser(uuid).get().getCachedData().getMetaData().getPrefix();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Player spigotPlayerFromPlayer(SimplePlayer player) {
        return SpigotMain.getInstance().getServer().getPlayer(player.getUniqueId());
    }

    public static OfflinePlayer offlineSpigotPlayerFromPlayer(SimplePlayer player) {
        return SpigotMain.getInstance().getServer().getOfflinePlayer(player.getUniqueId());
    }
}