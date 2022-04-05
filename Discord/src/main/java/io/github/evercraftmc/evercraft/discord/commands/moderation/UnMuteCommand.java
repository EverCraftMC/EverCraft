package io.github.evercraftmc.evercraft.discord.commands.moderation;

import io.github.evercraftmc.evercraft.discord.DiscordBot;
import io.github.evercraftmc.evercraft.discord.args.ArgsParser;
import io.github.evercraftmc.evercraft.discord.args.ArgsValidator;
import io.github.evercraftmc.evercraft.discord.commands.Command;
import io.github.evercraftmc.evercraft.discord.data.types.data.ModCase;
import io.github.evercraftmc.evercraft.discord.data.types.data.ModType;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;

public class UnMuteCommand extends Command {
    public UnMuteCommand() {
        super("unmute", new ArgsValidator.Arg[] { new ArgsValidator.Arg(ArgsValidator.ArgType.Member, false), new ArgsValidator.Arg(ArgsValidator.ArgType.String, true) }, new Permission[] { Permission.MODERATE_MEMBERS });
    }

    @Override
    public void run(Message message) {
        if (ArgsParser.getMemberArg(message, 1).isTimedOut()) {
            ArgsParser.getMemberArg(message, 1).removeTimeout().queue();

            if (ArgsParser.hasArg(message, 2)) {
                DiscordBot.Instance.sendEmbed(message.getTextChannel(), "Unmute", ArgsParser.getUserArg(message, 1).getAsMention() + " was unmuted for " + ArgsParser.getStringArg(message, 2), message.getAuthor());
                DiscordBot.Instance.log(ArgsParser.getUserArg(message, 1).getAsMention() + " was unmuted by " + message.getMember().getAsMention() + " for " + ArgsParser.getStringArg(message, 2));
                DiscordBot.Instance.getData().history.add(new ModCase(ArgsParser.getUserArg(message, 1).getId(), message.getAuthor().getId(), ModType.UNMUTE, ArgsParser.getStringArg(message, 2)));
                DiscordBot.Instance.getData().getParser().save();
            } else {
                DiscordBot.Instance.sendEmbed(message.getTextChannel(), "Unmute", ArgsParser.getUserArg(message, 1).getAsMention() + " was unmuted", message.getAuthor());
                DiscordBot.Instance.log(ArgsParser.getUserArg(message, 1).getAsMention() + " was unmuted by " + message.getMember().getAsMention());
                DiscordBot.Instance.getData().history.add(new ModCase(ArgsParser.getUserArg(message, 1).getId(), message.getAuthor().getId(), ModType.UNMUTE, null));
                DiscordBot.Instance.getData().getParser().save();
            }
        } else {
            DiscordBot.Instance.sendEmbed(message.getTextChannel(), "Error", ArgsParser.getUserArg(message, 1).getAsMention() + " is not muted", message.getAuthor());
        }
    }
}