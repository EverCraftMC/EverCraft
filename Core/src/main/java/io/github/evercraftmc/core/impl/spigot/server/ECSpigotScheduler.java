package io.github.evercraftmc.core.impl.spigot.server;

import io.github.evercraftmc.core.api.server.ECScheduler;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class ECSpigotScheduler implements ECScheduler {
    public static class ECSpigotTask implements ECTask {
        protected BukkitTask handle;

        public ECSpigotTask(BukkitTask handle) {
            this.handle = handle;
        }

        public BukkitTask getHandle() {
            return this.handle;
        }

        public void cancel() {
            this.handle.cancel();
        }
    }

    protected ECSpigotServer server;

    public ECSpigotScheduler(ECSpigotServer server) {
        this.server = server;
    }

    public ECSpigotServer getServer() {
        return this.server;
    }

    @Override
    public ECTask runTask(Runnable task) {
        return new ECSpigotTask(this.server.getHandle().getScheduler().runTask((JavaPlugin) this.server.getPlugin().getHandle(), task));
    }

    @Override
    public ECTask runTaskAsync(Runnable task) {
        return new ECSpigotTask(this.server.getHandle().getScheduler().runTaskAsynchronously((JavaPlugin) this.server.getPlugin().getHandle(), task));
    }

    @Override
    public ECTask runTaskLater(Runnable task, int ticks) {
        return new ECSpigotTask(this.server.getHandle().getScheduler().runTaskLater((JavaPlugin) this.server.getPlugin().getHandle(), task, ticks));
    }

    @Override
    public ECTask runTaskLaterAsync(Runnable task, int ticks) {
        return new ECSpigotTask(this.server.getHandle().getScheduler().runTaskLaterAsynchronously((JavaPlugin) this.server.getPlugin().getHandle(), task, ticks));
    }

    @Override
    public ECTask runTaskRepeat(Runnable task, int delay, int ticks) {
        return new ECSpigotTask(this.server.getHandle().getScheduler().runTaskTimer((JavaPlugin) this.server.getPlugin().getHandle(), task, delay, ticks));
    }

    @Override
    public ECTask runTaskRepeatAsync(Runnable task, int delay, int ticks) {
        return new ECSpigotTask(this.server.getHandle().getScheduler().runTaskTimerAsynchronously((JavaPlugin) this.server.getPlugin().getHandle(), task, delay, ticks));
    }
}