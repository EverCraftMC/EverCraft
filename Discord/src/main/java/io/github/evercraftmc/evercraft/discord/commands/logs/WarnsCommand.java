package io.github.evercraftmc.evercraft.discord.commands.logs;

import io.github.evercraftmc.evercraft.discord.DiscordBot;
import io.github.evercraftmc.evercraft.discord.args.ArgsParser;
import io.github.evercraftmc.evercraft.discord.args.ArgsValidator;
import io.github.evercraftmc.evercraft.discord.commands.Command;
import io.github.evercraftmc.evercraft.discord.data.types.data.Warning;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

public class WarnsCommand extends Command {
    public WarnsCommand() {
        super("warns", new ArgsValidator.Arg[] { new ArgsValidator.Arg(ArgsValidator.ArgType.Member, false) }, new Permission[] { Permission.MODERATE_MEMBERS });
    }

    @Override
    public void run(Message message) {
        Member member = ArgsParser.getMemberArg(message, 1);

        StringBuilder warningString = new StringBuilder();

        for (Warning warning : DiscordBot.Instance.getData().warnings.get(member.getId())) {
            warningString.append("Warned by " + message.getJDA().retrieveUserById(warning.mod).complete().getAsMention() + " for " + warning.reason + "\n");
        }

        DiscordBot.Instance.sendEmbed(message.getTextChannel(), "Warnings", warningString.toString().trim(), message.getAuthor());
    }
}