package io.github.evercraftmc.core.api;

import io.github.evercraftmc.core.ECPlugin;

public interface ECModule {
    public String getName();

    public ECModuleInfo getInfo();

    public void setInfo(ECModuleInfo info);

    public ECPlugin getPlugin();

    public void setPlugin(ECPlugin plugin);

    public void load();

    public void unload();
}