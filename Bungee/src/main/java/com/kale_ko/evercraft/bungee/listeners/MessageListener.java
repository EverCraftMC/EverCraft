package com.kale_ko.evercraft.bungee.listeners;

import java.util.UUID;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.kale_ko.evercraft.bungee.BungeeMain;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class MessageListener implements Listener {
    @EventHandler
    public void onMessage(PluginMessageEvent event) {
        if (event.getTag().equals("BungeeCord")) {
            ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());

            String subChannel = in.readUTF();

            if (subChannel.equals("globalChat")) {
                for (ServerInfo server : BungeeMain.getInstance().getProxy().getServers().values()) {
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();

                    out.writeUTF("globalMessage");
                    out.writeUTF("[" + in.readUTF() + "]" + BungeeMain.getInstance().getProxy().getPlayer(UUID.fromString(in.readUTF())) + " > " + in.readUTF());

                    server.sendData("BungeeCord", out.toByteArray());
                }
            } else if (subChannel.equals("globalMessage")) {
                for (ServerInfo server : BungeeMain.getInstance().getProxy().getServers().values()) {
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();

                    out.writeUTF("globalMessage");
                    out.writeUTF("[" + in.readUTF() + "] " + in.readUTF());

                    server.sendData("BungeeCord", out.toByteArray());
                }
            }
        }
    }
}