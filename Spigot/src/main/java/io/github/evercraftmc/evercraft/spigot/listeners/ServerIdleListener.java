package io.github.evercraftmc.evercraft.spigot.listeners;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;
import io.github.evercraftmc.evercraft.spigot.SpigotMain;

public class ServerIdleListener extends SpigotListener {
    private static Boolean serverFrozen = true;

    private BukkitTask freezeTask = null;

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (serverFrozen) {
            serverFrozen = false;
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerLeave(PlayerQuitEvent event) {
        if (!serverFrozen && SpigotMain.getInstance().getServer().getOnlinePlayers().size() - 1 == 0) {
            serverFrozen = true;
        }
    }

    @Override
    public SpigotListener register() {
        this.freezeTask = SpigotMain.getInstance().getServer().getScheduler().runTaskTimer(SpigotMain.getInstance(), new Runnable() {
            @Override
            public void run() {
                if (serverFrozen) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    for (World world : SpigotMain.getInstance().getServer().getWorlds()) {
                        for (Chunk chunk : world.getLoadedChunks()) {
                            chunk.unload(true);
                        }
                    }
                }
            }
        }, 200, 1);

        return super.register();
    }

    @Override
    public void unregister() {
        this.freezeTask.cancel();

        super.unregister();
    }
}