package io.github.evercraftmc.global;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import io.github.evercraftmc.core.ECPlugin;
import io.github.evercraftmc.core.api.ECModule;
import io.github.evercraftmc.core.api.ECModuleInfo;
import io.github.evercraftmc.core.api.commands.ECCommand;
import io.github.evercraftmc.core.api.events.ECHandler;
import io.github.evercraftmc.core.api.events.ECListener;
import io.github.evercraftmc.core.api.events.player.PlayerJoinEvent;
import io.github.evercraftmc.core.api.events.player.PlayerLeaveEvent;
import io.github.evercraftmc.core.impl.ECEnvironmentType;
import io.github.evercraftmc.global.commands.NickCommand;
import io.github.evercraftmc.global.commands.PrefixCommand;

public class GlobalModule implements ECModule {
    protected ECModuleInfo info;

    protected ECPlugin plugin;

    protected List<ECCommand> commands = new ArrayList<ECCommand>();
    protected List<ECListener> listeners = new ArrayList<ECListener>();

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
        this.commands.add(this.plugin.getServer().getCommandManager().register(new NickCommand(this), false));
        this.commands.add(this.plugin.getServer().getCommandManager().register(new PrefixCommand(this), false));

        if (this.plugin.getEnvironment().getType() == ECEnvironmentType.PROXY) {
            this.listeners.add(this.plugin.getServer().getEventManager().register(new ECListener() {
                protected final GlobalModule parent = GlobalModule.this;

                @ECHandler
                public void onPlayerJoin(PlayerJoinEvent event) {
                    if (parent.getPlugin().getPlayerData().players.get(event.getPlayer().getUuid().toString()).firstJoin == null) {
                        parent.getPlugin().getPlayerData().players.get(event.getPlayer().getUuid().toString()).firstJoin = Instant.now();

                        // TODO Welcome message
                    }

                    parent.getPlugin().getPlayerData().players.get(event.getPlayer().getUuid().toString()).lastJoin = Instant.now();

                    parent.getPlugin().getPlayerData().players.get(event.getPlayer().getUuid().toString()).lastIp = event.getPlayer().getAddress();

                    parent.getPlugin().saveData();
                }

                @ECHandler
                public void onPlayerLeave(PlayerLeaveEvent event) {
                    parent.getPlugin().getPlayerData().players.get(event.getPlayer().getUuid().toString()).lastJoin = Instant.now();
                }
            }));
        }
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