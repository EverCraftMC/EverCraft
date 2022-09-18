package io.github.evercraftmc.evercraft.discord.commands.moderation;

import java.time.Instant;
import java.util.Arrays;
import io.github.evercraftmc.evercraft.discord.BotMain;
import io.github.evercraftmc.evercraft.discord.args.ArgsParser;
import io.github.evercraftmc.evercraft.discord.args.ArgsValidator;
import io.github.evercraftmc.evercraft.discord.commands.Command;
import io.github.evercraftmc.evercraft.discord.data.types.data.ModCase;
import io.github.evercraftmc.evercraft.discord.data.types.data.ModType;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;

public class BanCommand extends Command {
    public BanCommand() {
        super("ban", Arrays.asList("permban"), Arrays.asList(new ArgsValidator.Arg(ArgsValidator.ArgType.Member, false), new ArgsValidator.Arg(ArgsValidator.ArgType.String, true)), Arrays.asList(Permission.BAN_MEMBERS));
    }

    @Override
    public void run(Message message) {
        try {
            message.getGuild().retrieveBan(ArgsParser.getUserArg(message, 1)).complete();

            BotMain.Instance.sendEmbed(message.getChannel().asTextChannel(), "Error", ArgsParser.getUserArg(message, 1).getAsMention() + " is already banned", message.getAuthor());
        } catch (ErrorResponseException err) {
            ArgsParser.getMemberArg(message, 1).ban(0).queue();

            if (ArgsParser.hasArg(message, 2)) {
                BotMain.Instance.sendEmbed(message.getChannel().asTextChannel(), "Banned", ArgsParser.getUserArg(message, 1).getAsMention() + " was banned for " + ArgsParser.getStringArg(message, 2), message.getAuthor());
                BotMain.Instance.log(ArgsParser.getUserArg(message, 1).getAsMention() + " was banned by " + message.getMember().getAsMention() + " for " + ArgsParser.getStringArg(message, 2));
                BotMain.Instance.getData().history.add(new ModCase(ArgsParser.getUserArg(message, 1).getId(), message.getAuthor().getId(), ModType.BAN, ArgsParser.getStringArg(message, 2), Instant.now()));
                BotMain.Instance.getData().getParser().save();
            } else {
                BotMain.Instance.sendEmbed(message.getChannel().asTextChannel(), "Banned", ArgsParser.getUserArg(message, 1).getAsMention() + " was banned", message.getAuthor());
                BotMain.Instance.log(ArgsParser.getUserArg(message, 1).getAsMention() + " was banned by " + message.getMember().getAsMention());
                BotMain.Instance.getData().history.add(new ModCase(ArgsParser.getUserArg(message, 1).getId(), message.getAuthor().getId(), ModType.BAN, null, Instant.now()));
                BotMain.Instance.getData().getParser().save();
            }
        }
    }
}