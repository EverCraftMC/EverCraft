package com.kale_ko.kalesutilities.bungee.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.kale_ko.kalesutilities.bungee.Util;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class GlobalChatListener implements Listener {
    @EventHandler
    public void onMessage(PluginMessageEvent event) {
        if (event.getTag().equals("BungeeChord")) {
            ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());

            if (in.readUTF().equals("globalMessage")) {
                Util.messageServers("globalMessage", in.readUTF(), in.readUTF());
            }
        }
    }
}