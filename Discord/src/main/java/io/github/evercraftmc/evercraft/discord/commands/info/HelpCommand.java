package io.github.evercraftmc.evercraft.discord.commands.info;

import io.github.evercraftmc.evercraft.discord.DiscordBot;
import io.github.evercraftmc.evercraft.discord.args.ArgsValidator;
import io.github.evercraftmc.evercraft.discord.commands.Command;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;

public class HelpCommand extends Command {
    public HelpCommand() {
        super("help", new ArgsValidator.Arg[] {}, new Permission[] {});
    }

    @Override
    public void run(Message message) {
        StringBuilder helpString = new StringBuilder();

        for (Command command : DiscordBot.Instance.commands) {
            StringBuilder args = new StringBuilder("");

            for (ArgsValidator.Arg arg : command.args) {
                if (!arg.optional()) {
                    args.append(arg.type().toString().toLowerCase() + " ");
                } else {
                    args.append(arg.type().toString().toLowerCase() + " (optional)");
                }
            }

            if (command.permissions.length > 0) {
                StringBuilder permissions = new StringBuilder("");

                for (Permission permission : command.permissions) {
                    permissions.append(permission.toString().toLowerCase() + " ");
                }

                helpString.append((DiscordBot.Instance.getConfig().getPrefix() + command.name + " " + args.toString()).trim() + " - " + permissions.toString().trim() + "\n");
            } else {
                helpString.append((DiscordBot.Instance.getConfig().getPrefix() + command.name + " " + args.toString()).trim() + "\n");
            }
        }

        DiscordBot.Instance.sendEmbed(message.getTextChannel(), "Help", helpString.toString().trim(), message.getAuthor());
    }
}