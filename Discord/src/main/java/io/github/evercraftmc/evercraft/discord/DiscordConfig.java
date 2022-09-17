package io.github.evercraftmc.evercraft.discord;

import net.dv8tion.jda.api.entities.Activity.ActivityType;

public class DiscordConfig {
    public static class Database {
        public String host = "localhost";
        public Integer port = 3306;

        public String name = "evercraft";

        public String username = "root";
        public String password = "";

        public String tableName = "evercraft";
    }

    public static class Discord {
        public String token = "";

        public String guildId = "";

        public ActivityType statusType = ActivityType.WATCHING;
        public String status = "the Discord";
    }

    public Database database = new Database();

    public Discord discord = new Discord();
}
