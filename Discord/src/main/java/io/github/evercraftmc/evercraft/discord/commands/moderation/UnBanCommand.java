package io.github.evercraftmc.evercraft.discord.commands.moderation;

import io.github.evercraftmc.evercraft.discord.DiscordBot;
import io.github.evercraftmc.evercraft.discord.args.ArgsParser;
import io.github.evercraftmc.evercraft.discord.args.ArgsValidator;
import io.github.evercraftmc.evercraft.discord.commands.Command;
import io.github.evercraftmc.evercraft.discord.data.types.data.ModCase;
import io.github.evercraftmc.evercraft.discord.data.types.data.ModType;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;

public class UnBanCommand extends Command {
    public UnBanCommand() {
        super("unban", new ArgsValidator.Arg[] { new ArgsValidator.Arg(ArgsValidator.ArgType.User, false), new ArgsValidator.Arg(ArgsValidator.ArgType.String, true) }, new Permission[] { Permission.BAN_MEMBERS });
    }

    @Override
    public void run(Message message) {
        try {
            message.getGuild().retrieveBan(ArgsParser.getUserArg(message, 1)).complete();

            message.getGuild().unban(ArgsParser.getUserArg(message, 1)).queue();

            if (ArgsParser.hasArg(message, 2)) {
                DiscordBot.Instance.sendEmbed(message.getTextChannel(), "Unbanned", ArgsParser.getUserArg(message, 1).getAsMention() + " was unbanned for " + ArgsParser.getStringArg(message, 2), message.getAuthor());
                DiscordBot.Instance.log(ArgsParser.getUserArg(message, 1).getAsMention() + " was unbanned by " + message.getMember().getAsMention() + " for " + ArgsParser.getStringArg(message, 2));
                DiscordBot.Instance.getData().history.add(new ModCase(ArgsParser.getUserArg(message, 1).getId(), message.getAuthor().getId(), ModType.UNBAN, ArgsParser.getStringArg(message, 2)));
                DiscordBot.Instance.getData().getParser().save();
            } else {
                DiscordBot.Instance.sendEmbed(message.getTextChannel(), "Unbanned", ArgsParser.getUserArg(message, 1).getAsMention() + " was unbanned", message.getAuthor());
                DiscordBot.Instance.log(ArgsParser.getUserArg(message, 1).getAsMention() + " was unbanned by " + message.getMember().getAsMention());
                DiscordBot.Instance.getData().history.add(new ModCase(ArgsParser.getUserArg(message, 1).getId(), message.getAuthor().getId(), ModType.UNBAN, null));
                DiscordBot.Instance.getData().getParser().save();
            }
        } catch (ErrorResponseException err) {
            DiscordBot.Instance.sendEmbed(message.getTextChannel(), "Error", ArgsParser.getUserArg(message, 1).getAsMention() + " is not banned", message.getAuthor());
        }
    }
}