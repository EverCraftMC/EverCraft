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

        String message = event.getMessage().trim().replaceAll(" +", " ");
        String[] words = message.split(" ");

        int index = 0;
        for (String word : words) {
            if (bannedWords.contains(Util.stripFormating(word).toLowerCase())) {
                words[index] = Util.formatMessage("&k") + word + Util.formatMessage("&r");
            }

            if (index != 0 && index != words.length - 1) {
                if (Util.stripFormating(word).length() == 1 && (Util.stripFormating(words[index - 1]).length() == 1 || Util.stripFormating(words[index + 1]).length() == 1)) {
                    words[index] = Util.formatMessage("&k") + word + Util.formatMessage("&r");
                }
            } else if (index != 0) {
                if (Util.stripFormating(word).length() == 1 && Util.stripFormating(words[index - 1]).length() == 1) {
                    words[index] = Util.formatMessage("&k") + word + Util.formatMessage("&r");
                }
            } else if (index != words.length - 1) {
                if (Util.stripFormating(word).length() == 1 && Util.stripFormating(words[index + 1]).length() == 1) {
                    words[index] = Util.formatMessage("&k") + word + Util.formatMessage("&r");
                }
            }

            index++;
        }

        event.setMessage(String.join(" ", words));
    }
}