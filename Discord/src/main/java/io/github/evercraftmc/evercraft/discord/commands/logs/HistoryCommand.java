package io.github.evercraftmc.evercraft.discord.commands.logs;

import java.util.Arrays;
import io.github.evercraftmc.evercraft.discord.BotMain;
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
        super("history", Arrays.asList("lookup"), Arrays.asList(new ArgsValidator.Arg(ArgsValidator.ArgType.Member, false)), Arrays.asList(Permission.MODERATE_MEMBERS));
    }

    @Override
    public void run(Message message) {
        Member member = ArgsParser.getMemberArg(message, 1);

        StringBuilder modsString = new StringBuilder();

        for (ModCase modcase : BotMain.Instance.getData().history) {
            if (modcase.getUser() != null && modcase.getUser().equals(member.getId())) {
                if (modcase.getReason() != null && !modcase.getReason().equals("")) {
                    if (modcase.getType() == ModType.WARN) {
                        modsString.append("Warned by " + message.getJDA().retrieveUserById(modcase.getMod()).complete().getAsMention() + " for " + modcase.getReason() + " at " + modcase.getTimestamp().toString() + "\n");
                    } else if (modcase.getType() == ModType.REMOVEWARN) {
                        modsString.append("Unwarned by " + message.getJDA().retrieveUserById(modcase.getMod()).complete().getAsMention() + " for " + modcase.getReason() + " at " + modcase.getTimestamp().toString() + "\n");
                    } else if (modcase.getType() == ModType.KICK) {
                        modsString.append("Kicked by " + message.getJDA().retrieveUserById(modcase.getMod()).complete().getAsMention() + " for " + modcase.getReason() + " at " + modcase.getTimestamp().toString() + "\n");
                    } else if (modcase.getType() == ModType.BAN) {
                        modsString.append("Banned by " + message.getJDA().retrieveUserById(modcase.getMod()).complete().getAsMention() + " for " + modcase.getReason() + " at " + modcase.getTimestamp().toString() + "\n");
                    } else if (modcase.getType() == ModType.UNBAN) {
                        modsString.append("Unbanned by " + message.getJDA().retrieveUserById(modcase.getMod()).complete().getAsMention() + " for " + modcase.getReason() + " at " + modcase.getTimestamp().toString() + "\n");
                    } else if (modcase.getType() == ModType.MUTE) {
                        modsString.append("Muted by " + message.getJDA().retrieveUserById(modcase.getMod()).complete().getAsMention() + " for " + modcase.getReason() + " at " + modcase.getTimestamp().toString() + "\n");
                    } else if (modcase.getType() == ModType.UNMUTE) {
                        modsString.append("Unmuted by " + message.getJDA().retrieveUserById(modcase.getMod()).complete().getAsMention() + " for " + modcase.getReason() + " at " + modcase.getTimestamp().toString() + "\n");
                    } else if (modcase.getType() == ModType.CLEARMESSAGES) {
                        modsString.append("Messages cleared by " + message.getJDA().retrieveUserById(modcase.getMod()).complete().getAsMention() + " in " + message.getGuild().getTextChannelById(modcase.getReason()).getAsMention() + " at " + modcase.getTimestamp().toString() + "\n");
                    } else if (modcase.getType() == ModType.CHANGENICK) {
                        modsString.append("Nick was changed by " + message.getJDA().retrieveUserById(modcase.getMod()).complete().getAsMention() + " for " + modcase.getReason() + " at " + modcase.getTimestamp().toString() + "\n");
                    }
                } else {
                    if (modcase.getType() == ModType.WARN) {
                        modsString.append("Warned by " + message.getJDA().retrieveUserById(modcase.getMod()).complete().getAsMention() + " at " + modcase.getTimestamp().toString() + "\n");
                    } else if (modcase.getType() == ModType.REMOVEWARN) {
                        modsString.append("Unwarned by " + message.getJDA().retrieveUserById(modcase.getMod()).complete().getAsMention() + " at " + modcase.getTimestamp().toString() + "\n");
                    } else if (modcase.getType() == ModType.KICK) {
                        modsString.append("Kicked by " + message.getJDA().retrieveUserById(modcase.getMod()).complete().getAsMention() + " at " + modcase.getTimestamp().toString() + "\n");
                    } else if (modcase.getType() == ModType.BAN) {
                        modsString.append("Banned by " + message.getJDA().retrieveUserById(modcase.getMod()).complete().getAsMention() + " at " + modcase.getTimestamp().toString() + "\n");
                    } else if (modcase.getType() == ModType.UNBAN) {
                        modsString.append("Unbanned by " + message.getJDA().retrieveUserById(modcase.getMod()).complete().getAsMention() + " at " + modcase.getTimestamp().toString() + "\n");
                    } else if (modcase.getType() == ModType.MUTE) {
                        modsString.append("Muted by " + message.getJDA().retrieveUserById(modcase.getMod()).complete().getAsMention() + " at " + modcase.getTimestamp().toString() + "\n");
                    } else if (modcase.getType() == ModType.UNMUTE) {
                        modsString.append("Unmuted by " + message.getJDA().retrieveUserById(modcase.getMod()).complete().getAsMention() + " at " + modcase.getTimestamp().toString() + "\n");
                    } else if (modcase.getType() == ModType.CLEARMESSAGES) {
                        modsString.append("Messages cleared by " + message.getJDA().retrieveUserById(modcase.getMod()).complete().getAsMention() + " in " + message.getGuild().getTextChannelById(modcase.getReason()).getAsMention() + " at " + modcase.getTimestamp().toString() + "\n");
                    } else if (modcase.getType() == ModType.CHANGENICK) {
                        modsString.append("Nick was changed by " + message.getJDA().retrieveUserById(modcase.getMod()).complete().getAsMention() + " at " + modcase.getTimestamp().toString() + "\n");
                    }
                }
            }
        }

        BotMain.Instance.sendEmbed(message.getTextChannel(), "History", modsString.toString().trim(), message.getAuthor());
    }
}