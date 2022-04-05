package io.github.evercraftmc.evercraft.discord.commands.info;

import java.util.Arrays;
import io.github.evercraftmc.evercraft.discord.BotMain;
import io.github.evercraftmc.evercraft.discord.args.ArgsValidator;
import io.github.evercraftmc.evercraft.discord.commands.Command;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;

public class HelpCommand extends Command {
    public HelpCommand() {
        super("help", Arrays.asList(), Arrays.asList(), Arrays.asList());
    }

    @Override
    public void run(Message message) {
        StringBuilder helpString = new StringBuilder();

        for (Command command : BotMain.Instance.commands) {
            StringBuilder args = new StringBuilder("");

            for (ArgsValidator.Arg arg : command.getArgs()) {
                if (!arg.optional()) {
                    args.append(arg.type().toString().toLowerCase() + " ");
                } else {
                    args.append(arg.type().toString().toLowerCase() + " (optional)");
                }
            }

            if (!command.getPermissions().isEmpty()) {
                StringBuilder permissions = new StringBuilder("");

                for (Permission permission : command.getPermissions()) {
                    permissions.append(permission.toString().toLowerCase() + " ");
                }

                helpString.append((BotMain.Instance.getConfig().getPrefix() + command.getName() + " " + args.toString()).trim() + " - " + permissions.toString().trim() + "\n");
            } else {
                helpString.append((BotMain.Instance.getConfig().getPrefix() + command.getName() + " " + args.toString()).trim() + "\n");
            }
        }

        BotMain.Instance.sendEmbed(message.getTextChannel(), "Help", helpString.toString().trim(), message.getAuthor());
    }
}