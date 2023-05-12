package io.github.evercraftmc.core.api.server;

public interface ECScheduler {
    public static interface ECTask {
        public void cancel();
    }

    public ECTask runTask(Runnable task);

    public ECTask runTaskAsync(Runnable task);

    public ECTask runTaskLater(Runnable task, int ticks);

    public ECTask runTaskLaterAsync(Runnable task, int ticks);

    public ECTask runTaskRepeat(Runnable task, int delay, int ticks);

    public ECTask runTaskRepeatAsync(Runnable task, int delay, int ticks);
}