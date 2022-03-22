package com.kale_ko.evercraft.bungee.listeners;

import com.kale_ko.evercraft.bungee.BungeePlugin;
import com.kale_ko.evercraft.bungee.Util;
import com.vexsoftware.votifier.bungee.events.VotifierEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class VoteListener implements Listener {
    @EventHandler
    public void onPlayerJoin(VotifierEvent event) {
        ProxiedPlayer player = BungeePlugin.Instance.getProxy().getPlayer(event.getVote().getUsername());

        BungeePlugin.Instance.Console.info("Received vote from " + event.getVote().getServiceName() + " (" + event.getVote().getAddress() + ") as user " + event.getVote().getUsername());

        if (player != null) {
            Util.broadcastMessage("&c&lThanks for voting " + event.getVote().getUsername() + "! /vote");
        }
    }
}