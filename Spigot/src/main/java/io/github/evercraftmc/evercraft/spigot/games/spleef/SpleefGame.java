package io.github.evercraftmc.evercraft.spigot.games.spleef;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import io.github.evercraftmc.evercraft.spigot.commands.kit.KitCommand;
import io.github.evercraftmc.evercraft.spigot.games.RoundedGame;

public class SpleefGame extends RoundedGame {
    protected String kitName;

    protected Material deathBlock;

    protected Map<Location, Material> brokenBlocks = new HashMap<Location, Material>();

    public SpleefGame(String name, String warpName, Integer countdownLength, String kitName, Material deathBlock) {
        super(name, warpName, 2, Integer.MAX_VALUE, countdownLength);

        this.kitName = kitName;

        this.deathBlock = deathBlock;
    }

    @Override
    public void startNoCountdown() {
        super.startNoCountdown();

        if (this.kitName != null) {
            for (Player player : this.players) {
                new KitCommand("kit", null, Arrays.asList(), null, true).run(player, new String[] { kitName });
            }
        }
    }

    @Override
    public void join(Player player) {
        super.join(player);

        if (this.players.size() >= this.minPlayers) {
            this.start();
        }
    }

    @Override
    public void stop() {
        super.stop();

        for (Map.Entry<Location, Material> entry : this.brokenBlocks.entrySet()) {
            entry.getKey().getBlock().setType(entry.getValue());
        }
        this.brokenBlocks.clear();
    }

    @Override
    public void tick() {
        super.tick();

        for (Player player : this.players) {
            if (player.getLocation().getBlock().getType() == this.deathBlock) {
                player.damage(Integer.MAX_VALUE);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (this.players.contains(event.getPlayer())) {
            if (started && event.getBlock().getType() == Material.SNOW_BLOCK) {
                brokenBlocks.put(event.getBlock().getLocation(), event.getBlock().getType());

                event.setDropItems(false);
                event.setExpToDrop(0);
            } else {
                event.setCancelled(true);
            }
        }
    }
}