package io.github.evercraftmc.evercraft.limbo.commands.player;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.loohp.limbo.commands.CommandSender;
import com.loohp.limbo.player.Player;
import com.loohp.limbo.utils.NamespacedKey;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import io.github.evercraftmc.evercraft.limbo.LimboMain;
import io.github.evercraftmc.evercraft.limbo.commands.LimboCommand;
import io.github.evercraftmc.evercraft.limbo.util.formatting.ComponentFormatter;

public class BungeeCommandCommand extends LimboCommand {
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

                try {
                    player.sendPluginMessage(new NamespacedKey("bungeecord:main"), out.toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(LimboMain.getInstance().getPluginMessages().getString("error.invalidArgs"))));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return Arrays.asList();
    }
}