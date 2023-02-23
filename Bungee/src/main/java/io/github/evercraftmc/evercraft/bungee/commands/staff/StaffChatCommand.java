package io.github.evercraftmc.evercraft.bungee.commands.staff;

import java.util.Arrays;
import java.util.List;
import io.github.evercraftmc.evercraft.bungee.BungeeMain;
import io.github.evercraftmc.evercraft.bungee.commands.BungeeCommand;
import io.github.evercraftmc.evercraft.bungee.util.formatting.ComponentFormatter;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class StaffChatCommand extends BungeeCommand {
    public StaffChatCommand(String name, String description, List<String> aliases, String permission) {
        super(name, description, aliases, permission);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (args.length > 0) {
            String senderName;
            if (sender instanceof ProxiedPlayer player) {
                senderName = player.getDisplayName();
            } else {
                senderName = "CONSOLE";
            }

            StringBuilder message = new StringBuilder();

            for (String arg : args) {
                message.append(arg + " ");
            }

            for (ProxiedPlayer player : BungeeMain.getInstance().getProxy().getPlayers()) {
                if (player.hasPermission(this.getPermission())) {
                    player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().get().chat.staff.replace("{player}", senderName).replace("{message}", message.substring(0, message.length() - 1)))));
                }
            }

            BungeeMain.getInstance().getDiscordBot().getGuild().getTextChannelById(BungeeMain.getInstance().getPluginConfig().get().discord.staffChannelId).sendMessage(TextFormatter.discordFormat(BungeeMain.getInstance().getPluginMessages().get().chat.staff.replace("{player}", senderName).replace("{message}", message.substring(0, message.length() - 1)))).queue();
        } else {
            sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().get().error.invalidArgs)));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return Arrays.asList();
    }
}