package io.github.evercraftmc.evercraft.discord.commands.info;

import java.util.List;
import io.github.evercraftmc.evercraft.discord.DiscordMain;
import io.github.evercraftmc.evercraft.discord.commands.DiscordCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;

public class InfoCommand extends DiscordCommand {
    public InfoCommand(String name, String description, List<String> aliases, Permission permission) {
        super(name, description, aliases, permission);
    }

    @Override
    public void run(Message message, String[] args) {
        message.reply(DiscordMain.getInstance().getPluginMessages().getParsed().info.get(this.getName())).queue();
    }
}