package com.kale_ko.evercraft.bungee.discord;

import javax.security.auth.login.LoginException;
import com.kale_ko.evercraft.shared.util.ParamRunnable;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class DiscordBot implements EventListener {
    private String token;

    private String serverID;
    private String minecraftChannelID;
    private String staffMinecraftChannelID;
    private String onlineChannelID;

    private ParamRunnable messageCallback;
    private ParamRunnable staffMessageCallback;

    public JDA jda;

    public DiscordBot(String token, String serverID, String minecraftChannelID, String staffMinecraftChannelID, String onlineChannelID, ParamRunnable messageCallback, ParamRunnable staffMessageCallback) {
        this.token = token;

        this.serverID = serverID;
        this.minecraftChannelID = minecraftChannelID;
        this.staffMinecraftChannelID = staffMinecraftChannelID;
        this.onlineChannelID = onlineChannelID;

        this.messageCallback = messageCallback;
        this.staffMessageCallback = staffMessageCallback;

        try {
            this.jda = JDABuilder.createDefault(this.token)
                    .enableIntents(GatewayIntent.GUILD_MESSAGES)
                    .disableCache(CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS, CacheFlag.EMOTE, CacheFlag.MEMBER_OVERRIDES, CacheFlag.ONLINE_STATUS, CacheFlag.ROLE_TAGS, CacheFlag.VOICE_STATE)
                    .setMemberCachePolicy(MemberCachePolicy.NONE)
                    .setAutoReconnect(true)
                    .setCompression(Compression.ZLIB)
                    .setActivity(Activity.playing("on play.evercraft.ga"))
                    .setStatus(OnlineStatus.ONLINE)
                    .addEventListeners(this)
                    .build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    public enum MessageType {
        Chat,
        StaffChat
    }

    public void sendMessage(MessageType type, String message) {
        if (type == MessageType.Chat) {
            this.jda.getGuildById(this.serverID).getTextChannelById(this.minecraftChannelID).sendMessage(message).queue();
        } else if (type == MessageType.StaffChat) {
            this.jda.getGuildById(this.serverID).getTextChannelById(this.staffMinecraftChannelID).sendMessage(message).queue();
        }
    }

    public void updateOnline(Integer online) {
        this.jda.getGuildById(this.serverID).getGuildChannelById(this.onlineChannelID).getManager().setName("Online: " + online).queue();
    }

    @Override
    public void onEvent(GenericEvent rawevent) {
        if (rawevent instanceof MessageReceivedEvent event) {
            if (event.getChannel().getId().equalsIgnoreCase(this.minecraftChannelID) && !event.getAuthor().getId().equalsIgnoreCase(this.jda.getSelfUser().getId())) {
                this.messageCallback.init(event.getAuthor().getAsTag(), event.getMessage().getContentDisplay());
                this.messageCallback.run();
            } else if (event.getChannel().getId().equalsIgnoreCase(this.staffMinecraftChannelID) && !event.getAuthor().getId().equalsIgnoreCase(this.jda.getSelfUser().getId())) {
                this.staffMessageCallback.init(event.getAuthor().getAsTag(), event.getMessage().getContentDisplay());
                this.staffMessageCallback.run();
            }
        }
    }
}