package io.github.evercraftmc.evercraft.shared;

import java.util.logging.Logger;

public interface Plugin {
    public void onEnable();

    public void onDisable();

    public default void reload() {
        this.onDisable();

        this.onEnable();
    }

    public Logger getLogger();
}