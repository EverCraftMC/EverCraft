package com.kale_ko.kalesutilities.shared.discord;

import com.kale_ko.kalesutilities.shared.util.ParamRunnable;
import javax.security.auth.login.LoginException;
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
    private String channelID;

    private ParamRunnable messageCallback;

    private JDA jda;

    public DiscordBot(String token, String serverID, String channelID, ParamRunnable messageCallback) {
        this.token = token;

        this.serverID = serverID;
        this.channelID = channelID;

        this.messageCallback = messageCallback;

        try {
            this.jda = JDABuilder.createDefault(this.token)
                    .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES)
                    .disableCache(CacheFlag.ACTIVITY, CacheFlag.EMOTE, CacheFlag.ONLINE_STATUS, CacheFlag.VOICE_STATE)
                    .setMemberCachePolicy(MemberCachePolicy.OWNER)
                    .setAutoReconnect(true)
                    .setCompression(Compression.ZLIB)
                    .setActivity(Activity.playing("on play.kalesmc.ga"))
                    .setStatus(OnlineStatus.ONLINE)
                    .addEventListeners(this)
                    .build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        this.jda.getGuildById(this.serverID).getTextChannelById(this.channelID).sendMessage(message).queue();
    }

    @Override
    public void onEvent(GenericEvent rawevent) {
        if (rawevent instanceof MessageReceivedEvent event) {
            this.messageCallback.init(event.getAuthor().getAsTag(), event.getMessage().getContentDisplay());
            this.messageCallback.run();
        }
    }
}