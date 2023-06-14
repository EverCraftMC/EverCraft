package io.github.evercraftmc.core;

import java.net.InetAddress;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ECPlayerData {
    public static class Player {
        // Core
        public UUID uuid;
        public String name;

        public String displayName = null;
        public String prefix = null;

        // Global
        public InetAddress lastIp = null;

        public Instant firstJoin = null;
        public Instant lastJoin = null;
        public long playTime = 0;

        public Player() {
            this(null, null);
        }

        public Player(UUID uuid, String name) {
            this.uuid = uuid;
            this.name = name;

            this.displayName = name;
        }
    }

    public Map<String, Player> players = new HashMap<String, Player>();
}