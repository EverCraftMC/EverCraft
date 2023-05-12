/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.event.PlayerDisconnectEvent
 *  net.md_5.bungee.api.event.PostLoginEvent
 *  net.md_5.bungee.api.plugin.Listener
 *  net.md_5.bungee.api.plugin.Plugin
 *  net.md_5.bungee.event.EventHandler
 */
package io.github.evercraftmc.core.impl.bungee.server;

import io.github.evercraftmc.core.ECData;
import io.github.evercraftmc.core.api.events.ECEvent;
import io.github.evercraftmc.core.api.events.ECHandler;
import io.github.evercraftmc.core.api.events.ECListener;
import io.github.evercraftmc.core.api.events.player.PlayerJoinEvent;
import io.github.evercraftmc.core.api.events.player.PlayerLeaveEvent;
import io.github.evercraftmc.core.api.server.ECEventManager;
import io.github.evercraftmc.core.impl.bungee.server.ECBungeeServer;
import io.github.evercraftmc.core.impl.bungee.server.player.ECBungeePlayer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class ECBungeeEventManager
implements ECEventManager {
    protected ECBungeeServer server;
    protected Map<Class<? extends ECEvent>, List<Map.Entry<ECListener, Method>>> listeners = new HashMap<Class<? extends ECEvent>, List<Map.Entry<ECListener, Method>>>();

    public ECBungeeEventManager(ECBungeeServer server) {
        this.server = server;
        this.server.getHandle().getPluginManager().registerListener((Plugin)this.server.getPlugin().getHandle(), (Listener)new BungeeListeners());
    }

    public ECBungeeServer getServer() {
        return this.server;
    }

    @Override
    public void emit(ECEvent event) {
        if (this.listeners.containsKey(event.getClass())) {
            for (Map.Entry<ECListener, Method> entry : this.listeners.get(event.getClass())) {
                try {
                    entry.getValue().setAccessible(true);
                    entry.getValue().invoke(entry.getKey(), event);
                }
                catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    this.server.getPlugin().getLogger().error("Failed to emit event", (Throwable)e);
                }
            }
        }
    }

    @Override
    public ECListener register(ECListener listener) {
        for (Method method : listener.getClass().getDeclaredMethods()) {
            if (method.getParameterCount() != 1 || !ECEvent.class.isAssignableFrom(method.getParameterTypes()[0]) || method.getDeclaredAnnotationsByType(ECHandler.class) == null) continue;
            if (!this.listeners.containsKey(method.getParameterTypes()[0])) {
                this.listeners.put(method.getParameterTypes()[0], new ArrayList());
            }
            this.listeners.get(method.getParameterTypes()[0]).add(new AbstractMap.SimpleEntry<ECListener, Method>(listener, method));
        }
        return listener;
    }

    @Override
    public ECListener unregister(ECListener listener) {
        for (Method method : listener.getClass().getDeclaredMethods()) {
            if (method.getParameterCount() != 1 || !ECEvent.class.isAssignableFrom(method.getParameterTypes()[0]) || method.getDeclaredAnnotationsByType(ECHandler.class) == null || !this.listeners.containsKey(method.getParameterTypes()[0])) continue;
            this.listeners.get(method.getParameterTypes()[0]).remove(new AbstractMap.SimpleEntry<ECListener, Method>(listener, method));
        }
        return listener;
    }

    @Override
    public void unregisterAll() {
        this.listeners.clear();
    }

    protected class BungeeListeners
    implements Listener {
        protected BungeeListeners() {
        }

        @EventHandler
        public void onPlayerJoin(PostLoginEvent event) {
            if (!ECBungeeEventManager.this.server.getPlugin().getData().players.containsKey(event.getPlayer().getUniqueId().toString())) {
                ECBungeeEventManager.this.server.getPlugin().getData().players.put(event.getPlayer().getUniqueId().toString(), new ECData.Player());
            }
            ECBungeeEventManager.this.emit(new PlayerJoinEvent(new ECBungeePlayer(ECBungeeEventManager.this.server.getPlugin().getData().players.get(event.getPlayer().getUniqueId().toString()), event.getPlayer())));
        }

        @EventHandler
        public void onPlayerLeave(PlayerDisconnectEvent event) {
            ECBungeeEventManager.this.emit(new PlayerLeaveEvent(new ECBungeePlayer(ECBungeeEventManager.this.server.getPlugin().getData().players.get(event.getPlayer().getUniqueId().toString()), event.getPlayer())));
        }
    }
}

