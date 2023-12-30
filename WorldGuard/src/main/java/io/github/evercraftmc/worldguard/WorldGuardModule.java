package io.github.evercraftmc.worldguard;

import io.github.evercraftmc.core.ECPlugin;
import io.github.evercraftmc.core.api.ECModule;
import io.github.evercraftmc.core.api.ECModuleInfo;
import io.github.evercraftmc.core.api.commands.ECCommand;
import io.github.evercraftmc.core.api.events.ECListener;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class WorldGuardModule implements ECModule {
    protected ECModuleInfo info;

    protected ECPlugin plugin;

    protected @NotNull List<ECCommand> commands = new ArrayList<>();
    protected @NotNull List<ECListener> listeners = new ArrayList<>();

    public @NotNull String getName() {
        return this.getInfo().getName();
    }

    public @NotNull ECModuleInfo getInfo() {
        return this.info;
    }

    public void setInfo(@NotNull ECModuleInfo info) {
        this.info = info;
    }

    public @NotNull ECPlugin getPlugin() {
        return this.plugin;
    }

    public void setPlugin(@NotNull ECPlugin plugin) {
        this.plugin = plugin;
    }

    public void load() {

    }

    public void unload() {
        for (ECCommand command : this.commands) {
            this.plugin.getServer().getCommandManager().unregister(command);
        }

        for (ECListener listener : this.listeners) {
            this.plugin.getServer().getEventManager().unregister(listener);
        }
    }
}