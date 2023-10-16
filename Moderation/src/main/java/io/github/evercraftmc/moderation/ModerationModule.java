package io.github.evercraftmc.moderation;

import io.github.evercraftmc.core.ECPlugin;
import io.github.evercraftmc.core.api.ECModule;
import io.github.evercraftmc.core.api.ECModuleInfo;
import io.github.evercraftmc.core.api.commands.ECCommand;
import io.github.evercraftmc.core.api.events.ECListener;
import io.github.evercraftmc.moderation.commands.*;
import io.github.evercraftmc.moderation.listeners.BanListener;
import io.github.evercraftmc.moderation.listeners.LockChatListener;
import io.github.evercraftmc.moderation.listeners.MuteListener;
import java.util.ArrayList;
import java.util.List;

public class ModerationModule implements ECModule {
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
        this.getPlugin().getServer().getCommandManager().register(new KickCommand(this), false, false);
        this.getPlugin().getServer().getCommandManager().register(new BanCommand(this), false, false);
        this.getPlugin().getServer().getCommandManager().register(new UnbanCommand(this), false, false);
        this.getPlugin().getServer().getCommandManager().register(new MuteCommand(this), false, false);
        this.getPlugin().getServer().getCommandManager().register(new UnmuteCommand(this), false, false);

        this.getPlugin().getServer().getCommandManager().register(new ClearChatCommand(this), false, false);
        this.getPlugin().getServer().getCommandManager().register(new LockChatCommand(this), false, false);

        this.getPlugin().getServer().getEventManager().register(new BanListener(this));
        this.getPlugin().getServer().getEventManager().register(new MuteListener(this));
        this.getPlugin().getServer().getEventManager().register(new LockChatListener(this));
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