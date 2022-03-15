package com.kale_ko.evercraft.shared.networking;

import java.util.ArrayList;
import java.util.List;

public class ServerPing {
    private ServerMotd description = new ServerMotd();
    private ServerPlayers players = new ServerPlayers();
    private ServerVersion version = new ServerVersion();
    private String favicon = "";
    private int ping = -1;

    public ServerMotd getMotd() {
        return this.description;
    }

    public ServerPlayers getPlayers() {
        return this.players;
    }

    public ServerVersion getVersion() {
        return this.version;
    }

    public String getFavicon() {
        return this.favicon;
    }

    public int getPing() {
        return this.ping;
    }

    public void setPing(int ping) {
        this.ping = ping;
    }

    public class ServerMotdComponent {
        private String text = "A Minecraft Server!";
        private String color = "white";

        private Boolean bold = false;
        private Boolean italic = false;
        private Boolean underline = false;

        public String getText() {
            return this.text;
        }

        public String getColor() {
            return this.color;
        }

        public Boolean getBold() {
            return this.bold;
        }

        public Boolean getItalic() {
            return this.italic;
        }

        public Boolean getUnderline() {
            return this.underline;
        }
    }

    public class ServerMotd extends ServerMotdComponent {
        private List<ServerMotdComponent> extra = new ArrayList<ServerMotdComponent>();

        public List<ServerMotdComponent> getExtra() {
            return this.extra;
        }
    }

    public class ServerPlayers {
        private int online = 0;
        private int max = 20;
        private List<ServerPlayer> sample = new ArrayList<ServerPlayer>();

        public int getOnline() {
            return online;
        }

        public int getMax() {
            return max;
        }

        public List<ServerPlayer> getOnlinePlayers() {
            return sample;
        }

        public class ServerPlayer {
            private String name = "Player";
            private String id = "00000000-0000-0000-0000-000000000000";

            public String getName() {
                return name;
            }

            public String getUniqueId() {
                return id;
            }
        }
    }

    public class ServerVersion {
        private String name = "Unknown";
        private String protocol = "-1";

        public String getName() {
            return name;
        }

        public String getProtocol() {
            return protocol;
        }
    }
}