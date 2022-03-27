package com.kale_ko.evercraft.shared.util.player;

import java.util.UUID;
import com.kale_ko.evercraft.shared.config.Config;

public class PlayerResolver {
    public static SimplePlayer getPlayer(Config config, UUID uuid) {
        if (getNameFromUUID(config, uuid) != null) {
            return new SimplePlayer(uuid, getNameFromUUID(config, uuid), getDisplayName(config, uuid));
        } else {
            return null;
        }
    }

    public static SimplePlayer getPlayer(Config config, String name) {
        System.out.println(getUUIDFromName(config, name));

        if (getUUIDFromName(config, name) != null) {
            return new SimplePlayer(getUUIDFromName(config, name), name, getDisplayName(config, getUUIDFromName(config, name)));
        } else {
            return null;
        }
    }

    public static UUID getUUIDFromName(Config config, String name) {
        for (String key : config.getKeys("players", false)) {
            if (config.getString(key + ".lastname").equalsIgnoreCase(name)) {
                return UUID.fromString(key.split("\\.")[1]);
            }
        }

        return null;
    }

    public static String getNameFromUUID(Config config, UUID uuid) {
        return config.getString("players." + uuid.toString() + ".lastname");
    }

    public static String getDisplayName(Config config, UUID uuid) {
        return config.getString("players." + uuid.toString() + ".nickname");
    }
}