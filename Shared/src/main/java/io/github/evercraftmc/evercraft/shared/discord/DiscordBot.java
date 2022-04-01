package io.github.evercraftmc.evercraft.shared.discord;

import java.util.function.Consumer;
import javax.security.auth.login.LoginException;
import io.github.evercraftmc.evercraft.shared.util.Closable;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.entities.Activity.ActivityType;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class DiscordBot implements Closable, EventListener {
    private JDA jda;

    private String guild;

    private Consumer<Message> callback = null;

    public DiscordBot(String token, String guild, ActivityType statusType, String status) {
        try {
            this.jda = JDABuilder.create(token, GatewayIntent.GUILD_MESSAGES)
                .setAutoReconnect(true)
                .disableCache(CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS, CacheFlag.EMOTE, CacheFlag.MEMBER_OVERRIDES, CacheFlag.ONLINE_STATUS, CacheFlag.ROLE_TAGS, CacheFlag.VOICE_STATE)
                .setMemberCachePolicy(MemberCachePolicy.NONE)
                .setCompression(Compression.ZLIB)
                .setActivity(Activity.of(statusType, status))
                .addEventListeners(this)
                .build();
        } catch (LoginException e) {
            e.printStackTrace();
        }

        this.guild = guild;
    }

    public void setMessageCallback(Consumer<Message> callback) {
        this.callback = callback;
    }

    @Override
    public void onEvent(GenericEvent rawevent) {
        if (rawevent instanceof MessageReceivedEvent event) {
            if (event.getAuthor() != this.getJDA().getSelfUser() && this.callback != null) {
                this.callback.accept(event.getMessage());
            }
        }
    }

    public JDA getJDA() {
        return this.jda;
    }

    public Guild getGuild() {
        return this.jda.getGuildById(guild);
    }

    public void close() {
        this.jda.shutdown();
    }
}