package com.kale_ko.evercraft.shared.networking;

import java.util.List;

public class ServerPing {
    private ServerMotd description;
    private ServerPlayers players;
    private ServerVersion version;
    private String favicon;
    private int ping;

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
        private String text;
        private String color;

        private Boolean bold;
        private Boolean italic;
        private Boolean underline;

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
        private List<ServerMotdComponent> extra;

        public List<ServerMotdComponent> getExtra() {
            return this.extra;
        }
    }

    public class ServerPlayers {
        private int online;
        private int max;
        private List<ServerPlayer> sample;

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
            private String name;
            private String id;

            public String getName() {
                return name;
            }

            public String getUniqueId() {
                return id;
            }
        }
    }

    public class ServerVersion {
        private String name;
        private String protocol;

        public String getName() {
            return name;
        }

        public String getProtocol() {
            return protocol;
        }
    }
}