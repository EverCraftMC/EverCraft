package io.github.evercraftmc.core.impl.spigot.server.player;

import org.bukkit.command.CommandSender;
import io.github.evercraftmc.core.api.server.player.ECConsole;
import io.github.evercraftmc.core.impl.spigot.server.util.ECSpigotComponentFormatter;

public class ECSpigotConsole implements ECConsole {
    protected CommandSender handle;

    public ECSpigotConsole(CommandSender handle) {
        this.handle = handle;
    }

    public CommandSender getHandle() {
        return this.handle;
    }

    @Override
    public void sendMessage(String message) {
        this.handle.sendMessage(ECSpigotComponentFormatter.stringToComponent(message));
    }
}