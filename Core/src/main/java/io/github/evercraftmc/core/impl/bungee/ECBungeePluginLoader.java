/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.plugin.Plugin
 */
package io.github.evercraftmc.core.impl.bungee;

import io.github.evercraftmc.core.ECPlugin;
import io.github.evercraftmc.core.impl.ECEnvironment;
import io.github.evercraftmc.core.impl.bungee.server.ECBungeeServer;
import net.md_5.bungee.api.plugin.Plugin;

public class ECBungeePluginLoader
extends Plugin {
    private ECPlugin plugin;

    public void onLoad() {
        this.plugin = new ECPlugin((Object)this, this.getFile(), this.getProxy().getPluginsFolder().toPath().resolve("EverCraft").toFile(), ECEnvironment.BUNGEE, this.getSLF4JLogger(), ((Object)((Object)this)).getClass().getClassLoader());
    }

    public void onEnable() {
        this.plugin.setServer(new ECBungeeServer(this.plugin, this.getProxy()));
        this.plugin.load();
    }

    public void onDisable() {
        this.plugin.unload();
    }
}

