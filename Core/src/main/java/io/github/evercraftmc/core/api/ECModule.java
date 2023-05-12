/*
 * Decompiled with CFR 0.152.
 */
package io.github.evercraftmc.core.api;

import io.github.evercraftmc.core.ECPlugin;
import io.github.evercraftmc.core.api.ECModuleInfo;

public interface ECModule {
    public String getName();

    public ECModuleInfo getInfo();

    public void setInfo(ECModuleInfo var1);

    public ECPlugin getPlugin();

    public void setPlugin(ECPlugin var1);

    public void load();

    public void unload();
}

