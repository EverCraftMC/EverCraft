package com.kale_ko.evercraft.bungee.listeners;

import com.kale_ko.evercraft.bungee.BungeeMain;
import com.kale_ko.evercraft.shared.util.formatting.TextFormatter;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.event.EventHandler;

public class WelcomeListener extends BungeeListener {
    @EventHandler
    public void onPlayerJoin(PostLoginEvent event) {
        BungeeMain.getInstance().getPlayerData().set("players." + event.getPlayer().getUniqueId() + ".lastname", event.getPlayer().getName());

        if (BungeeMain.getInstance().getPlayerData().getString("players." + event.getPlayer().getUniqueId() + ".nickname") == null) {
            BungeeMain.getInstance().getPlayerData().set("players." + event.getPlayer().getUniqueId() + ".nickname", event.getPlayer().getName());
        }

        event.getPlayer().setDisplayName(BungeeMain.getInstance().getPlayerData().getString("players." + event.getPlayer().getUniqueId() + ".nickname"));

        if (!BungeeMain.getInstance().getPlayerData().getBoolean("players." + event.getPlayer().getUniqueId() + ".joinedBefore")) {
            BungeeMain.getInstance().getPlayerData().set("players." + event.getPlayer().getUniqueId() + ".joinedBefore", true);

            BungeeMain.getInstance().getProxy().broadcast(TextComponent.fromLegacyText(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("welcome.first").replace("{player}", event.getPlayer().getDisplayName()))));

            BungeeMain.getInstance().getDiscordBot().getGuild().getTextChannelById(BungeeMain.getInstance().getPluginConfig().getString("discord.channelId")).sendMessage(TextFormatter.discordFormat(BungeeMain.getInstance().getPluginMessages().getString("welcome.first").replace("{player}", event.getPlayer().getDisplayName()))).queue();
        }

        BungeeMain.getInstance().getProxy().broadcast(TextComponent.fromLegacyText(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("welcome.join").replace("{player}", event.getPlayer().getDisplayName()))));

        BungeeMain.getInstance().getDiscordBot().getGuild().getTextChannelById(BungeeMain.getInstance().getPluginConfig().getString("discord.channelId")).sendMessage(TextFormatter.discordFormat(BungeeMain.getInstance().getPluginMessages().getString("welcome.join").replace("{player}", event.getPlayer().getDisplayName()))).queue();
    }

    @EventHandler
    public void onPlayerQuit(PlayerDisconnectEvent event) {
        BungeeMain.getInstance().getProxy().broadcast(TextComponent.fromLegacyText(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("welcome.quit").replace("{player}", event.getPlayer().getDisplayName()))));
    }
}