package io.github.evercraftmc.evercraft.discord.commands.info;

import io.github.evercraftmc.evercraft.discord.DiscordBot;
import io.github.evercraftmc.evercraft.discord.args.ArgsValidator;
import io.github.evercraftmc.evercraft.discord.commands.Command;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;

public class ServerInfoCommand extends Command {
    public ServerInfoCommand() {
        super("serverinfo", new ArgsValidator.Arg[] {}, new Permission[] {});
    }

    @Override
    public void run(Message message) {
        DiscordBot.Instance.sendEmbed(message.getTextChannel(), "Server Info", "Name: " + message.getGuild().getName() + "\nID: " + message.getGuild().getId() + "\nCreated: " + message.getGuild().getTimeCreated().getMonthValue() + "/" + message.getGuild().getTimeCreated().getDayOfMonth() + "/" + message.getGuild().getTimeCreated().getYear() + "\nOwner: " + message.getGuild().getOwner().getAsMention() + " (" + message.getGuild().getOwner().getId() + ")\nMembers: " + message.getGuild().getMemberCount() + "\nChannels: " + message.getGuild().getChannels().size() + "\nRoles: " + message.getGuild().getRoles().size() + "\nEmotes: " + message.getGuild().getEmotes().size());
    }
}