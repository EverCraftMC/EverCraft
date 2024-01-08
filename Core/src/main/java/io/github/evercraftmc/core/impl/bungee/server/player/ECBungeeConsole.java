package io.github.evercraftmc.core.impl.bungee.server.player;

import io.github.evercraftmc.core.api.server.player.ECConsole;
import io.github.evercraftmc.core.impl.bungee.util.ECBungeeComponentFormatter;
import net.md_5.bungee.api.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ECBungeeConsole implements ECConsole {
    protected final @NotNull CommandSender handle;

    public ECBungeeConsole(@NotNull CommandSender handle) {
        this.handle = handle;
    }

    public @NotNull CommandSender getHandle() {
        return this.handle;
    }

    @Override
    public boolean hasPermission(@NotNull String permission) {
        return this.handle.hasPermission(permission);
    }

    @Override
    public void sendMessage(@NotNull String message) {
        this.handle.sendMessage(ECBungeeComponentFormatter.stringToComponent(message));
    }
}