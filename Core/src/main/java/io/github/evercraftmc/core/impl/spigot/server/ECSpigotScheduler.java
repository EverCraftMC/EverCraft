package io.github.evercraftmc.core.impl.spigot.server;

import io.github.evercraftmc.core.api.server.ECScheduler;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public class ECSpigotScheduler implements ECScheduler {
    public static class ECSpigotTask implements ECTask {
        protected final @NotNull BukkitTask handle;

        public ECSpigotTask(@NotNull BukkitTask handle) {
            this.handle = handle;
        }

        public @NotNull BukkitTask getHandle() {
            return this.handle;
        }

        public void cancel() {
            this.handle.cancel();
        }
    }

    protected final @NotNull ECSpigotServer server;

    public ECSpigotScheduler(@NotNull ECSpigotServer server) {
        this.server = server;
    }

    public @NotNull ECSpigotServer getServer() {
        return this.server;
    }

    @Override
    public @NotNull ECSpigotTask runTask(@NotNull Runnable task) {
        return new ECSpigotTask(this.server.getHandle().getScheduler().runTask((Plugin) this.server.getPlugin().getHandle(), task));
    }

    @Override
    public @NotNull ECSpigotTask runTaskAsync(@NotNull Runnable task) {
        return new ECSpigotTask(this.server.getHandle().getScheduler().runTaskAsynchronously((Plugin) this.server.getPlugin().getHandle(), task));
    }

    @Override
    public @NotNull ECSpigotTask runTaskLater(@NotNull Runnable task, int ticks) {
        return new ECSpigotTask(this.server.getHandle().getScheduler().runTaskLater((Plugin) this.server.getPlugin().getHandle(), task, ticks));
    }

    @Override
    public @NotNull ECSpigotTask runTaskLaterAsync(@NotNull Runnable task, int ticks) {
        return new ECSpigotTask(this.server.getHandle().getScheduler().runTaskLaterAsynchronously((Plugin) this.server.getPlugin().getHandle(), task, ticks));
    }

    @Override
    public @NotNull ECSpigotTask runTaskRepeat(@NotNull Runnable task, int delay, int ticks) {
        return new ECSpigotTask(this.server.getHandle().getScheduler().runTaskTimer((Plugin) this.server.getPlugin().getHandle(), task, delay, ticks));
    }

    @Override
    public @NotNull ECSpigotTask runTaskRepeatAsync(@NotNull Runnable task, int delay, int ticks) {
        return new ECSpigotTask(this.server.getHandle().getScheduler().runTaskTimerAsynchronously((Plugin) this.server.getPlugin().getHandle(), task, delay, ticks));
    }
}