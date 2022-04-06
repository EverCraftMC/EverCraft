package io.github.evercraftmc.evercraft.discord;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.security.auth.login.LoginException;
import gnu.trove.set.hash.TLongHashSet;
import io.github.evercraftmc.evercraft.discord.args.ArgsParser;
import io.github.evercraftmc.evercraft.discord.args.ArgsValidator;
import io.github.evercraftmc.evercraft.discord.commands.Command;
import io.github.evercraftmc.evercraft.discord.commands.info.HelpCommand;
import io.github.evercraftmc.evercraft.discord.commands.info.ServerInfoCommand;
import io.github.evercraftmc.evercraft.discord.commands.info.WhoIsCommand;
import io.github.evercraftmc.evercraft.discord.commands.logs.HistoryCommand;
import io.github.evercraftmc.evercraft.discord.commands.logs.WarnsCommand;
import io.github.evercraftmc.evercraft.discord.commands.moderation.BanCommand;
import io.github.evercraftmc.evercraft.discord.commands.moderation.ClearCommand;
import io.github.evercraftmc.evercraft.discord.commands.moderation.ClearWarnsCommand;
import io.github.evercraftmc.evercraft.discord.commands.moderation.KickCommand;
import io.github.evercraftmc.evercraft.discord.commands.moderation.LockChannelCommand;
import io.github.evercraftmc.evercraft.discord.commands.moderation.MuteCommand;
import io.github.evercraftmc.evercraft.discord.commands.moderation.NickCommand;
import io.github.evercraftmc.evercraft.discord.commands.moderation.UnBanCommand;
import io.github.evercraftmc.evercraft.discord.commands.moderation.UnLockChannelCommand;
import io.github.evercraftmc.evercraft.discord.commands.moderation.UnMuteCommand;
import io.github.evercraftmc.evercraft.discord.commands.moderation.WarnCommand;
import io.github.evercraftmc.evercraft.discord.data.DataParser;
import io.github.evercraftmc.evercraft.discord.data.types.config.Config;
import io.github.evercraftmc.evercraft.discord.data.types.data.Data;
import io.github.evercraftmc.evercraft.shared.util.ModerationUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageActivity;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.MessageReference;
import net.dv8tion.jda.api.entities.MessageSticker;
import net.dv8tion.jda.api.entities.MessageType;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.Message.Attachment;
import net.dv8tion.jda.api.entities.Message.Interaction;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.guild.GuildUnbanEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageBulkDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandEditAction;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.dv8tion.jda.internal.entities.ReceivedMessage;

public class BotMain implements EventListener {
    public static BotMain Instance;

    private Config config;
    private Data data;

    public List<Command> commands;

    private JDA jda;
    private Map<String, Message> messageCache = new HashMap<String, Message>();
    private Map<Member, List<Message>> latestMessages = new HashMap<Member, List<Message>>();

    public Map<Member, Integer> warnings = new HashMap<Member, Integer>();

