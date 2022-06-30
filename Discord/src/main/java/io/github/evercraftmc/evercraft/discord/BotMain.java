package io.github.evercraftmc.evercraft.discord;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
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
import io.github.evercraftmc.evercraft.discord.commands.rr.CreateReactionRoleCommand;
import io.github.evercraftmc.evercraft.discord.commands.rr.RemoveReactionRoleCommand;
import io.github.evercraftmc.evercraft.discord.commands.util.SudoCommand;
import io.github.evercraftmc.evercraft.discord.data.DataParser;
import io.github.evercraftmc.evercraft.discord.data.types.config.Config;
import io.github.evercraftmc.evercraft.discord.data.types.data.Data;
import io.github.evercraftmc.evercraft.discord.data.types.data.ReactionRole;
import io.github.evercraftmc.evercraft.discord.data.types.data.Warning;
import io.github.evercraftmc.evercraft.shared.discord.DiscordBot;
import io.github.evercraftmc.evercraft.shared.util.ModerationUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.EmbedType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageActivity;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.MessageReference;
import net.dv8tion.jda.api.entities.MessageType;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.Message.Attachment;
import net.dv8tion.jda.api.entities.Message.Interaction;
import net.dv8tion.jda.api.entities.sticker.StickerItem;
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
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandEditAction;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.internal.JDAImpl;
import net.dv8tion.jda.internal.entities.GuildImpl;
import net.dv8tion.jda.internal.entities.MessageMentionsImpl;
import net.dv8tion.jda.internal.entities.ReceivedMessage;

public class BotMain implements EventListener {
    public static BotMain Instance;

    private Config config;
    private Data data;

    public List<Command> commands;

    private DiscordBot bot;
    private Map<String, Message> messageCache = new HashMap<String, Message>();
    private Map<Member, List<Message>> latestMessages = new HashMap<Member, List<Message>>();

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
        commands.add(new SudoCommand());
        commands.add(new CreateReactionRoleCommand());
        commands.add(new RemoveReactionRoleCommand());

        this.bot = new DiscordBot(this.getConfig().getToken(), config.getGuildId(), new GatewayIntent[] { GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_BANS, GatewayIntent.GUILD_EMOJIS_AND_STICKERS }, new CacheFlag[] { CacheFlag.MEMBER_OVERRIDES, CacheFlag.EMOJI }, MemberCachePolicy.ONLINE, config.getStatusType(), config.getStatus());

        this.bot.addListener((GenericEvent rawevent) -> {
            if (rawevent instanceof ReadyEvent event) {
                System.out.println("Finished starting bot..");
            }
        });

