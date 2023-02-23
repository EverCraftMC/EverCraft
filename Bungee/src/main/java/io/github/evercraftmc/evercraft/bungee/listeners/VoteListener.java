package io.github.evercraftmc.evercraft.bungee.listeners;

import java.io.IOException;
import com.vexsoftware.votifier.bungee.events.VotifierEvent;
import io.github.evercraftmc.evercraft.bungee.BungeeMain;
import io.github.evercraftmc.evercraft.bungee.util.formatting.ComponentFormatter;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.event.EventHandler;

public class VoteListener extends BungeeListener {
    @EventHandler
    public void onVote(VotifierEvent event) {
        ProxiedPlayer player = BungeeMain.getInstance().getProxy().getPlayer(event.getVote().getUsername());

        if (player != null) {
            VoteListener.process(player);
        } else {
            BungeeMain.getInstance().getPluginData().get().votes.get(event.getVote().getUsername()).toProcess = BungeeMain.getInstance().getPluginData().get().votes.get(event.getVote().getUsername()).toProcess + 1;
            try {
                BungeeMain.getInstance().getPluginData().save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void process(ProxiedPlayer player) {
        BungeeMain.getInstance().getProxy().broadcast(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().get().vote.replace("{player}", player.getDisplayName()))));

        BungeeMain.getInstance().getDiscordBot().getGuild().getTextChannelById(BungeeMain.getInstance().getPluginConfig().get().discord.channelId).sendMessage(TextFormatter.discordFormat(BungeeMain.getInstance().getPluginMessages().get().vote.replace("{player}", player.getDisplayName()))).queue();

        BungeeMain.getInstance().getPluginData().get().votes.get(player.getName()).total = BungeeMain.getInstance().getPluginData().get().votes.get(player.getName()).total + 1;
        try {
            BungeeMain.getInstance().getPluginData().save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}