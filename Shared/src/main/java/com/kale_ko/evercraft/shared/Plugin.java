package com.kale_ko.evercraft.shared;

public interface Plugin {
    public void onEnable();

    public void onDisable();

    public default void reload() {
        this.onDisable();

        this.onEnable();
    }
}