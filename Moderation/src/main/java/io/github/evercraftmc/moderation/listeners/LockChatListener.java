package io.github.evercraftmc.moderation.listeners;

import io.github.evercraftmc.core.api.events.ECHandler;
import io.github.evercraftmc.core.api.events.ECListener;
import io.github.evercraftmc.core.api.events.player.PlayerChatEvent;
import io.github.evercraftmc.core.impl.util.ECTextFormatter;
import io.github.evercraftmc.moderation.ModerationModule;

public class LockChatListener implements ECListener {
    protected final ModerationModule parent;

    public LockChatListener(ModerationModule parent) {
        this.parent = parent;
    }

    @ECHandler
    public void onPlayerChat(PlayerChatEvent event) {
        if (parent.getPlugin().getPlayerData().chatLocked && !event.getPlayer().hasPermission("evercraft.moderation.commands.lockChat.bypass")) {
            event.setCancelled(true);
            event.setCancelReason(ECTextFormatter.translateColors("&cThe chat is locked"));
        }
    }
}