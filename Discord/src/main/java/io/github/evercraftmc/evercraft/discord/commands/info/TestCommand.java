package io.github.evercraftmc.evercraft.discord.commands.info;

import java.util.List;
import io.github.evercraftmc.evercraft.discord.commands.DiscordCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;

public class TestCommand extends DiscordCommand {
    public TestCommand(String name, String description, List<String> aliases, Permission permission) {
        super(name, description, aliases, permission);
    }

    public void run(Message message, String[] args) {
        if (args.length == 0) {
            message.reply("Test :D").queue();
        } else {
            message.reply(args[0]).queue();
        }
    }
}