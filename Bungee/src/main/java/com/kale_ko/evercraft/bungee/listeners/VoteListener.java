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
            VoteListener.process(player);
        } else {
            if (BungeeMain.getInstance().getData().getFloat("votes." + event.getVote().getUsername() + ".toProcess") == null) {
                BungeeMain.getInstance().getData().set("votes." + event.getVote().getUsername() + ".toProcess", 1);
            } else {
                BungeeMain.getInstance().getData().set("votes." + event.getVote().getUsername() + ".toProcess", BungeeMain.getInstance().getData().getFloat("votes." + event.getVote().getUsername() + ".toProcess") + 1);
            }
        }
    }

    public static void process(ProxiedPlayer player) {
        BungeeMain.getInstance().getProxy().broadcast(TextComponent.fromLegacyText(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("vote").replace("{player}", player.getDisplayName()))));

        BungeeMain.getInstance().getDiscordBot().getGuild().getTextChannelById(BungeeMain.getInstance().getPluginConfig().getString("discord.channelId")).sendMessage(TextFormatter.discordFormat(BungeeMain.getInstance().getPluginMessages().getString("vote").replace("{player}", player.getDisplayName()))).queue();

        if (BungeeMain.getInstance().getData().getFloat("votes." + player.getDisplayName() + ".total") == null) {
            BungeeMain.getInstance().getData().set("votes." + player.getDisplayName() + ".total", 1);
        } else {
            BungeeMain.getInstance().getData().set("votes." + player.getDisplayName() + ".total", BungeeMain.getInstance().getData().getFloat("votes." + player.getDisplayName() + ".total") + 1);
        }
    }
}