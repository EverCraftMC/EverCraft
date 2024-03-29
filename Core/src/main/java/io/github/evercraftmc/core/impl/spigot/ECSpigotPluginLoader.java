package io.github.evercraftmc.core.impl.spigot;

import io.github.evercraftmc.core.ECPlugin;
import io.github.evercraftmc.core.impl.ECEnvironment;
import io.github.evercraftmc.core.impl.spigot.server.ECSpigotServer;
import org.bukkit.plugin.java.JavaPlugin;

public class ECSpigotPluginLoader extends JavaPlugin {
    private ECPlugin plugin;

    @Override
    public void onLoad() {
        this.plugin = new ECPlugin(this, this.getFile(), this.getServer().getPluginsFolder().toPath().resolve("EverCraft").toFile(), ECEnvironment.SPIGOT, this.getSLF4JLogger(), this.getClassLoader());
    }

    @Override
    public void onEnable() {
        this.plugin.setServer(new ECSpigotServer(this.plugin, this.getServer()));

        this.plugin.load();
    }

    @Override
    public void onDisable() {
        this.plugin.unload();
    }
}