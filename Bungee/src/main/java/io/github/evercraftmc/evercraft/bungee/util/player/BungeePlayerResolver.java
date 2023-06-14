package io.github.evercraftmc.evercraft.bungee.util.player;

import java.util.UUID;
import io.github.evercraftmc.evercraft.bungee.BungeeMain;
import io.github.evercraftmc.evercraft.shared.PluginData;
import io.github.evercraftmc.evercraft.shared.util.player.SimplePlayer;
import io.github.kale_ko.ejcl.StructuredConfig;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BungeePlayerResolver {
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

    public static ProxiedPlayer bungeePlayerFromPlayer(SimplePlayer player) {
        return BungeeMain.getInstance().getProxy().getPlayer(player.getUniqueId());
    }

    public static SimplePlayer playerFromConnection(StructuredConfig<PluginData> config, Connection connection) {
        for (ProxiedPlayer player : BungeeMain.getInstance().getProxy().getPlayers()) {
            if (player.getPendingConnection().getSocketAddress() == connection.getSocketAddress()) {
                return new SimplePlayer(player.getUniqueId(), player.getName());
            }
        }

        return null;
    }
}