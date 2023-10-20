package io.github.evercraftmc.moderation.listeners;

import io.github.evercraftmc.core.api.events.ECHandler;
import io.github.evercraftmc.core.api.events.ECHandlerOrder;
import io.github.evercraftmc.core.api.events.ECListener;
import io.github.evercraftmc.core.api.events.player.PlayerCommandEvent;
import io.github.evercraftmc.core.api.server.player.ECPlayer;
import io.github.evercraftmc.core.impl.util.ECTextFormatter;
import io.github.evercraftmc.moderation.ModerationModule;

public class CommandSpyListener implements ECListener {
    protected final ModerationModule parent;

    public CommandSpyListener(ModerationModule parent) {
        this.parent = parent;
    }

    @ECHandler(order=ECHandlerOrder.BEFORE)
    public void onPlayerChat(PlayerCommandEvent event) {
        for (ECPlayer player2 : parent.getPlugin().getServer().getOnlinePlayers()) {
            if (parent.getPlugin().getPlayerData().players.get(player2.getUuid().toString()).commandSpy && player2.hasPermission("evercraft.moderation.commands.commandSpy")) {
                player2.sendMessage(ECTextFormatter.translateColors("&d&l[CommandSpy] &r" + event.getPlayer().getDisplayName() + " &r> ") + event.getCommand().trim());
            }
        }
    }
}