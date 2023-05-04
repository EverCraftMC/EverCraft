package io.github.evercraftmc.evercraft.spigot.games.spleef;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import io.github.evercraftmc.evercraft.spigot.SpigotMain;

public class TntRunGame extends SpleefGame {
    public TntRunGame(String name, String warpName, Integer countdownLength, Material deathBlock) {
        super(name, warpName, countdownLength, null, deathBlock);
    }

    @Override
    public void tick() {
        super.tick();

        for (Player player : this.players) {
            Block block = null;
            for (int z = -1; z <= 1; z++) {
                for (int x = -1; x <= 1; x++) {
                    Block block2 = player.getLocation().add(x, -0.5, z).getBlock();

                    if (block2.getType() == Material.TNT && (block == null || block2.getLocation().distance(player.getLocation()) <= block.getLocation().distance(player.getLocation()))) {
                        block = block2;
                    }
                }
            }

            final Block block2 = block;

            if (block2 != null) {
                SpigotMain.getInstance().getServer().getScheduler().runTaskLater(SpigotMain.getInstance(), () -> {
                    if (started && block2.getType() == Material.TNT) {
                        brokenBlocks.put(block2.getLocation(), block2.getType());
                        block2.setType(Material.AIR);
                    }
                }, 2);
            }
        }
    }
}