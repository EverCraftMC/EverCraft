package io.github.evercraftmc.evercraft.discord.commands.rr;

import java.util.Arrays;
import io.github.evercraftmc.evercraft.discord.BotMain;
import io.github.evercraftmc.evercraft.discord.args.ArgsParser;
import io.github.evercraftmc.evercraft.discord.args.ArgsValidator;
import io.github.evercraftmc.evercraft.discord.commands.Command;
import io.github.evercraftmc.evercraft.discord.data.types.data.ReactionRole;
import net.dv8tion.jda.api.entities.Message;

public class CreateReactionRoleCommand extends Command {
    public CreateReactionRoleCommand() {
        super("createreactionrole", Arrays.asList(), Arrays.asList(new ArgsValidator.Arg(ArgsValidator.ArgType.Message, false), new ArgsValidator.Arg(ArgsValidator.ArgType.Emoji, false), new ArgsValidator.Arg(ArgsValidator.ArgType.Role, false)), Arrays.asList());
    }

    @Override
    public void run(Message message) {
        Message reactionMessage = ArgsParser.getMessageArg(message, 1);

        BotMain.Instance.getData().reactions.add(new ReactionRole(reactionMessage.getTextChannel().getId(), reactionMessage.getId(), ArgsParser.getEmojiArg(message, 2).getAsReactionCode(), ArgsParser.getRoleArg(message, 3).getId()));
        BotMain.Instance.getData().getParser().save();

        reactionMessage.addReaction(ArgsParser.getEmojiArg(message, 2)).queue();
    }
}