package io.github.evercraftmc.evercraft.discord.commands.moderation;

import java.util.List;

import io.github.evercraftmc.evercraft.discord.DiscordBot;
import io.github.evercraftmc.evercraft.discord.args.ArgsParser;
import io.github.evercraftmc.evercraft.discord.args.ArgsValidator;
import io.github.evercraftmc.evercraft.discord.commands.Command;
import io.github.evercraftmc.evercraft.discord.data.types.data.ModCase;
import io.github.evercraftmc.evercraft.discord.data.types.data.ModType;
import io.github.evercraftmc.evercraft.discord.data.types.data.Warning;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;

public class ClearWarnsCommand extends Command {
    public ClearWarnsCommand() {
        super("clearwarns", new ArgsValidator.Arg[] { new ArgsValidator.Arg(ArgsValidator.ArgType.Member, false), new ArgsValidator.Arg(ArgsValidator.ArgType.String, true) }, new Permission[] { Permission.MODERATE_MEMBERS });
    }

    @Override
    public void run(Message message) {
        if (ArgsParser.hasArg(message, 2)) {
            DiscordBot.Instance.sendEmbed(message.getTextChannel(), "Warn", ArgsParser.getUserArg(message, 1).getAsMention() + "'s warns where cleared for " + ArgsParser.getStringArg(message, 2), message.getAuthor());
            DiscordBot.Instance.log(ArgsParser.getUserArg(message, 1).getAsMention() + "'s warns where cleared by " + message.getAuthor().getAsMention() + " for " + ArgsParser.getStringArg(message, 2));
        } else {
            DiscordBot.Instance.sendEmbed(message.getTextChannel(), "Warn", ArgsParser.getUserArg(message, 1).getAsMention() + "'s warns where cleared", message.getAuthor());
            DiscordBot.Instance.log(ArgsParser.getUserArg(message, 1).getAsMention() + "'s warns where cleared by " + message.getAuthor().getAsMention());
        }

        List<Warning> warnings = DiscordBot.Instance.getData().warnings.get(ArgsParser.getUserArg(message, 1).getId());
        for (Warning warning : warnings.toArray(new Warning[] { })) {
            DiscordBot.Instance.getData().history.add(new ModCase(warning.user, warning.mod, ModType.REMOVEWARN, ArgsParser.getStringArg(message, 2)));
            warnings.remove(warning);
        }
        DiscordBot.Instance.getData().getParser().save();
    }
}