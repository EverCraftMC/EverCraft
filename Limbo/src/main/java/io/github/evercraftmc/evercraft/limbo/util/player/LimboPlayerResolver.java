package io.github.evercraftmc.evercraft.limbo.util.player;

import io.github.evercraftmc.evercraft.shared.util.player.PlayerResolver;
import io.github.evercraftmc.evercraft.shared.util.player.SimplePlayer;
import com.loohp.limbo.player.Player;
import io.github.evercraftmc.evercraft.limbo.LimboMain;

public class LimboPlayerResolver extends PlayerResolver {
    public static Player limboPlayerFromPlayer(SimplePlayer player) {
        return LimboMain.getInstance().getServer().getPlayer(player.getUniqueId());
    }
}