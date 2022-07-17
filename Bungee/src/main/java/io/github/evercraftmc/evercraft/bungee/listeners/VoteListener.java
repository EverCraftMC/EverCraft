package io.github.evercraftmc.evercraft.bungee.listeners;

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
            if (BungeeMain.getInstance().getPluginData().getFloat("votes." + event.getVote().getUsername() + ".toProcess") == null) {
                BungeeMain.getInstance().getPluginData().set("votes." + event.getVote().getUsername() + ".toProcess", 1);
            } else {
                BungeeMain.getInstance().getPluginData().set("votes." + event.getVote().getUsername() + ".toProcess", BungeeMain.getInstance().getPluginData().getFloat("votes." + event.getVote().getUsername() + ".toProcess") + 1);
            }
        }
    }

    public static void process(ProxiedPlayer player) {
        BungeeMain.getInstance().getProxy().broadcast(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().vote.replace("{player}", player.getDisplayName()))));

        BungeeMain.getInstance().getDiscordBot().getGuild().getTextChannelById(BungeeMain.getInstance().getPluginConfig().getParsed().discord.channelId).sendMessage(TextFormatter.discordFormat(BungeeMain.getInstance().getPluginMessages().getParsed().vote.replace("{player}", player.getDisplayName()))).queue();

        if (BungeeMain.getInstance().getPluginData().getFloat("votes." + player.getDisplayName() + ".total") == null) {
            BungeeMain.getInstance().getPluginData().set("votes." + player.getDisplayName() + ".total", 1);
        } else {
            BungeeMain.getInstance().getPluginData().set("votes." + player.getDisplayName() + ".total", BungeeMain.getInstance().getPluginData().getFloat("votes." + player.getDisplayName() + ".total") + 1);
        }
    }
}