/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.github.evercraftmc.core.ECPlugin
 *  io.github.evercraftmc.core.api.ECModule
 *  io.github.evercraftmc.core.api.ECModuleInfo
 */
package io.github.evercraftmc.global;

import io.github.evercraftmc.core.ECPlugin;
import io.github.evercraftmc.core.api.ECModule;
import io.github.evercraftmc.core.api.ECModuleInfo;

public class GlobalModule
implements ECModule {
    protected ECModuleInfo info;
    protected ECPlugin plugin;

    public String getName() {
        return this.getInfo().getName();
    }

    public ECModuleInfo getInfo() {
        return this.info;
    }

    public void setInfo(ECModuleInfo info) {
        this.info = info;
    }

    public ECPlugin getPlugin() {
        return this.plugin;
    }

    public void setPlugin(ECPlugin plugin) {
        this.plugin = plugin;
    }

    public void load() {
    }

    public void unload() {
    }
}

