package io.github.evercraftmc.evercraft.bungee.commands.player;

import java.util.Arrays;
import java.util.List;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import io.github.evercraftmc.evercraft.bungee.BungeeMain;
import io.github.evercraftmc.evercraft.bungee.commands.BungeeCommand;
import io.github.evercraftmc.evercraft.bungee.util.formatting.ComponentFormatter;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class SpigotCommandCommand extends BungeeCommand {
    public SpigotCommandCommand(String name, String description, List<String> aliases, String permission) {
        super(name, description, aliases, permission);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer player) {
            if (args.length > 0) {
                StringBuilder command = new StringBuilder();

                for (String arg : args) {
                    command.append(arg + " ");
                }

                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("crossCommand");
                out.writeUTF(player.getUniqueId().toString());
                out.writeUTF(command.substring(0, command.length() - 1));

                player.getServer().sendData("BungeeCord", out.toByteArray());
            } else {
                sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("error.invalidArgs"))));
            }
        } else {
            sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("error.noConsole"))));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return Arrays.asList();
    }
}