package io.github.evercraftmc.core.api.server;

public interface ECScheduler {
    interface ECTask {
        void cancel();
    }

    ECTask runTask(Runnable task);

    ECTask runTaskAsync(Runnable task);

    ECTask runTaskLater(Runnable task, int ticks);

    ECTask runTaskLaterAsync(Runnable task, int ticks);

    ECTask runTaskRepeat(Runnable task, int delay, int ticks);

    ECTask runTaskRepeatAsync(Runnable task, int delay, int ticks);
}