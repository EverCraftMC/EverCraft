package io.github.evercraftmc.evercraft.discord.commands.info;

import io.github.evercraftmc.evercraft.discord.DiscordBot;
import io.github.evercraftmc.evercraft.discord.args.ArgsParser;
import io.github.evercraftmc.evercraft.discord.args.ArgsValidator;
import io.github.evercraftmc.evercraft.discord.commands.Command;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

public class WhoIsCommand extends Command {
    public WhoIsCommand() {
        super("whois", new ArgsValidator.Arg[] { new ArgsValidator.Arg(ArgsValidator.ArgType.User, false) }, new Permission[] {});
    }

    @Override
    public void run(Message message) {
        User user = ArgsParser.getUserArg(message, 1);

        if (message.getGuild().isMember(user)) {
            Member member = message.getGuild().retrieveMember(user).complete();

            DiscordBot.Instance.sendEmbed(message.getTextChannel(), "Who Is", "**" + user.getAsMention() + "**\nName: " + user.getAsTag() + "\nNick: " + member.getNickname() + "\nId: " + user.getId() + "\nJoined Server: " + member.getTimeJoined().getMonthValue() + "/" + member.getTimeJoined().getDayOfMonth() + "/" + member.getTimeJoined().getYear() + "\nJoined Discord: " + user.getTimeCreated().getMonthValue() + "/" + user.getTimeCreated().getDayOfMonth() + "/" + user.getTimeCreated().getYear() + "\nRole: " + member.getRoles().get(0).getAsMention() + " (" + member.getRoles().get(0).getId() + ")\nIs Bot: " + user.isBot());
        } else {
            DiscordBot.Instance.sendEmbed(message.getTextChannel(), "Who Is", "**" + user.getAsMention() + "**\nName: " + user.getAsTag() + "\nId: " + user.getId() + "\nJoined Discord: " + user.getTimeCreated().getMonthValue() + "/" + user.getTimeCreated().getDayOfMonth() + "/" + user.getTimeCreated().getYear() + "\nIs Bot: " + user.isBot());
        }
    }
}