package com.kale_ko.kalesutilities.listeners;

import com.kale_ko.kalesutilities.Util;
import com.kale_ko.kalesutilities.Config;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Config config = Config.load("players.yml");
        YamlConfiguration data = config.getConfig();

        if (data.getString("players." + event.getPlayer().getName() + ".nickname") == null) {
            data.set("players." + event.getPlayer().getName() + ".nickname", event.getPlayer().getName());
        }

        if (data.getString("players." + event.getPlayer().getName() + ".prefix") == null) {
            data.set("players." + event.getPlayer().getName() + ".prefix", "");
        }

        config.save();

        Util.updatePlayerName(event.getPlayer());
    }
}