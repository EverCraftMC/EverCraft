package io.github.evercraftmc.evercraft.discord.commands.moderation;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import io.github.evercraftmc.evercraft.discord.BotMain;
import io.github.evercraftmc.evercraft.discord.args.ArgsParser;
import io.github.evercraftmc.evercraft.discord.args.ArgsValidator;
import io.github.evercraftmc.evercraft.discord.commands.Command;
import io.github.evercraftmc.evercraft.discord.data.types.data.ModCase;
import io.github.evercraftmc.evercraft.discord.data.types.data.ModType;
import io.github.evercraftmc.evercraft.discord.data.types.data.Warning;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;

public class WarnCommand extends Command {
    public WarnCommand() {
        super("warn", Arrays.asList(), Arrays.asList(new ArgsValidator.Arg(ArgsValidator.ArgType.Member, false), new ArgsValidator.Arg(ArgsValidator.ArgType.String, false)), Arrays.asList(Permission.MODERATE_MEMBERS));
    }

    @Override
    public void run(Message message) {
        BotMain.Instance.sendEmbed(message.getTextChannel(), "Warn", ArgsParser.getUserArg(message, 1).getAsMention() + " was warned for " + ArgsParser.getStringArg(message, 2), message.getAuthor());
        BotMain.Instance.log(ArgsParser.getUserArg(message, 1).getAsMention() + " was warned by " + message.getMember().getAsMention() + " for " + ArgsParser.getStringArg(message, 2));
        BotMain.Instance.getData().history.add(new ModCase(ArgsParser.getUserArg(message, 1).getId(), message.getAuthor().getId(), ModType.WARN, ArgsParser.getStringArg(message, 2), Instant.now()));

        if (!BotMain.Instance.getData().warnings.containsKey(ArgsParser.getUserArg(message, 1).getId())) {
            BotMain.Instance.getData().warnings.put(ArgsParser.getUserArg(message, 1).getId(), new ArrayList<Warning>());
        }
        BotMain.Instance.getData().warnings.get(ArgsParser.getUserArg(message, 1).getId()).add(new Warning(ArgsParser.getUserArg(message, 1).getId(), message.getAuthor().getId(), ArgsParser.getStringArg(message, 2), Instant.now()));
        BotMain.Instance.getData().getParser().save();
    }
}