package com.kale_ko.evercraft.spigot.listeners;

import com.kale_ko.evercraft.spigot.SpigotMain;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public abstract class SpigotListener implements Listener {
    public SpigotListener register() {
        SpigotMain.getInstance().getServer().getPluginManager().registerEvents(this, SpigotMain.getInstance());

        return this;
    }

    public void unregister() {
        HandlerList.unregisterAll(this);
    }
}