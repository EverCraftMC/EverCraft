package io.github.evercraftmc.core.api;

import io.github.evercraftmc.core.ECPlugin;

public interface ECModule {
    String getName();

    ECModuleInfo getInfo();

    void setInfo(ECModuleInfo info);

    ECPlugin getPlugin();

    void setPlugin(ECPlugin plugin);

    void load();

    void unload();
}