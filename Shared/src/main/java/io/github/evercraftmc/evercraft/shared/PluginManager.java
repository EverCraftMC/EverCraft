package io.github.evercraftmc.evercraft.shared;

import java.util.List;

public class PluginManager {
    public static List<Plugin> registeredPlugins;

    public static Plugin getInstance() {
        return PluginManager.registeredPlugins.get(0);
    }

    public static List<Plugin> getInstances() {
        return PluginManager.registeredPlugins;
    }

    public static void register(Plugin plugin) {
        if (!PluginManager.registeredPlugins.contains(plugin)) {
            PluginManager.registeredPlugins.add(plugin);
        }
    }

    public static void unregister(Plugin plugin) {
        if (PluginManager.registeredPlugins.contains(plugin)) {
            PluginManager.registeredPlugins.remove(plugin);
        }
    }
}