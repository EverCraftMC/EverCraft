package io.github.evercraftmc.evercraft.bungee.commands.player;

import java.util.Arrays;
import java.util.List;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import io.github.evercraftmc.evercraft.bungee.BungeeMain;
import io.github.evercraftmc.evercraft.bungee.commands.BungeeCommand;
import io.github.evercraftmc.evercraft.bungee.util.formatting.ComponentFormatter;
import io.github.evercraftmc.evercraft.bungee.util.network.TabListUtil;
import io.github.evercraftmc.evercraft.bungee.util.player.BungeePlayerResolver;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class NickNameCommand extends BungeeCommand {
    public NickNameCommand(String name, String description, List<String> aliases, String permission) {
        super(name, description, aliases, permission);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer player) {
            if (args.length > 0) {
                if (TextFormatter.removeColors(args[0]).length() > 0 && TextFormatter.removeColors(args[0]).length() < 16 && args[0].length() < 32) {
                    BungeeMain.getInstance().getPluginData().getParsed().players.get(player.getUniqueId().toString()).nickname = args[0];

                    player.setDisplayName(TextFormatter.translateColors(BungeePlayerResolver.getDisplayName(BungeeMain.getInstance().getPluginData(), player.getUniqueId())));
                    TabListUtil.updatePlayerName(player);

                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF("updateName");
                    out.writeUTF(player.getUniqueId().toString());
                    player.getServer().sendData("BungeeCord", out.toByteArray());

                    player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().nickname.replace("{nickname}", args[0]))));
                } else {
                    sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().error.invalidArgs)));
                }
            } else {
                sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().error.invalidArgs)));
            }
        } else {
            sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().error.noConsole)));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return Arrays.asList();
    }
}