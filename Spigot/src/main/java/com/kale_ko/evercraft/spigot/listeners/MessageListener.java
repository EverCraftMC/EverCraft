package com.kale_ko.evercraft.spigot.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.kale_ko.evercraft.spigot.SpigotMain;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class MessageListener implements Listener, PluginMessageListener {
    @EventHandler
    public void onChatMessage(AsyncPlayerChatEvent event) {
        event.setCancelled(true);

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("globalChat");
        out.writeUTF(SpigotMain.getInstance().getServerName());
        out.writeUTF(event.getPlayer().getUniqueId().toString());
        out.writeUTF(event.getMessage());

        SpigotMain.getInstance().getServer().sendPluginMessage(SpigotMain.getInstance(), "BungeeCord", out.toByteArray());
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (channel.equals("BungeeCord")) {
            ByteArrayDataInput in = ByteStreams.newDataInput(message);

            String subChannel = in.readUTF();
            if (subChannel.equals("GetServer")) {
                SpigotMain.getInstance().setServerName(in.readUTF());
            } else if (subChannel.equals("globalMessage")) {
                SpigotMain.getInstance().getServer().broadcastMessage(in.readUTF());
            } else if (subChannel.equals("globalPermMessage")) {
                SpigotMain.getInstance().getServer().broadcast(in.readUTF(), in.readUTF());
            }
        }
    }
}