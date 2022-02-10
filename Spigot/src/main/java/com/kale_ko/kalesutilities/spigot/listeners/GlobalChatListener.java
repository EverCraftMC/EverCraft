package com.kale_ko.kalesutilities.spigot.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.kale_ko.kalesutilities.spigot.SpigotPlugin;
import com.kale_ko.kalesutilities.spigot.Util;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class GlobalChatListener implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (channel.equals("BungeeCord")) {
            ByteArrayDataInput in = ByteStreams.newDataInput(message);

            if (in.readUTF().equals("globalChat")) {
                if (!in.readUTF().equals(SpigotPlugin.Instance.config.getString("config.serverName"))) {
                    Util.broadcastMessage(in.readUTF(), true);
                }
            }
        }
    }
}