        this.getJDA().addEventListener(this);
    }

    public DiscordBot getBot() {
        return this.bot;
    }

    public JDA getJDA() {
        return this.getBot().getJDA();
    }

    public Config getConfig() {
        return this.config;
    }

    public Data getData() {
        return this.data;
    }

    public MessageEmbed buildEmbed(String title, String description) {
        return new EmbedBuilder().setTitle(title).setDescription(description).setAuthor(this.getBot().getGuild().getSelfMember().getNickname() != null ? this.getBot().getGuild().getSelfMember().getNickname() : this.getJDA().getSelfUser().getName(), null, this.getJDA().getSelfUser().getAvatarUrl()).setFooter((this.getBot().getGuild().getSelfMember().getNickname() != null ? this.getBot().getGuild().getSelfMember().getNickname() : this.getJDA().getSelfUser().getName())).setTimestamp(new Date().toInstant()).setColor(config.getColor().toColor()).build();
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
        this.getJDA().getTextChannelById(config.getLogChannel()).sendMessageEmbeds(buildEmbed("Log", message)).queue();
    }

    @Override
    public void onEvent(GenericEvent rawevent) {
        if (rawevent instanceof ReadyEvent event) {
            System.out.println("Registering commands..");

            List<net.dv8tion.jda.api.interactions.commands.Command> fetchedCommands = this.getJDA().getGuildById(getConfig().getGuildId()).retrieveCommands().complete();

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
                this.getJDA().getGuildById(getConfig().getGuildId()).upsertCommand(command.getName(), BotMain.Instance.getConfig().getPrefix() + command.getName()).queue((net.dv8tion.jda.api.interactions.commands.Command fetchedCommand) -> {
                    CommandEditAction editor = fetchedCommand.editCommand();
                    for (ArgsValidator.Arg arg : command.getArgs()) {
                        editor.addOption(arg.toOption(), arg.type().toString().toLowerCase(), arg.type().toString(), !arg.optional());
                    }
                    editor.queue();
                });
            }

            System.out.println("Loading message chache..");

            for (TextChannel channel : this.getBot().getGuild().getTextChannels()) {
                channel.getHistory().retrieveFuture(100).queue((List<Message> messages) -> {
                    for (Message message : messages) {
                        messageCache.put(message.getId(), message);
                    }
                });

                channel.getHistory().retrievePast(100).queue((List<Message> messages) -> {
                    for (Message message : messages) {
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

                StringBuilder message = new StringBuilder(event.getMessage().getContentRaw());

                for (MessageEmbed embed : event.getMessage().getEmbeds()) {
                    if (embed.getType() == EmbedType.LINK) {
                        message.append(" " + embed.getTitle() + " " + embed.getDescription() + " " + embed.getUrl().replace("-", " ").replace("_", " "));
                    } else if (embed.getType() == EmbedType.IMAGE || embed.getType() == EmbedType.VIDEO) {
                        message.append(" " + embed.getUrl().replace("-", " ").replace("_", " "));
                    }
                }

                for (Attachment attachment : event.getMessage().getAttachments()) {
                    message.append(" " + attachment.getUrl().replace("-", " ").replace("_", " ") + " " + attachment.getFileName() + " " + attachment.getDescription());
                }

                if (ModerationUtil.isSuperInappropriateString(message.toString().trim())) {
                    if (event.getMember().getRoles().isEmpty() || event.getMember().getRoles().get(0).getPosition() <= event.getGuild().getSelfMember().getRoles().get(0).getPosition()) {
                        String command = getConfig().getPrefix() + "mute " + event.getMember().getAsMention() + " 1h No, just no, get the fuck out";
                        Long id = UUID.randomUUID().getMostSignificantBits();
                        Message fakemessage = new ReceivedMessage(id, event.getChannel(), MessageType.DEFAULT, new MessageReference(id, event.getChannel().getIdLong(), event.getGuild().getIdLong(), new MessageBuilder(command.trim()).build(), this.getJDA()), false, false, false, command.trim(), id + "", getJDA().getSelfUser(), event.getGuild().getSelfMember(), new MessageActivity(null, null, null), OffsetDateTime.now(), new MessageMentionsImpl((JDAImpl) event.getJDA(), (GuildImpl) event.getGuild(), command.trim(), false, DataArray.empty(), DataArray.empty()), new ArrayList<MessageReaction>(), new ArrayList<Attachment>(), new ArrayList<MessageEmbed>(), new ArrayList<StickerItem>(), new ArrayList<ActionRow>(), 0, null, null);
                        new MuteCommand().run(fakemessage);

                        event.getMessage().delete().queue();
                    }
                } else if (ModerationUtil.isInappropriateString(message.toString().trim())) {
                    String command = getConfig().getPrefix() + "warn " + event.getMember().getAsMention() + " Inappropriate language";
                    Long id = UUID.randomUUID().getMostSignificantBits();
                    Message fakemessage = new ReceivedMessage(id, event.getChannel(), MessageType.DEFAULT, new MessageReference(id, event.getChannel().getIdLong(), event.getGuild().getIdLong(), new MessageBuilder(command.trim()).build(), this.getJDA()), false, false, false, command.trim(), id + "", getJDA().getSelfUser(), event.getGuild().getSelfMember(), new MessageActivity(null, null, null), OffsetDateTime.now(), new MessageMentionsImpl((JDAImpl) event.getJDA(), (GuildImpl) event.getGuild(), command.trim(), false, DataArray.empty(), DataArray.empty()), new ArrayList<MessageReaction>(), new ArrayList<Attachment>(), new ArrayList<MessageEmbed>(), new ArrayList<StickerItem>(), new ArrayList<ActionRow>(), 0, null, null);
                    new WarnCommand().run(fakemessage);

                    Integer warningCount = 0;
                    for (Warning warning : BotMain.Instance.getData().warnings.get(event.getAuthor().getId())) {
                        if (Instant.now().getEpochSecond() - warning.getTimestamp().getEpochSecond() < 300 && warning.getReason().equals("Inappropriate language")) {
                            warningCount++;
                        }
                    }

                    if (event.getMember().getRoles().isEmpty() || event.getMember().getRoles().get(0).getPosition() <= event.getGuild().getSelfMember().getRoles().get(0).getPosition()) {
                        if (warningCount > 5) {
                            String command2 = getConfig().getPrefix() + "mute " + event.getMember().getAsMention() + " " + (5 * (warningCount - 5)) + "m Inappropriate language";
                            Long id2 = UUID.randomUUID().getMostSignificantBits();
                            Message fakemessage2 = new ReceivedMessage(id2, event.getChannel(), MessageType.DEFAULT, new MessageReference(id2, event.getChannel().getIdLong(), event.getGuild().getIdLong(), new MessageBuilder(command2.trim()).build(), this.getJDA()), false, false, false, command2.trim(), id2 + "", getJDA().getSelfUser(), event.getGuild().getSelfMember(), new MessageActivity(null, null, null), OffsetDateTime.now(), new MessageMentionsImpl((JDAImpl) event.getJDA(), (GuildImpl) event.getGuild(), command2.trim(), false, DataArray.empty(), DataArray.empty()), new ArrayList<MessageReaction>(), new ArrayList<Attachment>(), new ArrayList<MessageEmbed>(), new ArrayList<StickerItem>(), new ArrayList<ActionRow>(), 0, null, null);
                            new MuteCommand().run(fakemessage2);
                        }

                        event.getMessage().delete().queue();

                        return;
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
                        Message message = new ReceivedMessage(event.getIdLong(), event.getChannel(), MessageType.DEFAULT, new MessageReference(event.getIdLong(), event.getChannel().getIdLong(), event.getGuild().getIdLong(), new MessageBuilder(content.toString().trim()).build(), this.getJDA()), false, false, false, content.toString().trim(), event.getId(), event.getUser(), event.getMember(), new MessageActivity(null, null, null), OffsetDateTime.now(), new MessageMentionsImpl((JDAImpl) event.getJDA(), (GuildImpl) event.getGuild(), content.toString().trim(), false, DataArray.empty(), DataArray.empty()), new ArrayList<MessageReaction>(), new ArrayList<Attachment>(), new ArrayList<MessageEmbed>(), new ArrayList<StickerItem>(), new ArrayList<ActionRow>(), 0, new Interaction(event.getIdLong(), event.getTypeRaw(), event.getName(), event.getUser(), event.getMember()), null);

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
            sendEmbed(event.getGuild().getTextChannelById(config.getWelcomeChannel()), "Bye", "Sad to see you go " + event.getUser().getAsTag());

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
        } else if (rawevent instanceof MessageReactionAddEvent event) {
            for (ReactionRole reactionRole : BotMain.Instance.getData().reactions) {
                if (reactionRole.getChannel().equals(event.getChannel().getId()) && reactionRole.getMessage().equals(event.getMessageId()) && reactionRole.getEmoji().equals(event.getEmoji().getAsReactionCode())) {
                    event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById(reactionRole.getRole())).queue();
                }
            }
        } else if (rawevent instanceof MessageReactionRemoveEvent event) {
            for (ReactionRole reactionRole : BotMain.Instance.getData().reactions) {
                if (reactionRole.getChannel().equals(event.getChannel().getId()) && reactionRole.getMessage().equals(event.getMessageId()) && reactionRole.getEmoji().equals(event.getEmoji().getAsReactionCode())) {
                    event.getGuild().removeRoleFromMember(event.getMember(), event.getGuild().getRoleById(reactionRole.getRole())).queue();
                }
            }
        }
    }

    public static void main(String[] args) {
        new BotMain("config.json", "data.json");
    }
}