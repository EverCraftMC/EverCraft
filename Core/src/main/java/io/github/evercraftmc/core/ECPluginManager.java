package io.github.evercraftmc.core;

import io.github.evercraftmc.core.api.ECModule;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ECPluginManager {
    protected static ECPlugin plugin;

    protected static Map<Class<? extends ECModule>, ECModule> modules = new HashMap<>();

    private ECPluginManager() {
    }

    public static ECPlugin getPlugin() {
        return ECPluginManager.plugin;
    }

    public static void registerPlugin(ECPlugin plugin) {
        ECPluginManager.plugin = plugin;
    }

    public static void unregisterPlugin() {
        ECPluginManager.plugin = null;
    }

    public static Collection<ECModule> getModules() {
        return Collections.unmodifiableCollection(ECPluginManager.modules.values());
    }

    public static ECModule getModule(Class<? extends ECModule> clazz) {
        if (ECPluginManager.modules.containsKey(clazz)) {
            return ECPluginManager.modules.get(clazz);
        } else {
            throw new RuntimeException("Module \"" + clazz.getSimpleName() + "\" is not registered");
        }
    }

    public static void registerModule(ECModule module) {
        if (!ECPluginManager.modules.containsKey(module.getClass())) {
            ECPluginManager.modules.put(module.getClass(), module);
        } else {
            throw new RuntimeException("Module \"" + module.getClass().getSimpleName() + "\" is not registered");
        }
    }

    public static void unregisterModule(ECModule module) {
        if (ECPluginManager.modules.containsKey(module.getClass())) {
            ECPluginManager.modules.remove(module.getClass());
        } else {
            throw new RuntimeException("Module \"" + module.getClass().getSimpleName() + "\" is not registered");
        }
    }
}