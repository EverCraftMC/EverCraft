package io.github.evercraftmc.evercraft.limbo.listeners;

import com.loohp.limbo.events.EventHandler;
import com.loohp.limbo.events.player.PlayerJoinEvent;
import com.loohp.limbo.events.player.PlayerMoveEvent;
import io.github.evercraftmc.evercraft.limbo.LimboMain;

public class SpawnListener extends LimboListener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().teleport(LimboMain.getInstance().getPluginConfig().getParsed().spawnLocation.toLimboLocation());
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getTo().getY() < -64) {
            event.getPlayer().teleport(LimboMain.getInstance().getPluginConfig().getParsed().spawnLocation.toLimboLocation());
        }
    }
}