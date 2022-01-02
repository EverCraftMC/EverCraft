package com.kale_ko.kalesutilities.listeners;

import com.kale_ko.kalesutilities.Config;
import java.util.Date;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class SeenListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Config config = Config.load("players.yml");
        YamlConfiguration data = config.getConfig();

        data.set("players." + event.getPlayer().getName() + ".lastOnline", new Date().getTime());

        config.save();
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Config config = Config.load("players.yml");
        YamlConfiguration data = config.getConfig();

        data.set("players." + event.getPlayer().getName() + ".lastOnline", new Date().getTime());

        config.save();
    }
}