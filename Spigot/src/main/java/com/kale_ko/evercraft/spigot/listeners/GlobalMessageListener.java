package com.kale_ko.evercraft.spigot.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.kale_ko.evercraft.spigot.SpigotPlugin;
import com.kale_ko.evercraft.spigot.Util;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class GlobalMessageListener implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (channel.equals("BungeeCord")) {
            ByteArrayDataInput in = ByteStreams.newDataInput(message);

            String subchannel = in.readUTF();

            if (subchannel.equals("globalChat")) {
                if (!in.readUTF().equals(SpigotPlugin.Instance.config.getString("config.serverName"))) {
                    Util.broadcastMessage(in.readUTF(), true);
                }
            } else if (subchannel.equals("globalStaffChat")) {
                if (!in.readUTF().equals(SpigotPlugin.Instance.config.getString("config.serverName"))) {
                    String text = in.readUTF();

                    for (Player player2 : SpigotPlugin.Instance.getServer().getOnlinePlayers()) {
                        if (Util.hasPermission(player2, "evercraft.commands.staff.staffchat")) {
                            Util.sendMessage(player2, text, true);
                        }
                    }
                }
            } else if (subchannel.equals("globalCommandspy")) {
                if (!in.readUTF().equals(SpigotPlugin.Instance.config.getString("config.serverName"))) {
                    String text = in.readUTF();

                    for (Player player2 : SpigotPlugin.Instance.getServer().getOnlinePlayers()) {
                        if (Util.hasPermission(player2, "evercraft.commands.staff.commandspy") && (Util.hasMetadata(player2, "receiveCommandSpy") && Util.getMetadata(player2, "receiveCommandSpy").asBoolean())) {
                            Util.sendMessage(player2, text, true);
                        }
                    }
                }
            }
        }
    }
}