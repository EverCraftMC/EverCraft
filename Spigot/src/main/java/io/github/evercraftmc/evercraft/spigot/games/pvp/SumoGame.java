package io.github.evercraftmc.evercraft.spigot.games.pvp;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class SumoGame extends PvpGame {
    protected Material deathBlock;
    protected Integer deathDamage;

    public SumoGame(String name, String warpName, String kitName, Material deathBlock, Integer deathDamage) {
        super(name, warpName, kitName);

        this.deathBlock = deathBlock;
        this.deathDamage = deathDamage;
    }

    @Override
    public void tick() {
        super.tick();

        for (Player player : this.players) {
            if (player.getLocation().getBlock().getType() == this.deathBlock) {
                player.damage(this.deathDamage);
            }
        }
    }
}