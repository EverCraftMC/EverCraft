package io.github.evercraftmc.moderation.listeners;

import io.github.evercraftmc.core.api.events.ECHandler;
import io.github.evercraftmc.core.api.events.ECHandlerOrder;
import io.github.evercraftmc.core.api.events.ECListener;
import io.github.evercraftmc.core.api.events.player.PlayerChatEvent;
import io.github.evercraftmc.core.api.server.player.ECPlayer;
import io.github.evercraftmc.core.impl.util.ECTextFormatter;
import io.github.evercraftmc.moderation.ModerationModule;

public class StaffChatListener implements ECListener {
    protected final ModerationModule parent;

    public StaffChatListener(ModerationModule parent) {
        this.parent = parent;
    }

    @ECHandler(order=ECHandlerOrder.AFTER)
    public void onPlayerChat(PlayerChatEvent event) {
        if (event.getType() == PlayerChatEvent.MessageType.CHAT && parent.getPlugin().getPlayerData().players.get(event.getPlayer().getUuid().toString()).staffchat && event.getPlayer().hasPermission("evercraft.moderation.commands.staffChat")) {
            event.setCancelled(true);

            for (ECPlayer player2 : parent.getPlugin().getServer().getOnlinePlayers()) {
                if (player2.hasPermission("evercraft.moderation.commands.staffChat")) {
                    player2.sendMessage(ECTextFormatter.translateColors("&d&l[Staffchat] &r" + event.getMessage().trim()));
                }
            }
        }
    }
}