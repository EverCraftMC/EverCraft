package io.github.evercraftmc.global;

import java.time.Instant;
import io.github.evercraftmc.core.ECPlugin;
import io.github.evercraftmc.core.api.ECModule;
import io.github.evercraftmc.core.api.ECModuleInfo;
import io.github.evercraftmc.core.api.events.ECHandler;
import io.github.evercraftmc.core.api.events.ECListener;
import io.github.evercraftmc.core.api.events.player.PlayerJoinEvent;
import io.github.evercraftmc.core.api.events.player.PlayerLeaveEvent;

public class GlobalModule implements ECModule {
    protected ECModuleInfo info;

    protected ECPlugin plugin;

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
        this.plugin.getServer().getEventManager().register(new ECListener() {
            protected final GlobalModule parent = GlobalModule.this;

            @ECHandler
            public void onPlayerJoin(PlayerJoinEvent event) {
                if (parent.getPlugin().getData().players.get(event.getPlayer().getUuid().toString()).firstJoin == null) {
                    parent.getPlugin().getData().players.get(event.getPlayer().getUuid().toString()).firstJoin = Instant.now();

                    // TODO Welcome message
                }

                parent.getPlugin().getData().players.get(event.getPlayer().getUuid().toString()).lastJoin = Instant.now();
            }

            @ECHandler
            public void onPlayerLeave(PlayerLeaveEvent event) {
                parent.getPlugin().getData().players.get(event.getPlayer().getUuid().toString()).lastJoin = Instant.now();
            }
        });
    }

    public void unload() {
        // TODO
    }
}