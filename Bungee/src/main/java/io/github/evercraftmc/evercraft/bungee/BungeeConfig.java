package io.github.evercraftmc.evercraft.bungee;

import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.entities.Activity.ActivityType;

public class BungeeConfig {
    public static class Database {
        public String host = "localhost";
        public Integer port = 3306;

        public String name = "evercraft";

        public String username = "root";
        public String password = "";

        public String tableName = "evercraft";
    }

    public static class Server {
        public String main = "hub";
        public String fallback = "fallback";
    }

    public static class Discord {
        public String token = "";

        public String guildId = "";
        public String channelId = "";
        public String staffChannelId = "";

        public ActivityType statusType = ActivityType.PLAYING;
        public String status = "on play.evercraft.ga";
    }

    public static class Scoreboard {
        public String title = "&3&lEverCraft";

        public List<String> lines = new ArrayList<String>();
    }

    public static class TabList {
        public String header = "";
        public String footer = "";
    }

    public Database database = new Database();

    public Server server = new Server();

    public Discord discord = new Discord();

    public Scoreboard scoreboard = new Scoreboard();

    public TabList tabList = new TabList();

    public List<String> testerIps = new ArrayList<String>();
}