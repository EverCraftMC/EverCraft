package io.github.evercraftmc.evercraft.bungee.listeners;

import io.github.evercraftmc.evercraft.bungee.BungeeMain;
import io.github.evercraftmc.evercraft.shared.PluginListener;
import net.md_5.bungee.api.plugin.Listener;

public abstract class BungeeListener implements PluginListener, Listener {
    public BungeeListener register() {
        BungeeMain.getInstance().getProxy().getPluginManager().registerListener(BungeeMain.getInstance(), this);

        return this;
    }

    public void unregister() {
        BungeeMain.getInstance().getProxy().getPluginManager().unregisterListener(this);
    }
}