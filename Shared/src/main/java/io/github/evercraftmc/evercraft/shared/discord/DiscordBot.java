package io.github.evercraftmc.evercraft.shared.discord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import javax.security.auth.login.LoginException;
import io.github.evercraftmc.evercraft.shared.util.Closable;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.entities.Activity.ActivityType;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class DiscordBot implements Closable, EventListener {
    private JDA jda;

    private String guild;

    private List<Consumer<GenericEvent>> listeners = new ArrayList<Consumer<GenericEvent>>();

    public DiscordBot(String token, String guild, GatewayIntent[] intents, CacheFlag[] caches, MemberCachePolicy memberCachePolicy, ActivityType statusType, String status) {
        try {
            this.jda = JDABuilder.createDefault(token)
                .setAutoReconnect(true)
                .disableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_MESSAGE_TYPING, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_WEBHOOKS, GatewayIntent.GUILD_BANS, GatewayIntent.GUILD_INVITES, GatewayIntent.GUILD_EMOJIS_AND_STICKERS, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.DIRECT_MESSAGE_REACTIONS, GatewayIntent.DIRECT_MESSAGE_TYPING)
                .enableIntents(Arrays.asList(intents))
                .disableCache(CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS, CacheFlag.EMOJI, CacheFlag.MEMBER_OVERRIDES, CacheFlag.ONLINE_STATUS, CacheFlag.ROLE_TAGS, CacheFlag.STICKER, CacheFlag.VOICE_STATE)
                .enableCache(Arrays.asList(caches))
                .setMemberCachePolicy(memberCachePolicy)
                .setCompression(Compression.ZLIB)
                .setStatus(OnlineStatus.ONLINE)
                .setActivity(Activity.of(statusType, status))
                .addEventListeners(this)
                .build();
        } catch (LoginException e) {
            e.printStackTrace();
        }

        this.guild = guild;
    }

    public void addListener(Consumer<GenericEvent> listener) {
        this.listeners.add(listener);
    }

    @Override
    public void onEvent(GenericEvent event) {
        for (Consumer<GenericEvent> listener : this.listeners) {
            listener.accept(event);
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