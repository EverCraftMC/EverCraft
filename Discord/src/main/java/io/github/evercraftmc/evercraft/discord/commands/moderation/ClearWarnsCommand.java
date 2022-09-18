package io.github.evercraftmc.evercraft.discord.commands.moderation;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import io.github.evercraftmc.evercraft.discord.BotMain;
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
        super("clearwarns", Arrays.asList("clearwarnings"), Arrays.asList(new ArgsValidator.Arg(ArgsValidator.ArgType.Member, false), new ArgsValidator.Arg(ArgsValidator.ArgType.String, true)), Arrays.asList(Permission.MODERATE_MEMBERS));
    }

    @Override
    public void run(Message message) {
        if (ArgsParser.hasArg(message, 2)) {
            BotMain.Instance.sendEmbed(message.getChannel().asTextChannel(), "Warn", ArgsParser.getUserArg(message, 1).getAsMention() + "'s warns where cleared for " + ArgsParser.getStringArg(message, 2), message.getAuthor());
            BotMain.Instance.log(ArgsParser.getUserArg(message, 1).getAsMention() + "'s warns where cleared by " + message.getAuthor().getAsMention() + " for " + ArgsParser.getStringArg(message, 2));
        } else {
            BotMain.Instance.sendEmbed(message.getChannel().asTextChannel(), "Warn", ArgsParser.getUserArg(message, 1).getAsMention() + "'s warns where cleared", message.getAuthor());
            BotMain.Instance.log(ArgsParser.getUserArg(message, 1).getAsMention() + "'s warns where cleared by " + message.getAuthor().getAsMention());
        }

        List<Warning> warnings = BotMain.Instance.getData().warnings.get(ArgsParser.getUserArg(message, 1).getId());
        for (Warning warning : warnings.toArray(new Warning[] {})) {
            BotMain.Instance.getData().history.add(new ModCase(warning.getUser(), warning.getMod(), ModType.REMOVEWARN, ArgsParser.getStringArg(message, 2), Instant.now()));
            warnings.remove(warning);
        }
        BotMain.Instance.getData().getParser().save();
    }
}