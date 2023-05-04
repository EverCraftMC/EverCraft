package io.github.evercraftmc.evercraft.spigot.games.spleef;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class TntRunGame extends SpleefGame {
    public TntRunGame(String name, String warpName, Integer countdownLength, Material deathBlock) {
        super(name, warpName, countdownLength, null, deathBlock);
    }

    @Override
    public void tick() {
        super.tick();

        for (Player player : this.players) {
            // TODO Get closest block instead
            if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.TNT) {
                player.getLocation().getBlock().getRelative(BlockFace.DOWN).setType(Material.AIR);
            }
        }
    }
}