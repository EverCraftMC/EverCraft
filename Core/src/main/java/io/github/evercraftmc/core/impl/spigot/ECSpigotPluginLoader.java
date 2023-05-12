package io.github.evercraftmc.core.impl.spigot;

import org.bukkit.plugin.java.JavaPlugin;
import io.github.evercraftmc.core.ECPlugin;
import io.github.evercraftmc.core.impl.ECEnvironment;
import io.github.evercraftmc.core.impl.spigot.server.ECSpigotServer;

public class ECSpigotPluginLoader extends JavaPlugin {
    private ECPlugin plugin;

    public void onLoad() {
        this.plugin = new ECPlugin((Object) this, this.getFile(), this.getServer().getPluginsFolder().toPath().resolve("EverCraft").toFile(), ECEnvironment.SPIGOT, this.getSLF4JLogger(), this.getClassLoader());
    }

    public void onEnable() {
        this.plugin.setServer(new ECSpigotServer(this.plugin, this.getServer()));

        this.plugin.load();
    }

    public void onDisable() {
        this.plugin.unload();
    }
}