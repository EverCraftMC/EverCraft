package com.kale_ko.kalesutilities.listeners;

import com.kale_ko.kalesutilities.Main;
import com.kale_ko.kalesutilities.Util;
import java.util.List;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

@SuppressWarnings("deprecation")
public class ChatFilterListener implements Listener {
    @EventHandler
    public void onChatMessage(PlayerChatEvent event) {
        List<String> bannedWords = Main.Instance.config.getStringList("config.banned-words");

        String message = event.getMessage();

        for (String bannedWord : bannedWords) {
            if (message.contains(bannedWord)) {
                message = message.substring(0, message.indexOf(bannedWord)) + Util.formatMessage("&k") + message.substring(message.indexOf(bannedWord), message.indexOf(bannedWord) + bannedWord.length()) + Util.formatMessage("&r") + message.substring(message.indexOf(bannedWord) + bannedWord.length(), message.length());
            }
        }

        event.setMessage(message);
    }
}