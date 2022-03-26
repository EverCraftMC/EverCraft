package com.kale_ko.evercraft.bungee.listeners;

import com.kale_ko.evercraft.bungee.BungeeMain;
import com.kale_ko.evercraft.shared.util.formatting.TextFormatter;
import com.vexsoftware.votifier.bungee.events.VotifierEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.event.EventHandler;

public class VoteListener extends BungeeListener {
    @EventHandler
    public void onVote(VotifierEvent event) {
        ProxiedPlayer player = BungeeMain.getInstance().getProxy().getPlayer(event.getVote().getUsername());

        if (player != null) {
            player.sendMessage(TextComponent.fromLegacyText(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("vote").replace("{player}", event.getVote().getUsername()))));

            if (BungeeMain.getInstance().getPlayerData().getFloat("votes." + event.getVote().getUsername() + ".total") == null) {
                BungeeMain.getInstance().getPlayerData().set("votes." + event.getVote().getUsername() + ".total", 1);
            } else {
                BungeeMain.getInstance().getPlayerData().set("votes." + event.getVote().getUsername() + ".total", BungeeMain.getInstance().getPlayerData().getFloat("votes." + event.getVote().getUsername() + ".total") + 1);
            }
        } else {
            if (BungeeMain.getInstance().getPlayerData().getFloat("votes." + event.getVote().getUsername() + ".total") == null) {
                BungeeMain.getInstance().getPlayerData().set("votes." + event.getVote().getUsername() + ".toProcess", 1);
            } else {
                BungeeMain.getInstance().getPlayerData().set("votes." + event.getVote().getUsername() + ".toProcess", BungeeMain.getInstance().getPlayerData().getFloat("votes." + event.getVote().getUsername() + ".toProcess") + 1);
            }
        }
    }
}