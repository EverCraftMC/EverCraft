package io.github.evercraftmc.evercraft.discord.commands.moderation;

import java.util.Arrays;
import io.github.evercraftmc.evercraft.discord.BotMain;
import io.github.evercraftmc.evercraft.discord.args.ArgsParser;
import io.github.evercraftmc.evercraft.discord.args.ArgsValidator;
import io.github.evercraftmc.evercraft.discord.commands.Command;
import io.github.evercraftmc.evercraft.discord.data.types.data.ModCase;
import io.github.evercraftmc.evercraft.discord.data.types.data.ModType;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;

public class UnMuteCommand extends Command {
    public UnMuteCommand() {
        super("unmute", Arrays.asList(), Arrays.asList(new ArgsValidator.Arg(ArgsValidator.ArgType.Member, false), new ArgsValidator.Arg(ArgsValidator.ArgType.String, true)), Arrays.asList(Permission.MODERATE_MEMBERS));
    }

    @Override
    public void run(Message message) {
        if (ArgsParser.getMemberArg(message, 1).isTimedOut()) {
            ArgsParser.getMemberArg(message, 1).removeTimeout().queue();

            if (ArgsParser.hasArg(message, 2)) {
                BotMain.Instance.sendEmbed(message.getTextChannel(), "Unmute", ArgsParser.getUserArg(message, 1).getAsMention() + " was unmuted for " + ArgsParser.getStringArg(message, 2), message.getAuthor());
                BotMain.Instance.log(ArgsParser.getUserArg(message, 1).getAsMention() + " was unmuted by " + message.getMember().getAsMention() + " for " + ArgsParser.getStringArg(message, 2));
                BotMain.Instance.getData().history.add(new ModCase(ArgsParser.getUserArg(message, 1).getId(), message.getAuthor().getId(), ModType.UNMUTE, ArgsParser.getStringArg(message, 2)));
                BotMain.Instance.getData().getParser().save();
            } else {
                BotMain.Instance.sendEmbed(message.getTextChannel(), "Unmute", ArgsParser.getUserArg(message, 1).getAsMention() + " was unmuted", message.getAuthor());
                BotMain.Instance.log(ArgsParser.getUserArg(message, 1).getAsMention() + " was unmuted by " + message.getMember().getAsMention());
                BotMain.Instance.getData().history.add(new ModCase(ArgsParser.getUserArg(message, 1).getId(), message.getAuthor().getId(), ModType.UNMUTE, null));
                BotMain.Instance.getData().getParser().save();
            }
        } else {
            BotMain.Instance.sendEmbed(message.getTextChannel(), "Error", ArgsParser.getUserArg(message, 1).getAsMention() + " is not muted", message.getAuthor());
        }
    }
}