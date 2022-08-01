package io.github.evercraftmc.evercraft.spigot.util.player;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import io.github.evercraftmc.evercraft.shared.PluginData;
import io.github.evercraftmc.evercraft.shared.config.Config;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import io.github.evercraftmc.evercraft.shared.util.player.SimplePlayer;
import io.github.evercraftmc.evercraft.spigot.SpigotMain;
import net.luckperms.api.LuckPermsProvider;

public class SpigotPlayerResolver {
    public static SimplePlayer getPlayer(Config<PluginData> config, UUID uuid) {
        if (getNameFromUUID(config, uuid) != null) {
            return new SimplePlayer(uuid, getNameFromUUID(config, uuid), getNickname(config, uuid), getPrefix(uuid));
        } else {
            return null;
        }
    }

    public static SimplePlayer getPlayer(Config<PluginData> config, String name) {
        if (getUUIDFromName(config, name) != null) {
            return new SimplePlayer(getUUIDFromName(config, name), name, getNickname(config, getUUIDFromName(config, name)), getPrefix(getUUIDFromName(config, name)));
        } else {
            return null;
        }
    }

    public static UUID getUUIDFromName(Config<PluginData> config, String name) {
        for (String key : config.getParsed().players.keySet()) {
            if (config.getParsed().players.get(key).lastName.equalsIgnoreCase(name)) {
                return UUID.fromString(config.getParsed().players.get(key).uuid);
            }
        }

        return null;
    }

    public static String getNameFromUUID(Config<PluginData> config, UUID uuid) {
        return config.getParsed().players.get(uuid.toString()).lastName;
    }

    public static String getDisplayName(Config<PluginData> config, UUID uuid) {
        return getPrefix(uuid) + getNickname(config, uuid);
    }

    public static String getNickname(Config<PluginData> config, UUID uuid) {
        if (config.getParsed().players.get(uuid.toString()).nickname != null) {
            Boolean needsStar = true;
            for (String string : TextFormatter.removeColors(config.getParsed().players.get(uuid.toString()).nickname).replace("_", "-").split("-")) {
                if (getNameFromUUID(config, uuid).toLowerCase().contains(string.toLowerCase())) {
                    needsStar = false;
                }
            }

            return config.getParsed().players.get(uuid.toString()).nickname + (needsStar ? "*" : "");
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