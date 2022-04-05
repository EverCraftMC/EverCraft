package io.github.evercraftmc.evercraft.discord.commands.moderation;

import io.github.evercraftmc.evercraft.discord.DiscordBot;
import io.github.evercraftmc.evercraft.discord.args.ArgsParser;
import io.github.evercraftmc.evercraft.discord.args.ArgsValidator;
import io.github.evercraftmc.evercraft.discord.commands.Command;
import io.github.evercraftmc.evercraft.discord.data.types.data.ModCase;
import io.github.evercraftmc.evercraft.discord.data.types.data.ModType;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;

public class UnLockChannelCommand extends Command {
    public UnLockChannelCommand() {
        super("unlockchannel", new ArgsValidator.Arg[] { new ArgsValidator.Arg(ArgsValidator.ArgType.TextChannel, false), new ArgsValidator.Arg(ArgsValidator.ArgType.String, true) }, new Permission[] { Permission.MANAGE_CHANNEL });
    }

    @Override
    public void run(Message message) {
        ArgsParser.getTextChannelArg(message, 1).getPermissionOverride(message.getGuild().getPublicRole()).getManager().clear(Permission.MESSAGE_SEND, Permission.MESSAGE_SEND_IN_THREADS, Permission.MESSAGE_TTS, Permission.CREATE_PUBLIC_THREADS, Permission.CREATE_PRIVATE_THREADS, Permission.USE_APPLICATION_COMMANDS, Permission.MESSAGE_ADD_REACTION).queue();

        if (ArgsParser.hasArg(message, 2)) {
            DiscordBot.Instance.sendEmbed(message.getTextChannel(), "Lock Channel", ArgsParser.getTextChannelArg(message, 1).getAsMention() + " has been unlocked for " + ArgsParser.getStringArg(message, 2), message.getAuthor());
            DiscordBot.Instance.log(ArgsParser.getTextChannelArg(message, 1).getAsMention() + " was unlocked by " + message.getMember().getAsMention() + " for " + ArgsParser.getStringArg(message, 2));
            DiscordBot.Instance.getData().history.add(new ModCase(null, message.getAuthor().getId(), ModType.UNLOCKCHANNEL, ArgsParser.getStringArg(message, 2)));
            DiscordBot.Instance.getData().getParser().save();
        } else {
            DiscordBot.Instance.sendEmbed(message.getTextChannel(), "Lock Channel", ArgsParser.getTextChannelArg(message, 1).getAsMention() + " has been unlocked", message.getAuthor());
            DiscordBot.Instance.log(ArgsParser.getTextChannelArg(message, 1).getAsMention() + " was unlocked by " + message.getMember().getAsMention());
            DiscordBot.Instance.getData().history.add(new ModCase(null, message.getAuthor().getId(), ModType.UNLOCKCHANNEL, null));
            DiscordBot.Instance.getData().getParser().save();
        }
    }
}