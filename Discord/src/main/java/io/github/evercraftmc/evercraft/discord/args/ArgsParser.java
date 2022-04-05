package io.github.evercraftmc.evercraft.discord.args;

import java.time.Duration;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class ArgsParser {
    public static String[] getRawArgs(Message message) {
        return message.getContentRaw().trim().split(" ");
    }

    public static String getRawArg(Message message, Integer index) {
        return message.getContentRaw().trim().split(" ")[index];
    }

    public static Boolean hasArg(Message message, Integer index) {
        return message.getContentRaw().trim().split(" ").length > index;
    }

    public static String getStringArg(Message message, Integer start) {
        StringBuilder string = new StringBuilder();

        for (Integer index = start; index < getRawArgs(message).length; index++) {
            string.append(getWordArg(message, index) + " ");
        }

        return string.toString().trim();
    }

    public static String getWordArg(Message message, Integer index) {
        return getRawArg(message, index);
    }

    public static Float getFloatArg(Message message, Integer index) throws NumberFormatException {
        return Float.parseFloat(getWordArg(message, index));
    }

    public static Integer getIntegerArg(Message message, Integer index) throws NumberFormatException {
        return Integer.parseInt(getWordArg(message, index));
    }

    public static Boolean getBooleanArg(Message message, Integer index) {
        if (getWordArg(message, index).equalsIgnoreCase("true") || getWordArg(message, index).equalsIgnoreCase("yes")) {
            return true;
        } else if (getWordArg(message, index).equalsIgnoreCase("false") || getWordArg(message, index).equalsIgnoreCase("no")) {
            return false;
        } else {
            return null;
        }
    }

    public static Duration getDurrationArg(Message message, Integer index) throws NumberFormatException, ArithmeticException {
        String string = getWordArg(message, index);

        if (string.endsWith("s") || string.endsWith("S")) {
            return Duration.ofSeconds(Long.parseLong(string.substring(0, string.length() - 1)));
        } else if (string.endsWith("m") || string.endsWith("M")) {
            return Duration.ofMinutes(Long.parseLong(string.substring(0, string.length() - 1)));
        } else if (string.endsWith("h") || string.endsWith("H")) {
            return Duration.ofHours(Long.parseLong(string.substring(0, string.length() - 1)));
        } else if (string.endsWith("d") || string.endsWith("D")) {
            return Duration.ofDays(Long.parseLong(string.substring(0, string.length() - 1)));
        } else {
            throw new ArithmeticException("Invalid time unit");
        }
    }

    public static User getUserArg(Message message, Integer index) throws NumberFormatException {
        return message.getJDA().retrieveUserById(getWordArg(message, index).replaceAll("<@(!)?", "").replace(">", "")).complete();
    }

    public static Member getMemberArg(Message message, Integer index) throws NumberFormatException {
        return message.getGuild().retrieveMember(getUserArg(message, index)).complete();
    }

    public static Role getRoleArg(Message message, Integer index) {
        return message.getGuild().getRoleById(getWordArg(message, index).replaceAll("<@(!)?", "").replace(">", ""));
    }

    public static GuildChannel getChannelArg(Message message, Integer index) {
        return message.getGuild().getGuildChannelById(getWordArg(message, index).replaceAll("<#(!)?", "").replace(">", ""));
    }

    public static TextChannel getTextChannelArg(Message message, Integer index) {
        return message.getGuild().getTextChannelById(getWordArg(message, index).replaceAll("<#(!)?", "").replace(">", ""));
    }

    public static VoiceChannel getVoiceChannelArg(Message message, Integer index) {
        return message.getGuild().getVoiceChannelById(getWordArg(message, index).replaceAll("<#(!)?", "").replace(">", ""));
    }
}