package io.github.evercraftmc.global.listeners;
import io.github.evercraftmc.core.ECPluginManager;
import io.github.evercraftmc.core.api.events.ECHandler;
import io.github.evercraftmc.core.api.events.ECListener;
import io.github.evercraftmc.core.api.events.player.PlayerChatEvent;
import io.github.evercraftmc.core.impl.ECEnvironmentType;
import io.github.evercraftmc.core.impl.util.ECTextFormatter;
import io.github.evercraftmc.global.GlobalModule;

public class ChatListener implements ECListener {
    private final GlobalModule parent = ECPluginManager.getModule(GlobalModule.class);

    @ECHandler
    public void onPlayerChat(PlayerChatEvent event) {
        if (parent.getPlugin().getEnvironment().getType() == ECEnvironmentType.PROXY) {
            event.setMessage(ECTextFormatter.translateColors("&r" + event.getPlayer().getDisplayName() + " &r&7> " + ECTextFormatter.stripColors(event.getMessage())));
        } else {
            event.setCancelled(true);
        }
    }
}