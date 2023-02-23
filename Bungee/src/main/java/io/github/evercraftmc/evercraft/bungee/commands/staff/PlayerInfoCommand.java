package io.github.evercraftmc.evercraft.bungee.commands.staff;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import io.github.evercraftmc.evercraft.bungee.BungeeMain;
import io.github.evercraftmc.evercraft.bungee.commands.BungeeCommand;
import io.github.evercraftmc.evercraft.bungee.util.formatting.ComponentFormatter;
import io.github.evercraftmc.evercraft.bungee.util.player.BungeePlayerResolver;
import io.github.evercraftmc.evercraft.shared.util.StringUtils;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import io.github.evercraftmc.evercraft.shared.util.player.SimplePlayer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PlayerInfoCommand extends BungeeCommand {
    public PlayerInfoCommand(String name, String description, List<String> aliases, String permission) {
        super(name, description, aliases, permission);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (args.length > 0) {
            SimplePlayer player = BungeePlayerResolver.getPlayer(BungeeMain.getInstance().getPluginData(), args[0]);

            if (player != null) {
                ProxiedPlayer bungeePlayer = BungeeMain.getInstance().getProxy().getPlayer(args[0]);

                if (bungeePlayer != null) {
                    sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().get().playerInfo.online
                        .replace("{player}", BungeeMain.getInstance().getPluginData().get().players.get(player.getUniqueId().toString()).lastName)
                        .replace("{uuid}", BungeeMain.getInstance().getPluginData().get().players.get(player.getUniqueId().toString()).uuid)
                        .replace("{ip}", sender.hasPermission("evercraft.commands.staff.playerinfo.ip") ? BungeeMain.getInstance().getPluginData().get().players.get(player.getUniqueId().toString()).lastIP : "hidden")
                        .replace("{nickname}", BungeeMain.getInstance().getPluginData().get().players.get(player.getUniqueId().toString()).nickname)
                        .replace("{ping}", bungeePlayer.getPing() + "ms")
                        .replace("{version}", ProtocolVersion.getProtocol(Via.getAPI().getPlayerVersion(player.getUniqueId())).getName())
                        .replace("{protocolVersion}", Via.getAPI().getPlayerVersion(player.getUniqueId()) + ""))));
                } else {
                    sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().get().playerInfo.offline
                        .replace("{player}", BungeeMain.getInstance().getPluginData().get().players.get(player.getUniqueId().toString()).lastName)
                        .replace("{uuid}", BungeeMain.getInstance().getPluginData().get().players.get(player.getUniqueId().toString()).uuid)
                        .replace("{ip}", sender.hasPermission("evercraft.commands.staff.playerinfo.ip") ? BungeeMain.getInstance().getPluginData().get().players.get(player.getUniqueId().toString()).lastIP : "hidden")
                        .replace("{nickname}", BungeeMain.getInstance().getPluginData().get().players.get(player.getUniqueId().toString()).nickname))));
                }
            } else {
                sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().get().error.playerNotFound.replace("{player}", args[0]))));
            }
        } else {
            sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().get().error.invalidArgs)));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<String>();

        if (args.length == 1) {
            for (ProxiedPlayer player : BungeeMain.getInstance().getProxy().getPlayers()) {
                list.add(player.getName());
            }
        } else {
            return Arrays.asList();
        }

        if (args.length > 0) {
            return StringUtils.matchPartial(args[args.length - 1], list);
        } else {
            return list;
        }
    }
}