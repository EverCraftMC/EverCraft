package com.kale_ko.evercraft.bungee.listeners;

import com.kale_ko.evercraft.bungee.BungeeMain;
import com.kale_ko.evercraft.shared.PluginListener;
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