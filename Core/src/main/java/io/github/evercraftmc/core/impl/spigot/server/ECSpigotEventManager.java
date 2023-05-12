/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package io.github.evercraftmc.core.impl.spigot.server;

import io.github.evercraftmc.core.ECData;
import io.github.evercraftmc.core.api.events.ECEvent;
import io.github.evercraftmc.core.api.events.ECHandler;
import io.github.evercraftmc.core.api.events.ECListener;
import io.github.evercraftmc.core.api.events.player.PlayerLeaveEvent;
import io.github.evercraftmc.core.api.server.ECEventManager;
import io.github.evercraftmc.core.impl.spigot.server.ECSpigotServer;
import io.github.evercraftmc.core.impl.spigot.server.player.ECSpigotPlayer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class ECSpigotEventManager
implements ECEventManager {
    protected ECSpigotServer server;
    protected Map<Class<? extends ECEvent>, List<Map.Entry<ECListener, Method>>> listeners = new HashMap<Class<? extends ECEvent>, List<Map.Entry<ECListener, Method>>>();

    public ECSpigotEventManager(ECSpigotServer server) {
        this.server = server;
        this.server.getHandle().getPluginManager().registerEvents((Listener)new SpigotListeners(), (Plugin)((JavaPlugin)this.server.getPlugin().getHandle()));
    }

    public ECSpigotServer getServer() {
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

    protected class SpigotListeners
    implements Listener {
        protected SpigotListeners() {
        }

        @EventHandler
        public void onPlayerJoin(PlayerJoinEvent event) {
            if (!ECSpigotEventManager.this.server.getPlugin().getData().players.containsKey(event.getPlayer().getUniqueId().toString())) {
                ECSpigotEventManager.this.server.getPlugin().getData().players.put(event.getPlayer().getUniqueId().toString(), new ECData.Player());
            }
            ECSpigotEventManager.this.emit(new io.github.evercraftmc.core.api.events.player.PlayerJoinEvent(new ECSpigotPlayer(ECSpigotEventManager.this.server.getPlugin().getData().players.get(event.getPlayer().getUniqueId().toString()), event.getPlayer())));
        }

        @EventHandler
        public void onPlayerLeave(PlayerQuitEvent event) {
            ECSpigotEventManager.this.emit(new PlayerLeaveEvent(new ECSpigotPlayer(ECSpigotEventManager.this.server.getPlugin().getData().players.get(event.getPlayer().getUniqueId().toString()), event.getPlayer())));
        }
    }
}

