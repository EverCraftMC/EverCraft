package io.github.evercraftmc.evercraft.limbo.listeners;

import java.io.IOException;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.loohp.limbo.events.EventHandler;
import com.loohp.limbo.events.player.PlayerChatEvent;
import com.loohp.limbo.utils.NamespacedKey;
import io.github.evercraftmc.evercraft.limbo.LimboMain;

public class MessageListener extends LimboListener {
    @EventHandler
    public void onChatMessage(PlayerChatEvent event) {
        event.setCancelled(true);

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("globalChat");
        out.writeUTF(LimboMain.getInstance().getServerName());
        out.writeUTF(event.getPlayer().getUniqueId().toString());
        out.writeUTF(event.getMessage());

        try {
            event.getPlayer().sendPluginMessage(new NamespacedKey("bungeecord:main"), out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}