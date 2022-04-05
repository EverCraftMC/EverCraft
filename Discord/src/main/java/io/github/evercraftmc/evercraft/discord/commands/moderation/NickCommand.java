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

public class NickCommand extends Command {
    public NickCommand() {
        super("nick", Arrays.asList("nickname"), Arrays.asList(new ArgsValidator.Arg(ArgsValidator.ArgType.Member, false), new ArgsValidator.Arg(ArgsValidator.ArgType.String, false)), Arrays.asList(Permission.NICKNAME_MANAGE));
    }

    @Override
    public void run(Message message) {
        ArgsParser.getMemberArg(message, 1).modifyNickname(ArgsParser.getStringArg(message, 2)).queue();

        BotMain.Instance.sendEmbed(message.getTextChannel(), "Nick", "Set " + ArgsParser.getUserArg(message, 1).getAsMention() + "'s nick to " + ArgsParser.getStringArg(message, 2), message.getAuthor());
        BotMain.Instance.log(message.getMember().getAsMention() + " set " + ArgsParser.getUserArg(message, 1).getAsMention() + "'s nick to " + ArgsParser.getStringArg(message, 2));
        BotMain.Instance.getData().history.add(new ModCase(ArgsParser.getUserArg(message, 1).getId(), message.getAuthor().getId(), ModType.CHANGENICK, ArgsParser.getStringArg(message, 2)));
        BotMain.Instance.getData().getParser().save();
    }
}