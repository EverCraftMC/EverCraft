package com.kale_ko.evercraft.bungee.listeners;

import com.kale_ko.evercraft.bungee.BungeeMain;
import net.md_5.bungee.api.plugin.Listener;

public abstract class BungeeListener implements Listener {
    public void register() {
        BungeeMain.getInstance().getProxy().getPluginManager().registerListener(BungeeMain.getInstance(), this);
    }
}