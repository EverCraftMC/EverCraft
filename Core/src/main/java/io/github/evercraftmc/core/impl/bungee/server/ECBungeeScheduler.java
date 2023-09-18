package io.github.evercraftmc.core.impl.bungee.server;

import io.github.evercraftmc.core.api.server.ECScheduler;
import java.util.concurrent.TimeUnit;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;

public class ECBungeeScheduler implements ECScheduler {
    public static class ECBungeeTask implements ECTask {
        protected ScheduledTask handle;

        public ECBungeeTask(ScheduledTask handle) {
            this.handle = handle;
        }

        public ScheduledTask getHandle() {
            return this.handle;
        }

        public void cancel() {
            this.handle.cancel();
        }
    }

    protected ECBungeeServer server;

    public ECBungeeScheduler(ECBungeeServer server) {
        this.server = server;
    }

    public ECBungeeServer getServer() {
        return this.server;
    }

    @Override
    public ECTask runTask(Runnable task) {
        return this.runTaskLater(task, 0);
    }

    @Override
    public ECTask runTaskAsync(Runnable task) {
        return new ECBungeeTask(this.server.getHandle().getScheduler().runAsync((Plugin) this.server.getPlugin().getHandle(), task));
    }

    @Override
    public ECTask runTaskLater(Runnable task, int ticks) {
        return this.runTaskLaterAsync(task, ticks);
    }

    @Override
    public ECTask runTaskLaterAsync(Runnable task, int ticks) {
        return new ECBungeeTask(this.server.getHandle().getScheduler().schedule((Plugin) this.server.getPlugin().getHandle(), task, ticks * 50L, TimeUnit.MILLISECONDS));
    }

    @Override
    public ECTask runTaskRepeat(Runnable task, int delay, int ticks) {
        return this.runTaskRepeatAsync(task, delay, ticks);
    }

    @Override
    public ECTask runTaskRepeatAsync(Runnable task, int delay, int ticks) {
        return new ECBungeeTask(this.server.getHandle().getScheduler().schedule((Plugin) this.server.getPlugin().getHandle(), task, delay * 50L, ticks * 50L, TimeUnit.MILLISECONDS));
    }
}