package io.github.evercraftmc.evercraft.spigot;

import java.util.Arrays;
import java.util.List;

public class SpigotConfig {
    public static class Database {
        public String host = "localhost";
        public Integer port = 3306;

        public String name = "evercraft";

        public String username = "root";
        public String password = "";
    }

    public static class Warp {
        public boolean overrideSpawn = false;
        public boolean clearOnWarp = false;
    }

    public static class ChestProtection {
        public Boolean enabled = false;

        public List<String> protectable = Arrays.asList("chest:0", "trapped_chest:0", "ender_chest:1", "barrel:0", "shulker_box:0", "{color}_shulker_box:0", "furnace:0", "blast_furnace:0", "smoker:0", "hopper:0", "dropper:0", "dispenser:0", "jukebox:0", "{color}_bed:1");
    }

    public static class Games {
        public Boolean enabled = false;
    }

    public String serverName = "unknown";

    public Database database = new Database();

    public Warp warp = new Warp();

    public Boolean passiveEnabled = false;
    public ChestProtection chestProtection = new ChestProtection();

    public Games games = new Games();
}