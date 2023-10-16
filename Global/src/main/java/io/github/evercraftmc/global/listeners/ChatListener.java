package io.github.evercraftmc.global.listeners;

import io.github.evercraftmc.core.ECPluginManager;
import io.github.evercraftmc.core.api.events.ECHandler;
import io.github.evercraftmc.core.api.events.ECListener;
import io.github.evercraftmc.core.api.events.messaging.MessageEvent;
import io.github.evercraftmc.core.api.events.player.PlayerChatEvent;
import io.github.evercraftmc.core.api.server.player.ECPlayer;
import io.github.evercraftmc.core.impl.ECEnvironmentType;
import io.github.evercraftmc.core.impl.util.ECTextFormatter;
import io.github.evercraftmc.core.messaging.ECMessage;
import io.github.evercraftmc.core.messaging.ECMessageType;
import io.github.evercraftmc.core.messaging.ECRecipient;
import io.github.evercraftmc.global.GlobalModule;
import java.io.*;
import java.util.UUID;

public class ChatListener implements ECListener {
    private final GlobalModule parent = ECPluginManager.getModule(GlobalModule.class);

    @ECHandler
    public void onPlayerChat(PlayerChatEvent event) {
        if (parent.getPlugin().getEnvironment().getType() == ECEnvironmentType.PROXY) {
            if (event.getType() == PlayerChatEvent.MessageType.CHAT) {
                event.setMessage(ECTextFormatter.translateColors("&r" + event.getPlayer().getDisplayName() + " &r> " + ECTextFormatter.stripColors(event.getMessage())));
            } else {
                String message = event.getMessage();
                for (ECPlayer player : parent.getPlugin().getServer().getOnlinePlayers()) {
                    message = message.replace(player.getName(), player.getDisplayName());
                }

                event.setMessage(ECTextFormatter.translateColors("&r" + message));
            }
        } else {
            if (event.getType() != PlayerChatEvent.MessageType.CHAT) {
                try {
                    ByteArrayOutputStream chatMessageData = new ByteArrayOutputStream();
                    DataOutputStream chatMessage = new DataOutputStream(chatMessageData);
                    chatMessage.writeInt(ECMessageType.GLOBAL_CHAT);
                    chatMessage.writeUTF(event.getPlayer().getUuid().toString());
                    chatMessage.writeUTF(event.getType().name());
                    chatMessage.writeUTF(event.getMessage());
                    chatMessage.close();

                    parent.getPlugin().getMessager().send(ECRecipient.fromEnvironmentType(ECEnvironmentType.PROXY), chatMessageData.toByteArray());
                } catch (IOException e) {
                    parent.getPlugin().getLogger().error("[Messager] Failed to send message", e);
                }
            }

            event.setCancelled(true);
        }
    }

    @ECHandler
    public void onGlobalChat(MessageEvent event) {
        ECMessage message = event.getMessage();

        if (!message.getSender().matches(parent.getPlugin().getServer()) && message.getRecipient().matches(parent.getPlugin().getServer())) {
            try {
                ByteArrayInputStream commandMessageData = new ByteArrayInputStream(message.getData());
                DataInputStream commandMessage = new DataInputStream(commandMessageData);

                int type = commandMessage.readInt();
                if (type == ECMessageType.GLOBAL_CHAT) {
                    UUID uuid = UUID.fromString(commandMessage.readUTF());
                    String chatType = commandMessage.readUTF();
                    String chat = commandMessage.readUTF();

                    ECPlayer player = parent.getPlugin().getServer().getOnlinePlayer(uuid);

                    onPlayerChat(new PlayerChatEvent(player, PlayerChatEvent.MessageType.valueOf(chatType), chat));
                }

                commandMessage.close();
            } catch (IOException e) {
                parent.getPlugin().getLogger().error("[Messager] Failed to read message", e);
            }
        }
    }
}