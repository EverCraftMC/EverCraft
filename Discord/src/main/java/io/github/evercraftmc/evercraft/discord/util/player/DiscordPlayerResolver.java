package io.github.evercraftmc.evercraft.discord.util.player;

import java.util.UUID;
import io.github.evercraftmc.evercraft.shared.PluginData;
import io.github.evercraftmc.evercraft.shared.config.Config;

public class DiscordPlayerResolver {
    public static UUID getUUIDFromID(Config<PluginData> config, String id) {
        for (String key : config.getParsed().players.keySet()) {
            if (config.getParsed().players.get(key).discordAccount != null && config.getParsed().players.get(key).discordAccount.equals(id)) {
                return UUID.fromString(config.getParsed().players.get(key).uuid);
            }
        }

        return null;
    }
}