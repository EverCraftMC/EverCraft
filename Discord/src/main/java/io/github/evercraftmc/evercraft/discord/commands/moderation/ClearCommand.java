package io.github.evercraftmc.evercraft.discord.commands.moderation;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import io.github.evercraftmc.evercraft.discord.BotMain;
import io.github.evercraftmc.evercraft.discord.args.ArgsParser;
import io.github.evercraftmc.evercraft.discord.args.ArgsValidator;
import io.github.evercraftmc.evercraft.discord.commands.Command;
import io.github.evercraftmc.evercraft.discord.data.types.data.ModCase;
import io.github.evercraftmc.evercraft.discord.data.types.data.ModType;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;

public class ClearCommand extends Command {
    public ClearCommand() {
        super("clear", Arrays.asList("remove"), Arrays.asList(new ArgsValidator.Arg(ArgsValidator.ArgType.Integer, false), new ArgsValidator.Arg(ArgsValidator.ArgType.User, true)), Arrays.asList(Permission.MANAGE_CHANNEL));
    }

    @Override
    public void run(Message message) {
        if (ArgsParser.hasArg(message, 2)) {
            List<Message> messages = new ArrayList<Message>();

            for (Message prevmessage : message.getChannel().asTextChannel().getHistoryBefore(message, ArgsParser.getIntegerArg(message, 1)).complete().getRetrievedHistory()) {
                if (prevmessage.getAuthor().equals(ArgsParser.getUserArg(message, 2))) {
                    messages.add(prevmessage);
                }
            }

            message.getChannel().asTextChannel().deleteMessages(messages).queue();

            BotMain.Instance.sendEmbed(message.getChannel().asTextChannel(), "Clear", "Cleared " + ArgsParser.getIntegerArg(message, 1) + " messages by " + ArgsParser.getUserArg(message, 2).getAsMention(), message.getAuthor());
            BotMain.Instance.log(ArgsParser.getIntegerArg(message, 1) + " messages from " + ArgsParser.getUserArg(message, 2).getAsMention() + " where cleared by " + message.getMember().getAsMention() + " in " + message.getChannel().asTextChannel().getAsMention());
            BotMain.Instance.getData().history.add(new ModCase(ArgsParser.getUserArg(message, 2).getId(), message.getAuthor().getId(), ModType.CLEARMESSAGES, message.getChannel().asTextChannel().getId(), Instant.now()));
            BotMain.Instance.getData().getParser().save();
        } else {
            message.getChannel().asTextChannel().deleteMessages(message.getChannel().asTextChannel().getHistoryBefore(message, ArgsParser.getIntegerArg(message, 1)).complete().getRetrievedHistory()).queue();

            BotMain.Instance.sendEmbed(message.getChannel().asTextChannel(), "Clear", "Cleared " + ArgsParser.getIntegerArg(message, 1) + " messages", message.getAuthor());
            BotMain.Instance.log(ArgsParser.getIntegerArg(message, 1) + " messages where cleared by " + message.getMember().getAsMention() + " in " + message.getChannel().asTextChannel().getAsMention());
            BotMain.Instance.getData().history.add(new ModCase(null, message.getAuthor().getId(), ModType.CLEARMESSAGES, message.getChannel().asTextChannel().getId(), Instant.now()));
            BotMain.Instance.getData().getParser().save();
        }
    }
}