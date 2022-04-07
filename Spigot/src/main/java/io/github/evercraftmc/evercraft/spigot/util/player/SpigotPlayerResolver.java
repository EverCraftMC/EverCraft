package io.github.evercraftmc.evercraft.spigot.util.player;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import io.github.evercraftmc.evercraft.shared.util.player.PlayerResolver;
import io.github.evercraftmc.evercraft.shared.util.player.SimplePlayer;
import io.github.evercraftmc.evercraft.spigot.SpigotMain;

public class SpigotPlayerResolver extends PlayerResolver {
    public static Player spigotPlayerFromPlayer(SimplePlayer player) {
        return SpigotMain.getInstance().getServer().getPlayer(player.getUniqueId());
    }

    public static OfflinePlayer offlineSpigotPlayerFromPlayer(SimplePlayer player) {
        return SpigotMain.getInstance().getServer().getOfflinePlayer(player.getUniqueId());
    }
}