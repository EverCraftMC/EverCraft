package io.github.evercraftmc.evercraft.spigot.util.player;

import java.util.UUID;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import io.github.evercraftmc.evercraft.shared.PluginData;
import io.github.evercraftmc.evercraft.shared.util.player.SimplePlayer;
import io.github.evercraftmc.evercraft.spigot.SpigotMain;
import io.github.kale_ko.ejcl.StructuredConfig;

public class SpigotPlayerResolver {
    public static SimplePlayer getPlayer(StructuredConfig<PluginData> config, UUID uuid) {
        if (getNameFromUUID(config, uuid) != null) {
            return new SimplePlayer(uuid, getNameFromUUID(config, uuid));
        } else {
            return null;
        }
    }

    public static SimplePlayer getPlayer(StructuredConfig<PluginData> config, String name) {
        if (getUUIDFromName(config, name) != null) {
            return new SimplePlayer(getUUIDFromName(config, name), name);
        } else {
            return null;
        }
    }

    public static UUID getUUIDFromName(StructuredConfig<PluginData> config, String name) {
        for (String key : config.get().players.keySet()) {
            if (config.get().players.get(key).lastName.equalsIgnoreCase(name)) {
                return UUID.fromString(config.get().players.get(key).uuid);
            }
        }

        return null;
    }

    public static String getNameFromUUID(StructuredConfig<PluginData> config, UUID uuid) {
        return config.get().players.get(uuid.toString()).lastName;
    }

    public static Player spigotPlayerFromPlayer(SimplePlayer player) {
        return SpigotMain.getInstance().getServer().getPlayer(player.getUniqueId());
    }

    public static OfflinePlayer offlineSpigotPlayerFromPlayer(SimplePlayer player) {
        return SpigotMain.getInstance().getServer().getOfflinePlayer(player.getUniqueId());
    }
}