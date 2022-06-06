package io.github.evercraftmc.evercraft.discord.commands.rr;

import java.util.Arrays;
import io.github.evercraftmc.evercraft.discord.BotMain;
import io.github.evercraftmc.evercraft.discord.args.ArgsParser;
import io.github.evercraftmc.evercraft.discord.args.ArgsValidator;
import io.github.evercraftmc.evercraft.discord.commands.Command;
import io.github.evercraftmc.evercraft.discord.data.types.data.ReactionRole;
import net.dv8tion.jda.api.entities.Message;

public class RemoveReactionRoleCommand extends Command {
    public RemoveReactionRoleCommand() {
        super("removereactionrole", Arrays.asList(), Arrays.asList(new ArgsValidator.Arg(ArgsValidator.ArgType.Message, false)), Arrays.asList());
    }

    @Override
    public void run(Message message) {
        Message reactionMessage = ArgsParser.getMessageArg(message, 1);

        for (ReactionRole reactionRole : BotMain.Instance.getData().reactions) {
            if (reactionRole.getMessage().equals(reactionMessage.getId())) {
                reactionMessage.removeReaction(reactionRole.getEmoji()).queue();

                BotMain.Instance.getData().reactions.remove(reactionRole);
                BotMain.Instance.getData().getParser().save();
            }
        }
    }
}