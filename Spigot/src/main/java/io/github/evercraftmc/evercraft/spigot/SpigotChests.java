package io.github.evercraftmc.evercraft.spigot;

import java.util.HashMap;
import java.util.Map;

public class SpigotChests {
    public static class Chest {
        public String owner = "";
        public Boolean isProtected = true;
        public Boolean allowUse = false;
    }

    public Map<String, Chest> blocks = new HashMap<String, Chest>();
}