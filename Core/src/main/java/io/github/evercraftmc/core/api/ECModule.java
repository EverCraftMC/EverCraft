package io.github.evercraftmc.core.api;

import io.github.evercraftmc.core.ECPlugin;
import org.jetbrains.annotations.NotNull;

public abstract class ECModule {
    protected ECModuleInfo info;

    protected ECPlugin plugin;

    public @NotNull String getName() {
        return this.getInfo().getName();
    }

    public @NotNull ECModuleInfo getInfo() {
        return this.info;
    }

    public void setInfo(@NotNull ECModuleInfo info) {
        this.info = info;
    }

    public @NotNull ECPlugin getPlugin() {
        return this.plugin;
    }

    public void setPlugin(@NotNull ECPlugin plugin) {
        this.plugin = plugin;
    }

    public abstract void load();

    public abstract void unload();
}