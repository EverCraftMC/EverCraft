package io.github.evercraftmc.evercraft.discord.commands.logs;

import java.util.Arrays;
import io.github.evercraftmc.evercraft.discord.BotMain;
import io.github.evercraftmc.evercraft.discord.args.ArgsParser;
import io.github.evercraftmc.evercraft.discord.args.ArgsValidator;
import io.github.evercraftmc.evercraft.discord.commands.Command;
import io.github.evercraftmc.evercraft.discord.data.types.data.Warning;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

public class WarnsCommand extends Command {
    public WarnsCommand() {
        super("warns", Arrays.asList("warnings"), Arrays.asList(new ArgsValidator.Arg(ArgsValidator.ArgType.Member, false)), Arrays.asList(Permission.MODERATE_MEMBERS));
    }

    @Override
    public void run(Message message) {
        Member member = ArgsParser.getMemberArg(message, 1);

        StringBuilder warningString = new StringBuilder();

        for (Warning warning : BotMain.Instance.getData().warnings.get(member.getId())) {
            warningString.append("Warned by " + message.getJDA().retrieveUserById(warning.getMod()).complete().getAsMention() + " for " + warning.getReason() + " at " + warning.getTimestamp().toString() + "\n");
        }

        BotMain.Instance.sendEmbed(message.getChannel().asTextChannel(), "Warnings", warningString.toString().trim(), message.getAuthor());
    }
}