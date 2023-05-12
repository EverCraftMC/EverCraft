/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.plugin.java.JavaPlugin
 */
package io.github.evercraftmc.core.impl.spigot;

import io.github.evercraftmc.core.ECPlugin;
import io.github.evercraftmc.core.impl.ECEnvironment;
import io.github.evercraftmc.core.impl.spigot.server.ECSpigotServer;
import org.bukkit.plugin.java.JavaPlugin;

public class ECSpigotPluginLoader
extends JavaPlugin {
    private ECPlugin plugin;

    public void onLoad() {
        this.plugin = new ECPlugin((Object)this, this.getFile(), this.getServer().getPluginsFolder().toPath().resolve("EverCraft").toFile(), ECEnvironment.SPIGOT, this.getSLF4JLogger(), this.getClassLoader());
    }

    public void onEnable() {
        this.plugin.setServer(new ECSpigotServer(this.plugin, this.getServer()));
        this.plugin.load();
    }

    public void onDisable() {
        this.plugin.unload();
    }
}

