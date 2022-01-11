package com.kale_ko.kalesutilities.listeners;

import com.kale_ko.kalesutilities.Main;
import java.util.Date;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class SeenListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Main.Instance.seen.set(event.getPlayer().getName(), new Date().getTime());

        Main.Instance.seen.save();
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Main.Instance.seen.set(event.getPlayer().getName(), new Date().getTime());

        Main.Instance.seen.save();
    }
}