    public BotMain(String configFileName, String dataFileName) {
        BotMain.Instance = this;

        System.out.println("Loading config..");

        this.config = new DataParser<Config>(Config.class, configFileName).getData();
        this.data = new DataParser<Data>(Data.class, dataFileName).getData();

        System.out.println("Finished loading config");

        System.out.println("Starting bot..");

        this.commands = new ArrayList<Command>();

        commands.add(new HelpCommand());
        commands.add(new WhoIsCommand());
        commands.add(new ServerInfoCommand());
        commands.add(new HistoryCommand());
        commands.add(new WarnCommand());
        commands.add(new ClearWarnsCommand());
        commands.add(new WarnsCommand());
        commands.add(new KickCommand());
        commands.add(new MuteCommand());
        commands.add(new UnMuteCommand());
        commands.add(new BanCommand());
        commands.add(new UnBanCommand());
        commands.add(new LockChannelCommand());
        commands.add(new UnLockChannelCommand());
        commands.add(new ClearCommand());
        commands.add(new NickCommand());

        try {
            this.jda = JDABuilder.createDefault(this.getConfig().getToken())
                .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_BANS)
                .disableCache(CacheFlag.VOICE_STATE)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .setAutoReconnect(true)
                .setCompression(Compression.ZLIB)
                .setActivity(Activity.of(config.getStatusType(), config.getStatus()))
                .setStatus(OnlineStatus.ONLINE)
                .addEventListeners(this)
                .build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    public JDA getJDA() {
        return this.jda;
    }

    public Config getConfig() {
        return this.config;
    }

    public Data getData() {
        return this.data;
    }

    public MessageEmbed buildEmbed(String title, String description) {
        return new EmbedBuilder().setTitle(title).setDescription(description).setAuthor(jda.getGuildById(config.getGuildId()).getSelfMember().getNickname() != null ? jda.getGuildById(config.getGuildId()).getSelfMember().getNickname() : jda.getSelfUser().getName(), null, jda.getSelfUser().getAvatarUrl()).setFooter((jda.getGuildById(config.getGuildId()).getSelfMember().getNickname() != null ? jda.getGuildById(config.getGuildId()).getSelfMember().getNickname() : jda.getSelfUser().getName())).setTimestamp(new Date().toInstant()).setColor(config.getColor().toColor()).build();
    }

    public void sendEmbed(TextChannel channel, String title, String message) {
        channel.sendMessageEmbeds(buildEmbed(title, message)).queue();
    }

    public void sendEmbed(TextChannel channel, String title, String message, User user) {
        sendEmbed(channel, title, message);
    }

    public void sendMessage(TextChannel channel, String message) {
        channel.sendMessage(message).queue();
    }

    public void sendMessage(TextChannel channel, String message, User user) {
        sendMessage(channel, user.getAsMention() + "\n" + message);
    }

    public void log(String message) {
        BotMain.Instance.jda.getTextChannelById(config.getLogChannel()).sendMessageEmbeds(buildEmbed("Log", message)).queue();
    }

    @Override
    public void onEvent(GenericEvent rawevent) {
        if (rawevent instanceof ReadyEvent event) {
            System.out.println("Finished starting bot..");

            System.out.println("Registering commands..");

            List<net.dv8tion.jda.api.interactions.commands.Command> fetchedCommands = jda.getGuildById(getConfig().getGuildId()).retrieveCommands().complete();

            for (net.dv8tion.jda.api.interactions.commands.Command command : fetchedCommands) {
                Command regcommand = null;

                for (Command regedcommand : commands) {
                    if (regedcommand.getName().equals(command.getName())) {
                        regcommand = regedcommand;
                    }
                }

                if (regcommand == null) {
                    command.delete().queue();
                }
            }

            for (Command command : commands) {
                jda.getGuildById(getConfig().getGuildId()).upsertCommand(command.getName(), BotMain.Instance.getConfig().getPrefix() + command.getName()).queue((net.dv8tion.jda.api.interactions.commands.Command fetchedCommand) -> {
                    CommandEditAction editor = fetchedCommand.editCommand();
                    for (ArgsValidator.Arg arg : command.getArgs()) {
                        editor.addOption(arg.toOption(), arg.type().toString().toLowerCase(), arg.type().toString(), !arg.optional());
                    }
                    editor.queue();
                });
            }

            System.out.println("Loading message chache..");

            for (TextChannel channel : jda.getGuildById(config.getGuildId()).getTextChannels()) {
                channel.getHistoryBefore(channel.getLatestMessageId(), 100).queue((MessageHistory history) -> {
                    for (Message message : history.getRetrievedHistory()) {
                        messageCache.put(message.getId(), message);
                    }
                });
            }
        } else if (rawevent instanceof MessageReceivedEvent event) {
            messageCache.put(event.getMessageId(), event.getMessage());

            if (!event.getAuthor().isBot() && !event.getMessage().getContentRaw().trim().startsWith(config.getPrefix())) {
                if (!latestMessages.containsKey(event.getMember())) {
                    latestMessages.put(event.getMember(), new ArrayList<Message>());
                }
                List<Message> messages = latestMessages.get(event.getMember());
                messages.add(event.getMessage());

                for (Message message : messages.toArray(new Message[] {})) {
                    if (message.getTimeCreated().toEpochSecond() + 5 < Instant.now().getEpochSecond()) {
                        messages.remove(message);
                    }
                }

                if (messages.size() > 5) {
                    for (Message message : messages.toArray(new Message[] {})) {
                        message.delete().queue();

                        messages.remove(message);
                    }

                    sendEmbed(event.getTextChannel(), ":(", "Please don't spam");

                    return;
                }

                if (event.getMessage().getContentRaw().contains("nigger")) {
                    event.getMember().timeoutFor(Duration.ofMinutes(15)).queue();

                    BotMain.Instance.sendEmbed(event.getTextChannel(), "Mute", event.getMember().getAsMention() + " was muted for 15m Saying the freaking n word");
                    BotMain.Instance.log(event.getMember().getAsMention() + " was muted by AutoMod for 15m Saying the freaking n word");
                } else {
                    for (String word : ArgsParser.getRawArgs(event.getMessage())) {
                        if (ModerationUtil.isInappropriateWord(word)) {
                            if (warnings.containsKey(event.getMember())) {
                                Integer value = warnings.get(event.getMember()) + 1;
                                warnings.remove(event.getMember());
                                warnings.put(event.getMember(), value);
                            } else {
                                warnings.put(event.getMember(), 1);
                            }

                            if (event.getMember().getRoles().isEmpty() || event.getMember().getRoles().get(0).getPosition() <= event.getGuild().getSelfMember().getRoles().get(0).getPosition()) {
                                if (warnings.get(event.getMember()) == 1) {
                                    sendEmbed(event.getTextChannel(), ":(", "**Hey don't say that :(**");
                                } else if (warnings.get(event.getMember()) == 2) {
                                    sendEmbed(event.getTextChannel(), ":(", "**Seriously don't say that**");
                                } else if (warnings.get(event.getMember()) == 3) {
                                    sendEmbed(event.getTextChannel(), ":(", "**This is your last warning, do not say that**");
                                } else if (warnings.get(event.getMember()) >= 4) {
                                    event.getMember().timeoutFor(Duration.ofMinutes(5 * (warnings.get(event.getMember()) - 3))).queue();

                                    BotMain.Instance.sendEmbed(event.getTextChannel(), "Mute", event.getMember().getAsMention() + " was muted for " + (5 * (warnings.get(event.getMember()) - 3)) + "m Inappropriate language");
                                    BotMain.Instance.log(event.getMember().getAsMention() + " was muted by AutoMod for " + (5 * (warnings.get(event.getMember()) - 3)) + "m Inappropriate language");
                                }

                                event.getMessage().delete().queue();

                                return;
                            }
                        }
                    }
                }
            }

            Boolean isCommand = false;

            if (event.getMessage().getContentRaw().trim().startsWith(config.getPrefix()) && event.getMessage().getContentRaw().trim().length() > 1 && !event.getAuthor().isBot()) {
                for (Command command : commands) {
                    if (ArgsParser.getWordArg(event.getMessage(), 0).equalsIgnoreCase(config.getPrefix() + command.getName())) {
                        isCommand = true;

                        Boolean hasPerms = true;

                        for (Permission permission : command.getPermissions()) {
                            if (!event.getMember().getPermissions().contains(permission)) {
                                hasPerms = false;
                            }
                        }

                        if (hasPerms) {
                            if (ArgsValidator.validateArgs(event.getMessage(), command.getArgs())) {
                                command.run(event.getMessage());
                            }
                        } else {
                            sendEmbed(event.getTextChannel(), "Error", "You can't do that", event.getAuthor());
                        }
                    }
                }

                if (!isCommand) {
                    sendEmbed(event.getTextChannel(), "Error", "That is not a command", event.getAuthor());
                }

                if (!event.getChannel().getId().equals(config.getBotCommandsChannel())) {
                    event.getMessage().delete().queue();
                }
            } else if (event.getMember() == event.getGuild().getSelfMember() && !event.getChannel().getId().equals(config.getLogChannel()) && !event.getChannel().getId().equals(config.getWelcomeChannel()) && !event.getChannel().getId().equals(config.getBotCommandsChannel())) {
                event.getMessage().delete().queueAfter(10, TimeUnit.SECONDS);
            }
        } else if (rawevent instanceof SlashCommandInteractionEvent event) {
            event.deferReply().queue();

            for (Command command : commands) {
                if (event.getName().equals(command.getName())) {
                    Boolean hasPerms = true;

                    for (Permission permission : command.getPermissions()) {
                        if (!event.getMember().getPermissions().contains(permission)) {
                            hasPerms = false;
                        }
                    }

                    if (hasPerms) {
                        StringBuilder content = new StringBuilder(getConfig().getPrefix() + event.getName());
                        for (OptionMapping option : event.getOptions()) {
                            content.append(" " + option.getAsString());
                        }
                        Message message = new ReceivedMessage(event.getIdLong(), event.getChannel(), MessageType.SLASH_COMMAND, new MessageReference(event.getIdLong(), event.getChannel().getIdLong(), event.getGuild().getIdLong(), new MessageBuilder(content.toString().trim()).build(), jda), false, false, new TLongHashSet(), new TLongHashSet(), false, false, content.toString().trim(), event.getId(), event.getUser(), event.getMember(), new MessageActivity(null, null, null), OffsetDateTime.now(), new ArrayList<MessageReaction>(), new ArrayList<Attachment>(), new ArrayList<MessageEmbed>(), new ArrayList<MessageSticker>(), new ArrayList<ActionRow>(), 0, new Interaction(event.getIdLong(), event.getTypeRaw(), event.getName(), event.getUser(), event.getMember()));

                        if (ArgsValidator.validateArgs(message, command.getArgs())) {
                            command.run(message);

                            event.getHook().sendMessage("Ran!").queue();
                        } else {
                            event.getHook().sendMessage("Error").queue();
                        }
                    } else {
                        event.getHook().sendMessage(new MessageBuilder(buildEmbed("Error", "You can't do that")).build()).queue();
                    }
                }
            }
        } else if (rawevent instanceof GuildMemberJoinEvent event) {
            sendEmbed(event.getGuild().getTextChannelById(config.getWelcomeChannel()), "Welcome", "Welcome " + event.getMember().getAsMention() + " to the server!");

            log("User " + event.getUser().getAsMention() + " joined the server");
        } else if (rawevent instanceof GuildMemberRemoveEvent event) {
            sendEmbed(event.getGuild().getTextChannelById(config.getWelcomeChannel()), "Bye", "Sad to see you go " + event.getMember().getAsMention());

            log("User " + event.getUser().getAsMention() + " left the server");
        } else if (rawevent instanceof GuildBanEvent event) {
            if (event.getGuild().retrieveBan(event.getUser()).complete().getReason() == null) {
                BotMain.Instance.log(event.getUser().getAsMention() + " was banned");
            } else {
                BotMain.Instance.log(event.getUser().getAsMention() + " was banned for " + event.getGuild().retrieveBan(event.getUser()).complete().getReason());
            }
        } else if (rawevent instanceof GuildUnbanEvent event) {
            BotMain.Instance.log(event.getUser().getAsMention() + " was unbanned");
        } else if (rawevent instanceof GuildMemberRoleAddEvent event) {
            for (Role role : event.getRoles()) {
                log("User " + event.getUser().getAsMention() + " was added to " + role.getAsMention());
            }
        } else if (rawevent instanceof GuildMemberRoleRemoveEvent event) {
            for (Role role : event.getRoles()) {
                log("User " + event.getUser().getAsMention() + " was removed from " + role.getAsMention());
            }
        } else if (rawevent instanceof MessageDeleteEvent event) {
            if (messageCache.containsKey(event.getMessageId())) {
                Message message = messageCache.get(event.getMessageId());

                if (!message.getContentRaw().startsWith(config.getPrefix()) && message.getAuthor() != message.getJDA().getSelfUser()) {
                    log("Message by " + message.getAuthor().getAsMention() + " \"" + message.getContentRaw() + "\" deleted in " + message.getTextChannel().getAsMention());
                }
            } else {
                log("An uncached message (" + event.getMessageId() + ") was deleted in " + event.getChannel().getAsMention());
            }
        } else if (rawevent instanceof MessageBulkDeleteEvent event) {
            for (String messageId : event.getMessageIds()) {
                if (messageCache.containsKey(messageId)) {
                    Message message = messageCache.get(messageId);

                    if (!message.getContentRaw().startsWith(config.getPrefix()) && message.getAuthor() != message.getJDA().getSelfUser()) {
                        log("Message by " + message.getAuthor().getAsMention() + " \"" + message.getContentRaw() + "\" deleted in " + message.getTextChannel().getAsMention());
                    }
                } else {
                    log("An uncached message (" + messageId + ") was deleted in " + event.getChannel().getAsMention());
                }
            }
        }
    }

    public static void main(String[] args) {
        new BotMain("config.json", "data.json");
    }
}