package io.github.evercraftmc.evercraft.shared;

import java.util.HashMap;
import java.util.Map;

public class PluginData {
    public static class Player {
        public static class Settings {
            public Boolean passive = false;

            public Boolean commandspy = false;
        }

        public static class Ban {
            public Boolean banned = false;

            public String reason = null;
            public String until = null;
            public String by = null;
        }

        public static class Mute {
            public Boolean muted = false;

            public String reason = null;
            public String until = null;
            public String by = null;
        }

        public String uuid = "";
        public String lastName = "";
        public String lastIP = "";

        public Long lastOnline = null;

        public Settings settings = new Settings();

        public Ban ban = new Ban();
        public Mute mute = new Mute();
    }

    public static class Linking {
        public String account;
        public Long expires;
    }

    public static class Vote {
        public Integer total = 0;

        public Integer toProcess = 0;
    }

    public Map<String, Player> players = new HashMap<String, Player>();

    public Map<String, Vote> votes = new HashMap<String, Vote>();

    public Boolean chatLock = false;
    public Boolean maintenance = false;
}