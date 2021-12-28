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
        String[] words = message.split(" ");

        int index = 0;
        for (String word : words) {
            if (bannedWords.contains(word)) {
                words[index] = Util.formatMessage("&k") + word + Util.formatMessage("&r");
            }
            index++;
        }

        // for (String bannedWord : bannedWords) {
        //     int lastFound = 0;
        //     while (message.contains(bannedWord)) {
        //         message = message.substring(0, message.indexOf(bannedWord, lastFound)) + Util.formatMessage("&k") + message.substring(message.indexOf(bannedWord, lastFound), message.indexOf(bannedWord, lastFound) + bannedWord.length()) + Util.formatMessage("&r") + message.substring(message.indexOf(bannedWord, lastFound) + bannedWord.length(), message.length());
        //         lastFound = message.indexOf(bannedWord, lastFound);
        //     }
        // }

        event.setMessage(String.join(" ", words));
    }
}