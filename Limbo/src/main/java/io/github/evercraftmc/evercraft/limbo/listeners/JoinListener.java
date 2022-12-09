package io.github.evercraftmc.evercraft.limbo.listeners;

import java.io.IOException;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.loohp.limbo.events.EventHandler;
import com.loohp.limbo.events.player.PlayerJoinEvent;
import net.kyori.adventure.key.Key;

public class JoinListener extends LimboListener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        try {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("GetServer");
            event.getPlayer().sendPluginMessage(Key.key("bungee:main"), out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}