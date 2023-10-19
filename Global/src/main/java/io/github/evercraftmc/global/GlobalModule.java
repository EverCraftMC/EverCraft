package io.github.evercraftmc.global;

import io.github.evercraftmc.core.ECPlugin;
import io.github.evercraftmc.core.api.ECModule;
import io.github.evercraftmc.core.api.ECModuleInfo;
import io.github.evercraftmc.core.api.commands.ECCommand;
import io.github.evercraftmc.core.api.events.ECListener;
import io.github.evercraftmc.global.commands.NickCommand;
import io.github.evercraftmc.global.commands.PrefixCommand;
import io.github.evercraftmc.global.listeners.ChatListener;
import io.github.evercraftmc.global.listeners.JoinListener;
import java.util.ArrayList;
import java.util.List;

public class GlobalModule implements ECModule {
    protected ECModuleInfo info;

    protected ECPlugin plugin;

    protected List<ECCommand> commands = new ArrayList<>();
    protected List<ECListener> listeners = new ArrayList<>();

    public String getName() {
        return this.getInfo().getName();
    }

    public ECModuleInfo getInfo() {
        return this.info;
    }

    public void setInfo(ECModuleInfo info) {
        this.info = info;
    }

    public ECPlugin getPlugin() {
        return this.plugin;
    }

    public void setPlugin(ECPlugin plugin) {
        this.plugin = plugin;
    }

    public void load() {
        this.commands.add(this.plugin.getServer().getCommandManager().register(new NickCommand(this), false, true));
        this.commands.add(this.plugin.getServer().getCommandManager().register(new PrefixCommand(this), false, true));

        this.listeners.add(this.plugin.getServer().getEventManager().register(new JoinListener(this)));
        this.listeners.add(this.plugin.getServer().getEventManager().register(new ChatListener(this)));
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