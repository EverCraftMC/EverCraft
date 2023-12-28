package io.github.evercraftmc.core.api;

import io.github.evercraftmc.core.ECPlugin;
import org.jetbrains.annotations.NotNull;

public interface ECModule {
    @NotNull String getName();

    @NotNull ECModuleInfo getInfo();

    void setInfo(@NotNull ECModuleInfo info);

    @NotNull ECPlugin getPlugin();

    void setPlugin(@NotNull ECPlugin plugin);

    void load();

    void unload();
}