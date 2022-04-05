package io.github.evercraftmc.evercraft.discord.commands.info;

import java.util.Arrays;
import io.github.evercraftmc.evercraft.discord.BotMain;
import io.github.evercraftmc.evercraft.discord.commands.Command;
import net.dv8tion.jda.api.entities.Message;

public class ServerInfoCommand extends Command {
    public ServerInfoCommand() {
        super("serverinfo", Arrays.asList("server"), Arrays.asList(), Arrays.asList());
    }

    @Override
    public void run(Message message) {
        BotMain.Instance.sendEmbed(message.getTextChannel(), "Server Info", "Name: " + message.getGuild().getName() + "\nID: " + message.getGuild().getId() + "\nCreated: " + message.getGuild().getTimeCreated().getMonthValue() + "/" + message.getGuild().getTimeCreated().getDayOfMonth() + "/" + message.getGuild().getTimeCreated().getYear() + "\nOwner: " + message.getGuild().getOwner().getAsMention() + " (" + message.getGuild().getOwner().getId() + ")\nMembers: " + message.getGuild().getMemberCount() + "\nChannels: " + message.getGuild().getChannels().size() + "\nRoles: " + message.getGuild().getRoles().size() + "\nEmotes: " + message.getGuild().getEmotes().size());
    }
}