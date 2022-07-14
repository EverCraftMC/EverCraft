package io.github.evercraftmc.evercraft.spigot.games.race;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPlaceEvent;
import io.github.evercraftmc.evercraft.spigot.commands.kit.KitCommand;
import io.github.evercraftmc.evercraft.spigot.games.Game;

public class BoatRaceGame extends Game {
    protected String kitName;

    protected Map<Player, List<Entity>> boats = new HashMap<Player, List<Entity>>();

    public BoatRaceGame(String name, String warpName, String kitName) {
        super(name, warpName, 1f, Float.MAX_VALUE);

        this.kitName = kitName;
    }

    public String getKitName() {
        return this.kitName;
    }

    @Override
    public void join(Player player) {
        super.join(player);

        new KitCommand("kit", null, Arrays.asList(), null).run(player, new String[] { kitName, "true" });
    }

    @Override
    public void leave(Player player, LeaveReason leaveReason) {
        super.leave(player, leaveReason);

        if (this.boats.containsKey(player)) {
            for (Entity entity : this.boats.get(player)) {
                entity.remove();
            }

            this.boats.remove(player);
        }
    }

    @EventHandler
    public void onBoatPlace(EntityPlaceEvent event) {
        if (event.getEntityType() == EntityType.BOAT && this.players.contains(event.getPlayer())) {
            if (!this.boats.containsKey(event.getPlayer())) {
                this.boats.put(event.getPlayer(), new ArrayList<Entity>());
            }

            this.boats.get(event.getPlayer()).add(event.getEntity());
        }
    }
}