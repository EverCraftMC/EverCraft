package io.github.evercraftmc.evercraft.spigot.commands.player;

import java.util.Arrays;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import io.github.evercraftmc.evercraft.spigot.SpigotMain;
import io.github.evercraftmc.evercraft.spigot.commands.SpigotCommand;
import io.github.evercraftmc.evercraft.spigot.util.formatting.ComponentFormatter;

public class BungeeCommandCommand extends SpigotCommand {
    public BungeeCommandCommand(String name, String description, List<String> aliases, String permission) {
        super(name, description, aliases, permission);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (args.length > 0) {
            if (sender instanceof Player player) {
                StringBuilder command = new StringBuilder();

                for (String arg : args) {
                    command.append(arg + " ");
                }

                String commandString = command.toString();

                if (commandString.startsWith("/")) {
                    commandString = commandString.substring(1);
                }

                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("crossCommand");
                out.writeUTF(player.getUniqueId().toString());
                out.writeUTF(commandString.trim());

                player.sendPluginMessage(SpigotMain.getInstance(), "BungeeCord", out.toByteArray());
            }
        } else {
            sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getParsed().error.invalidArgs)));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return Arrays.asList();
    }
}