package io.github.evercraftmc.core.impl.bungee.server.player;

import io.github.evercraftmc.core.api.server.player.ECConsole;
import io.github.evercraftmc.core.impl.bungee.server.util.ECBungeeComponentFormatter;
import net.md_5.bungee.api.CommandSender;

public class ECBungeeConsole implements ECConsole {
    protected CommandSender handle;

    public ECBungeeConsole(CommandSender handle) {
        this.handle = handle;
    }

    public CommandSender getHandle() {
        return this.handle;
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.handle.hasPermission(permission);
    }

    @Override
    public void sendMessage(String message) {
        this.handle.sendMessage(ECBungeeComponentFormatter.stringToComponent(message));
    }
}