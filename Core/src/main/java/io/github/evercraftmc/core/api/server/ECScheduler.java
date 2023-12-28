package io.github.evercraftmc.core.api.server;

import org.jetbrains.annotations.NotNull;

public interface ECScheduler {
    interface ECTask {
        void cancel();
    }

    @NotNull ECTask runTask(@NotNull Runnable task);

    @NotNull ECTask runTaskAsync(@NotNull Runnable task);

    @NotNull ECTask runTaskLater(@NotNull Runnable task, int ticks);

    @NotNull ECTask runTaskLaterAsync(@NotNull Runnable task, int ticks);

    @NotNull ECTask runTaskRepeat(@NotNull Runnable task, int delay, int ticks);

    @NotNull ECTask runTaskRepeatAsync(@NotNull Runnable task, int delay, int ticks);
}