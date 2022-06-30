package io.github.evercraftmc.evercraft.bungee.util.player;

import java.util.UUID;
import io.github.evercraftmc.evercraft.bungee.BungeeMain;
import io.github.evercraftmc.evercraft.shared.config.Config;
import io.github.evercraftmc.evercraft.shared.util.player.PlayerResolver;
import io.github.evercraftmc.evercraft.shared.util.player.SimplePlayer;
import net.luckperms.api.LuckPermsProvider;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BungeePlayerResolver extends PlayerResolver {
    public static ProxiedPlayer bungeePlayerFromPlayer(SimplePlayer player) {
        return BungeeMain.getInstance().getProxy().getPlayer(player.getUniqueId());
    }

    public static String getDisplayName(Config config, UUID uuid) {
        return LuckPermsProvider.get().getUserManager().getUser(uuid).getCachedData().getMetaData().getPrefix() + PlayerResolver.getDisplayName(config, uuid);
    }

    public static SimplePlayer playerFromConnection(Connection connection) {
        for (ProxiedPlayer player : BungeeMain.getInstance().getProxy().getPlayers()) {
            if (player.getPendingConnection().getSocketAddress() == connection.getSocketAddress()) {
                return new SimplePlayer(player.getUniqueId(), player.getName(), player.getDisplayName());
            }
        }

        return null;
    }
}