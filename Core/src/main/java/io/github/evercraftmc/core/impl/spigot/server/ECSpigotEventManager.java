package io.github.evercraftmc.core.impl.spigot.server;

import io.github.evercraftmc.core.ECPlayerData;
import io.github.evercraftmc.core.api.events.ECEvent;
import io.github.evercraftmc.core.api.events.ECHandler;
import io.github.evercraftmc.core.api.events.ECListener;
import io.github.evercraftmc.core.api.events.player.PlayerChatEvent;
import io.github.evercraftmc.core.api.events.player.PlayerCommandEvent;
import io.github.evercraftmc.core.api.server.ECEventManager;
import io.github.evercraftmc.core.api.server.player.ECPlayer;
import io.github.evercraftmc.core.impl.spigot.server.player.ECSpigotPlayer;
import io.github.evercraftmc.core.impl.spigot.util.ECSpigotComponentFormatter;
import io.github.evercraftmc.core.impl.util.ECTextFormatter;
import io.papermc.paper.event.player.AsyncChatEvent;
import java.lang.reflect.Method;
import java.util.*;
import net.kyori.adventure.text.Component;
import org.bukkit.GameRule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class ECSpigotEventManager implements ECEventManager {
    protected class SpigotListeners implements Listener {
        protected final @NotNull ECSpigotEventManager parent = ECSpigotEventManager.this;

        @EventHandler
        public void onPlayerJoin(@NotNull PlayerLoginEvent event) {
            if (!parent.server.getPlugin().getPlayerData().players.containsKey(event.getPlayer().getUniqueId().toString())) {
                parent.server.getPlugin().getPlayerData().players.put(event.getPlayer().getUniqueId().toString(), new ECPlayerData.Player(event.getPlayer().getUniqueId(), event.getPlayer().getName()));
            }

            io.github.evercraftmc.core.api.events.player.PlayerLoginEvent newEvent = new io.github.evercraftmc.core.api.events.player.PlayerLoginEvent(new ECSpigotPlayer(parent.server.getPlugin().getPlayerData().players.get(event.getPlayer().getUniqueId().toString())));
            parent.emit(newEvent);

            if (newEvent.isCancelled()) {
                event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
                event.getPlayer().kick(ECSpigotComponentFormatter.stringToComponent(newEvent.getCancelReason()));
            }
        }

        @EventHandler
        public void onPlayerJoin(@NotNull PlayerJoinEvent event) {
            Component ogMessage = event.joinMessage();
            io.github.evercraftmc.core.api.events.player.PlayerJoinEvent newEvent = new io.github.evercraftmc.core.api.events.player.PlayerJoinEvent(new ECSpigotPlayer(parent.server.getPlugin().getPlayerData().players.get(event.getPlayer().getUniqueId().toString()), event.getPlayer()), ogMessage != null ? ECSpigotComponentFormatter.componentToString(ogMessage) : "");
            parent.emit(newEvent);

            event.joinMessage(Component.empty());

            if (newEvent.isCancelled()) {
                event.getPlayer().kick(ECSpigotComponentFormatter.stringToComponent(newEvent.getCancelReason()));
            } else if (!newEvent.getJoinMessage().isEmpty()) {
                parent.server.broadcastMessage(newEvent.getJoinMessage());
            }
        }

        @EventHandler
        public void onPlayerLeave(@NotNull PlayerQuitEvent event) {
            Component ogMessage = event.quitMessage();
            io.github.evercraftmc.core.api.events.player.PlayerLeaveEvent newEvent = new io.github.evercraftmc.core.api.events.player.PlayerLeaveEvent(new ECSpigotPlayer(parent.server.getPlugin().getPlayerData().players.get(event.getPlayer().getUniqueId().toString()), event.getPlayer()), ogMessage != null ? ECSpigotComponentFormatter.componentToString(ogMessage) : "");
            parent.emit(newEvent);

            event.quitMessage(Component.empty());

            if (!newEvent.getLeaveMessage().isEmpty()) {
                parent.server.broadcastMessage(newEvent.getLeaveMessage());
            }
        }

        @EventHandler
        public void onPlayerChat(@NotNull AsyncChatEvent event) {
            String message = ECSpigotComponentFormatter.componentToString(event.message());
            if (message.isEmpty()) {
                event.setCancelled(true);
                return;
            }

            ECSpigotPlayer player = parent.server.getOnlinePlayer(event.getPlayer().getUniqueId());

            PlayerChatEvent newEvent = new PlayerChatEvent(new ECSpigotPlayer(parent.server.getPlugin().getPlayerData().players.get(player.getUuid().toString()), player.getHandle()), message, PlayerChatEvent.MessageType.CHAT, new ArrayList<>(parent.getServer().getOnlinePlayers()));
            parent.emit(newEvent);

            event.message(Component.empty());
            event.setCancelled(true);

            if (newEvent.isCancelled()) {
                if (!newEvent.getCancelReason().isEmpty()) {
                    player.sendMessage(newEvent.getCancelReason());
                }
            } else if (!newEvent.getMessage().isEmpty()) {
                for (ECPlayer player2 : (!newEvent.getRecipients().isEmpty() ? newEvent.getRecipients() : parent.getServer().getOnlinePlayers())) {
                    player2.sendMessage(ECTextFormatter.translateColors("&r" + newEvent.getMessage()));
                }
            }
        }

        @EventHandler
        public void onPlayerCommand(@NotNull PlayerCommandPreprocessEvent event) {
            String message = event.getMessage();
            if (message.isEmpty() || message.equalsIgnoreCase("/")) {
                event.setCancelled(true);
                return;
            }

            ECSpigotPlayer player = parent.server.getOnlinePlayer(event.getPlayer().getUniqueId());

            PlayerCommandEvent newEvent = new PlayerCommandEvent(new ECSpigotPlayer(parent.server.getPlugin().getPlayerData().players.get(player.getUuid().toString()), player.getHandle()), message);
            parent.emit(newEvent);

            if (newEvent.isCancelled()) {
                event.setCancelled(true);

                if (!newEvent.getCancelReason().isEmpty()) {
                    player.sendMessage(newEvent.getCancelReason());
                }
            }
        }

        @EventHandler
        public void onPlayerChat(@NotNull PlayerDeathEvent event) {
            Component component = event.deathMessage();
            if (component == null) {
                return;
            }
            String message = ECSpigotComponentFormatter.componentToString(component);

            ECSpigotPlayer player = parent.server.getOnlinePlayer(event.getPlayer().getUniqueId());

            PlayerChatEvent newEvent = new PlayerChatEvent(new ECSpigotPlayer(parent.server.getPlugin().getPlayerData().players.get(player.getUuid().toString()), player.getHandle()), message, PlayerChatEvent.MessageType.DEATH, new ArrayList<>(parent.getServer().getOnlinePlayers()));
            parent.emit(newEvent);

            event.deathMessage(Component.empty());

            if (newEvent.isCancelled()) {
                if (!newEvent.getCancelReason().isEmpty()) {
                    player.sendMessage(newEvent.getCancelReason());
                }
            } else if (!newEvent.getMessage().isEmpty()) {
                for (ECPlayer player2 : (!newEvent.getRecipients().isEmpty() ? newEvent.getRecipients() : parent.getServer().getOnlinePlayers())) {
                    player2.sendMessage(ECTextFormatter.translateColors("&r" + newEvent.getMessage()));
                }
            }
        }

        @EventHandler
        public void onPlayerChat(@NotNull PlayerAdvancementDoneEvent event) {
            Component component = event.message();
            if (component == null || event.getAdvancement().getDisplay() == null || !event.getAdvancement().getDisplay().doesAnnounceToChat() || Boolean.FALSE.equals(event.getPlayer().getWorld().getGameRuleValue(GameRule.ANNOUNCE_ADVANCEMENTS))) {
                return;
            }
            String message = ECSpigotComponentFormatter.componentToString(component);

            ECSpigotPlayer player = parent.server.getOnlinePlayer(event.getPlayer().getUniqueId());

            PlayerChatEvent newEvent = new PlayerChatEvent(new ECSpigotPlayer(parent.server.getPlugin().getPlayerData().players.get(player.getUuid().toString()), player.getHandle()), message, PlayerChatEvent.MessageType.ADVANCEMENT, new ArrayList<>(parent.getServer().getOnlinePlayers()));
            parent.emit(newEvent);

            event.message(Component.empty());

            if (newEvent.isCancelled()) {
                if (!newEvent.getCancelReason().isEmpty()) {
                    player.sendMessage(newEvent.getCancelReason());
                }
            } else if (!newEvent.getMessage().isEmpty()) {
                for (ECPlayer player2 : (!newEvent.getRecipients().isEmpty() ? newEvent.getRecipients() : parent.getServer().getOnlinePlayers())) {
                    player2.sendMessage(ECTextFormatter.translateColors("&r" + newEvent.getMessage()));
                }
            }
        }
    }

    protected final @NotNull ECSpigotServer server;

    protected final @NotNull Map<Class<? extends ECEvent>, List<Map.Entry<ECListener, Method>>> listeners = new HashMap<>();

    public ECSpigotEventManager(@NotNull ECSpigotServer server) {
        this.server = server;

        this.server.getHandle().getPluginManager().registerEvents(new SpigotListeners(), (Plugin) this.server.getPlugin().getHandle());
    }

    public @NotNull ECSpigotServer getServer() {
        return this.server;
    }

    @Override
    public void emit(@NotNull ECEvent event) {
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

    @SuppressWarnings("unchecked")
    @Override
    public @NotNull ECListener register(@NotNull ECListener listener) {
        if (listener instanceof org.bukkit.event.Listener spigotListener) {
            this.server.getHandle().getPluginManager().registerEvents(spigotListener, (Plugin) this.server.getPlugin().getHandle());
        }

        for (Method method : listener.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(ECHandler.class) && method.getParameterCount() == 1 && ECEvent.class.isAssignableFrom(method.getParameterTypes()[0])) {
                if (!this.listeners.containsKey((Class<? extends ECEvent>) method.getParameterTypes()[0])) {
                    this.listeners.put((Class<? extends ECEvent>) method.getParameterTypes()[0], new ArrayList<>());
                }
                this.listeners.get((Class<? extends ECEvent>) method.getParameterTypes()[0]).add(new AbstractMap.SimpleEntry<>(listener, method));

                this.listeners.get((Class<? extends ECEvent>) method.getParameterTypes()[0]).sort(Comparator.comparingInt(a -> a.getValue().getDeclaredAnnotationsByType(ECHandler.class)[0].order().getValue()));
            }
        }

        return listener;
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NotNull ECListener unregister(@NotNull ECListener listener) {
        for (Method method : listener.getClass().getDeclaredMethods()) {
            if (method.getParameterCount() == 1 && ECEvent.class.isAssignableFrom(method.getParameterTypes()[0]) && method.getDeclaredAnnotationsByType(ECHandler.class).length > 0 && this.listeners.containsKey((Class<? extends ECEvent>) method.getParameterTypes()[0])) {
                this.listeners.get((Class<? extends ECEvent>) method.getParameterTypes()[0]).remove(new AbstractMap.SimpleEntry<>(listener, method));
            }
        }

        return listener;
    }

    @Override
    public void unregisterAll() {
        this.listeners.clear();
    }
}