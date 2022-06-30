package io.github.evercraftmc.evercraft.spigot.util.player;

import java.util.UUID;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import io.github.evercraftmc.evercraft.shared.config.Config;
import io.github.evercraftmc.evercraft.shared.util.player.PlayerResolver;
import io.github.evercraftmc.evercraft.shared.util.player.SimplePlayer;
import io.github.evercraftmc.evercraft.spigot.SpigotMain;
import net.luckperms.api.LuckPermsProvider;

public class SpigotPlayerResolver extends PlayerResolver {
    public static Player spigotPlayerFromPlayer(SimplePlayer player) {
        return SpigotMain.getInstance().getServer().getPlayer(player.getUniqueId());
    }

    public static OfflinePlayer offlineSpigotPlayerFromPlayer(SimplePlayer player) {
        return SpigotMain.getInstance().getServer().getOfflinePlayer(player.getUniqueId());
    }

    public static String getDisplayName(Config config, UUID uuid) {
        return LuckPermsProvider.get().getUserManager().getUser(uuid).getCachedData().getMetaData().getPrefix() + PlayerResolver.getDisplayName(config, uuid);
    }
}