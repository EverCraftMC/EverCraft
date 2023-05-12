/*
 * Decompiled with CFR 0.152.
 */
package io.github.evercraftmc.core;

import io.github.evercraftmc.core.ECPlugin;
import io.github.evercraftmc.core.api.ECModule;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ECPluginManager {
    protected static ECPlugin plugin;
    protected static Map<Class<? extends ECModule>, ECModule> modules;

    public static ECPlugin getPlugin() {
        return plugin;
    }

    public static void registerPlugin(ECPlugin plugin) {
        ECPluginManager.plugin = plugin;
    }

    public static void unregisterPlugin() {
        plugin = null;
    }

    public static Collection<ECModule> getModules() {
        return Collections.unmodifiableCollection(modules.values());
    }

    public static ECModule getModule(Class<? extends ECModule> clazz) {
        if (modules.containsKey(clazz)) {
            return modules.get(clazz);
        }
        throw new RuntimeException("Module \"" + clazz.getSimpleName() + "\" is not registered");
    }

    public static void registerModule(ECModule Module2) {
        if (modules.containsKey(Module2.getClass())) {
            throw new RuntimeException("Module \"" + Module2.getClass().getSimpleName() + "\" is not registered");
        }
        modules.put(Module2.getClass(), Module2);
    }

    public static void unregisterModule(ECModule Module2) {
        if (!modules.containsKey(Module2.getClass())) {
            throw new RuntimeException("Module \"" + Module2.getClass().getSimpleName() + "\" is not registered");
        }
        modules.remove(Module2.getClass());
    }

    static {
        modules = new HashMap<Class<? extends ECModule>, ECModule>();
    }
}

