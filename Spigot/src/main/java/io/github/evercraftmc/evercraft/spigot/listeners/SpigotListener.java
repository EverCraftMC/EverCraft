package io.github.evercraftmc.evercraft.spigot.listeners;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import io.github.evercraftmc.evercraft.shared.PluginListener;
import io.github.evercraftmc.evercraft.spigot.SpigotMain;

public abstract class SpigotListener implements PluginListener, Listener {
    public SpigotListener register() {
        SpigotMain.getInstance().getServer().getPluginManager().registerEvents(this, SpigotMain.getInstance());

        return this;
    }

    public void unregister() {
        HandlerList.unregisterAll(this);
    }
}