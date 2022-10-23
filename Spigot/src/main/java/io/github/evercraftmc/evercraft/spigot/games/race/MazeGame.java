package io.github.evercraftmc.evercraft.spigot.games.race;

import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import io.github.evercraftmc.evercraft.spigot.SpigotMain;
import io.github.evercraftmc.evercraft.spigot.games.Game;

public class MazeGame extends Game {
    protected String finishWarpName;

    public MazeGame(String name, String warpName, String finishWarpName) {
        super(name, warpName, 1f, Float.MAX_VALUE);

        this.finishWarpName = finishWarpName;

        // TODO Maybe dynamicly generate the maze
    }

    @EventHandler
    public void onPlayerFinish(PlayerInteractEvent event) {
        if (event.getAction() == Action.PHYSICAL && event.getClickedBlock() != null && event.getClickedBlock().getType().toString().endsWith("_PRESSURE_PLATE")) {
            if (event.getClickedBlock().getLocation().toBlockLocation().equals(SpigotMain.getInstance().getWarps().getParsed().warps.get(this.finishWarpName).toBukkitLocation().toBlockLocation())) {
                event.setCancelled(true);

                this.leave(event.getPlayer(), LeaveReason.GAMEOVER);
            }
        }
    }
}