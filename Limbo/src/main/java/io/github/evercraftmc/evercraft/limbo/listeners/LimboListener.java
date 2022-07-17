package io.github.evercraftmc.evercraft.limbo.listeners;

import com.loohp.limbo.events.Listener;
import io.github.evercraftmc.evercraft.limbo.LimboMain;
import io.github.evercraftmc.evercraft.shared.PluginListener;

public abstract class LimboListener implements PluginListener, Listener {
    public LimboListener register() {
        LimboMain.getInstance().getServer().getEventsManager().registerEvents(LimboMain.getInstance(), this);

        return this;
    }

    public void unregister() {}
}