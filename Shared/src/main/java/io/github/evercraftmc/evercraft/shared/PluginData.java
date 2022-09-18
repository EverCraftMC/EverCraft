package io.github.evercraftmc.evercraft.shared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PluginData {
    public static class Player {
        public static class Settings {
            public enum MessageSetting {
                EVERYONE, FRIENDS, NOONE
            }

            public MessageSetting messaging = MessageSetting.EVERYONE;

            public Boolean passive = false;

            public Boolean commandspy = false;
        }

        public static class FriendInvite {
            public String uuid;
            public Boolean inbound;

            public FriendInvite(String uuid, Boolean inbound) {
                this.uuid = uuid;
                this.inbound = inbound;
            }
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

        public String nickname = null;

        public Boolean joinedBefore = false;
        public Long lastOnline = null;

        public Float balance = 0f;

        public List<String> friends = new ArrayList<String>();
        public List<FriendInvite> friendInvites = new ArrayList<FriendInvite>();

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