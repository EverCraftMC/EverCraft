package io.github.evercraftmc.evercraft.spigot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpigotChests {
    public static class Chest {
        public String owner = "";
        public Boolean isProtected = true;
        public Boolean allowUse = false;
    }

    public static class Player {
        public Boolean autoClaim = true;

        public List<String> friends = new ArrayList<String>();
    }

    public Map<String, Chest> blocks = new HashMap<String, Chest>();

    public Map<String, Player> players = new HashMap<String, Player>();
}