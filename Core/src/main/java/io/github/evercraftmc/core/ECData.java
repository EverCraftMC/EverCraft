/*
 * Decompiled with CFR 0.152.
 */
package io.github.evercraftmc.core;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ECData {
    public Map<String, Player> players = new HashMap<String, Player>();

    public static class Player {
        public UUID uuid;
        public String name;
        public String displayName;
    }
}

