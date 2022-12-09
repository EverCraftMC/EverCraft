package io.github.evercraftmc.evercraft.limbo.listeners;

import java.io.IOException;
import java.util.UUID;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.loohp.limbo.events.EventHandler;
import com.loohp.limbo.events.player.PlayerChatEvent;
import com.loohp.limbo.events.player.PluginMessageEvent;
import com.loohp.limbo.player.Player;
import io.github.evercraftmc.evercraft.limbo.LimboMain;
import io.github.evercraftmc.evercraft.shared.util.StringUtils;
import net.kyori.adventure.key.Key;

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
            event.getPlayer().sendPluginMessage(Key.key("bungeecord:main"), out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onPluginMessageReceived(PluginMessageEvent event) {
        if (event.getChannel().equals("BungeeCord") || event.getChannel().equals("bungee:main")) {
            ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());

            String subChannel = in.readUTF();
            if (subChannel.equals("GetServer")) {
                LimboMain.getInstance().setServerName(StringUtils.toTtitleCase(in.readUTF()));
            } else if (subChannel.equals("crossCommand")) {
                Player player = LimboMain.getInstance().getServer().getPlayer(UUID.fromString(in.readUTF()));

                String command = in.readUTF();

                LimboMain.getInstance().getServer().dispatchCommand(player, command);
            }
        }
    }
}