package io.github.evercraftmc.evercraft.discord.commands.logs;

import io.github.evercraftmc.evercraft.discord.DiscordBot;
import io.github.evercraftmc.evercraft.discord.args.ArgsParser;
import io.github.evercraftmc.evercraft.discord.args.ArgsValidator;
import io.github.evercraftmc.evercraft.discord.commands.Command;
import io.github.evercraftmc.evercraft.discord.data.types.data.ModCase;
import io.github.evercraftmc.evercraft.discord.data.types.data.ModType;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

public class HistoryCommand extends Command {
    public HistoryCommand() {
        super("history", new ArgsValidator.Arg[] { new ArgsValidator.Arg(ArgsValidator.ArgType.Member, false) }, new Permission[] { Permission.MODERATE_MEMBERS });
    }

    @Override
    public void run(Message message) {
        Member member = ArgsParser.getMemberArg(message, 1);

        StringBuilder modsString = new StringBuilder();

        for (ModCase modcase : DiscordBot.Instance.getData().history) {
            if (modcase.user != null && modcase.user.equals(member.getId())) {
                if (modcase.reason != null && !modcase.reason.equals("")) {
                    if (modcase.type == ModType.WARN) {
                        modsString.append("Warned by " + message.getJDA().retrieveUserById(modcase.mod).complete().getAsMention() + " for " + modcase.reason + "\n");
                    } else if (modcase.type == ModType.REMOVEWARN) {
                        modsString.append("Unwarned by " + message.getJDA().retrieveUserById(modcase.mod).complete().getAsMention() + " for " + modcase.reason + "\n");
                    } else if (modcase.type == ModType.KICK) {
                        modsString.append("Kicked by " + message.getJDA().retrieveUserById(modcase.mod).complete().getAsMention() + " for " + modcase.reason + "\n");
                    } else if (modcase.type == ModType.BAN) {
                        modsString.append("Banned by " + message.getJDA().retrieveUserById(modcase.mod).complete().getAsMention() + " for " + modcase.reason + "\n");
                    } else if (modcase.type == ModType.UNBAN) {
                        modsString.append("Unbanned by " + message.getJDA().retrieveUserById(modcase.mod).complete().getAsMention() + " for " + modcase.reason + "\n");
                    } else if (modcase.type == ModType.MUTE) {
                        modsString.append("Muted by " + message.getJDA().retrieveUserById(modcase.mod).complete().getAsMention() + " for " + modcase.reason + "\n");
                    } else if (modcase.type == ModType.UNMUTE) {
                        modsString.append("Unmuted by " + message.getJDA().retrieveUserById(modcase.mod).complete().getAsMention() + " for " + modcase.reason + "\n");
                    } else if (modcase.type == ModType.CLEARMESSAGES) {
                        modsString.append("Messages cleared by " + message.getJDA().retrieveUserById(modcase.mod).complete().getAsMention() + " in " + message.getGuild().getTextChannelById(modcase.reason).getAsMention() + "\n");
                    } else if (modcase.type == ModType.CHANGENICK) {
                        modsString.append("Nick was changed by " + message.getJDA().retrieveUserById(modcase.mod).complete().getAsMention() + " for " + modcase.reason + "\n");
                    }
                } else {
                    if (modcase.type == ModType.WARN) {
                        modsString.append("Warned by " + message.getJDA().retrieveUserById(modcase.mod).complete().getAsMention() + "\n");
                    } else if (modcase.type == ModType.REMOVEWARN) {
                        modsString.append("Unwarned by " + message.getJDA().retrieveUserById(modcase.mod).complete().getAsMention() + "\n");
                    } else if (modcase.type == ModType.KICK) {
                        modsString.append("Kicked by " + message.getJDA().retrieveUserById(modcase.mod).complete().getAsMention() + "\n");
                    } else if (modcase.type == ModType.BAN) {
                        modsString.append("Banned by " + message.getJDA().retrieveUserById(modcase.mod).complete().getAsMention() + "\n");
                    } else if (modcase.type == ModType.UNBAN) {
                        modsString.append("Unbanned by " + message.getJDA().retrieveUserById(modcase.mod).complete().getAsMention() + "\n");
                    } else if (modcase.type == ModType.MUTE) {
                        modsString.append("Muted by " + message.getJDA().retrieveUserById(modcase.mod).complete().getAsMention() + "\n");
                    } else if (modcase.type == ModType.UNMUTE) {
                        modsString.append("Unmuted by " + message.getJDA().retrieveUserById(modcase.mod).complete().getAsMention() + "\n");
                    } else if (modcase.type == ModType.CLEARMESSAGES) {
                        modsString.append("Messages cleared by " + message.getJDA().retrieveUserById(modcase.mod).complete().getAsMention() + " in " + message.getGuild().getTextChannelById(modcase.reason).getAsMention() + "\n");
                    } else if (modcase.type == ModType.CHANGENICK) {
                        modsString.append("Nick was changed by " + message.getJDA().retrieveUserById(modcase.mod).complete().getAsMention() + "\n");
                    }
                }
            }
        }

        DiscordBot.Instance.sendEmbed(message.getTextChannel(), "History", modsString.toString().trim(), message.getAuthor());
    }
}