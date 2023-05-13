package io.github.evercraftmc.core.impl.bungee.server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import io.github.evercraftmc.core.ECData;
import io.github.evercraftmc.core.api.events.ECEvent;
import io.github.evercraftmc.core.api.events.ECHandler;
import io.github.evercraftmc.core.api.events.ECListener;
import io.github.evercraftmc.core.api.events.player.PlayerJoinEvent;
import io.github.evercraftmc.core.api.events.player.PlayerLeaveEvent;
import io.github.evercraftmc.core.api.server.ECEventManager;
import io.github.evercraftmc.core.impl.bungee.server.player.ECBungeePlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

@SuppressWarnings("unchecked")
public class ECBungeeEventManager implements ECEventManager {
    protected class BungeeListeners implements Listener {
        protected final ECBungeeEventManager parent = ECBungeeEventManager.this;

        @EventHandler
        public void onPlayerConnect(LoginEvent event) {
            String uuid = event.getLoginResult().getId();
            uuid = uuid.substring(0, 8) + "-" + uuid.substring(8, 12) + "-" + uuid.substring(12, 16) + "-" + uuid.substring(16, 20) + "-" + uuid.substring(20, 32);
            if (!parent.server.getPlugin().getData().players.containsKey(uuid)) {
                parent.server.getPlugin().getData().players.put(uuid, new ECData.Player(UUID.fromString(uuid), event.getLoginResult().getName()));
            }
        }

        @EventHandler
        public void onPlayerJoin(PostLoginEvent event) {
            parent.emit(new PlayerJoinEvent(new ECBungeePlayer(parent.server.getPlugin().getData().players.get(event.getPlayer().getUniqueId().toString()), event.getPlayer())));
        }

        @EventHandler
        public void onPlayerLeave(PlayerDisconnectEvent event) {
            parent.emit(new PlayerLeaveEvent(new ECBungeePlayer(parent.server.getPlugin().getData().players.get(event.getPlayer().getUniqueId().toString()), event.getPlayer())));
        }
    }

    protected ECBungeeServer server;

    protected Map<Class<? extends ECEvent>, List<Map.Entry<ECListener, Method>>> listeners = new HashMap<Class<? extends ECEvent>, List<Map.Entry<ECListener, Method>>>();

    public ECBungeeEventManager(ECBungeeServer server) {
        this.server = server;

        this.server.getHandle().getPluginManager().registerListener((Plugin) this.server.getPlugin().getHandle(), (Listener) new BungeeListeners());
    }

    public ECBungeeServer getServer() {
        return this.server;
    }

    @Override
    public void emit(ECEvent event) {
        if (this.listeners.containsKey((Class<? extends ECEvent>) event.getClass())) {
            for (Map.Entry<ECListener, Method> entry : this.listeners.get((Class<? extends ECEvent>) event.getClass())) {
                try {
                    entry.getValue().setAccessible(true);
                    entry.getValue().invoke(entry.getKey(), event);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    this.server.getPlugin().getLogger().error("Failed to emit event", (Throwable) e);
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