package io.github.evercraftmc.core.impl.spigot.server;

import io.github.evercraftmc.core.ECPlayerData;
import io.github.evercraftmc.core.api.events.ECEvent;
import io.github.evercraftmc.core.api.events.ECHandler;
import io.github.evercraftmc.core.api.events.ECListener;
import io.github.evercraftmc.core.api.events.player.PlayerLeaveEvent;
import io.github.evercraftmc.core.api.server.ECEventManager;
import io.github.evercraftmc.core.impl.spigot.server.player.ECSpigotPlayer;
import java.lang.reflect.Method;
import java.util.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

@SuppressWarnings("unchecked")
public class ECSpigotEventManager implements ECEventManager {
    protected class SpigotListeners implements Listener {
        protected final ECSpigotEventManager parent = ECSpigotEventManager.this;

        @EventHandler
        public void onPlayerJoin(PlayerLoginEvent event) {
            if (!parent.server.getPlugin().getPlayerData().players.containsKey(event.getPlayer().getUniqueId().toString())) {
                parent.server.getPlugin().getPlayerData().players.put(event.getPlayer().getUniqueId().toString(), new ECPlayerData.Player(event.getPlayer().getUniqueId(), event.getPlayer().getName()));
            }
        }

        @EventHandler
        public void onPlayerJoin(PlayerJoinEvent event) {
            parent.emit(new io.github.evercraftmc.core.api.events.player.PlayerJoinEvent(new ECSpigotPlayer(parent.server.getPlugin().getPlayerData().players.get(event.getPlayer().getUniqueId().toString()), event.getPlayer())));
        }

        @EventHandler
        public void onPlayerLeave(PlayerQuitEvent event) {
            parent.emit(new PlayerLeaveEvent(new ECSpigotPlayer(parent.server.getPlugin().getPlayerData().players.get(event.getPlayer().getUniqueId().toString()), event.getPlayer())));
        }
    }

    protected ECSpigotServer server;

    protected Map<Class<? extends ECEvent>, List<Map.Entry<ECListener, Method>>> listeners = new HashMap<Class<? extends ECEvent>, List<Map.Entry<ECListener, Method>>>();

    public ECSpigotEventManager(ECSpigotServer server) {
        this.server = server;

        this.server.getHandle().getPluginManager().registerEvents(new SpigotListeners(), (Plugin) this.server.getPlugin().getHandle());
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
                } catch (Exception e) {
                    this.server.getPlugin().getLogger().error("Failed to emit event", e);
                }
            }
        }
    }

    @Override
    public ECListener register(ECListener listener) {
        for (Method method : listener.getClass().getDeclaredMethods()) {
            if (method.getParameterCount() == 1 && ECEvent.class.isAssignableFrom(method.getParameterTypes()[0]) && method.getDeclaredAnnotationsByType(ECHandler.class) != null) {
                if (!this.listeners.containsKey((Class<? extends ECEvent>) method.getParameterTypes()[0])) {
                    this.listeners.put((Class<? extends ECEvent>) method.getParameterTypes()[0], new ArrayList<Map.Entry<ECListener, Method>>());
                }
                this.listeners.get((Class<? extends ECEvent>) method.getParameterTypes()[0]).add(new AbstractMap.SimpleEntry<ECListener, Method>(listener, method));
            }
        }

        return listener;
    }

    @Override
    public ECListener unregister(ECListener listener) {
        for (Method method : listener.getClass().getDeclaredMethods()) {
            if (method.getParameterCount() == 1 && ECEvent.class.isAssignableFrom(method.getParameterTypes()[0]) && method.getDeclaredAnnotationsByType(ECHandler.class) != null && this.listeners.containsKey((Class<? extends ECEvent>) method.getParameterTypes()[0])) {
                this.listeners.get((Class<? extends ECEvent>) method.getParameterTypes()[0]).remove(new AbstractMap.SimpleEntry<ECListener, Method>(listener, method));
            }
        }

        return listener;
    }

    @Override
    public void unregisterAll() {
        this.listeners.clear();
    }
}