package io.github.evercraftmc.evercraft.discord.listeners;

import io.github.evercraftmc.evercraft.shared.PluginListener;

public abstract class DiscordListener implements PluginListener {
    public DiscordListener register() {
        return this;
    }

    public void unregister() {}
}