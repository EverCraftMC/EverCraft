package io.github.evercraftmc.evercraft.bungee.commands.moderation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import io.github.evercraftmc.evercraft.bungee.BungeeMain;
import io.github.evercraftmc.evercraft.bungee.commands.BungeeCommand;
import io.github.evercraftmc.evercraft.bungee.util.formatting.ComponentFormatter;
import io.github.evercraftmc.evercraft.bungee.util.player.BungeePlayerResolver;
import io.github.evercraftmc.evercraft.shared.util.StringUtils;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import io.github.evercraftmc.evercraft.shared.util.player.SimplePlayer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class UnBanCommand extends BungeeCommand {
    public UnBanCommand(String name, String description, List<String> aliases, String permission) {
        super(name, description, aliases, permission);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        String senderName;
        if (sender instanceof ProxiedPlayer player) {
            senderName = player.getDisplayName();
        } else {
            senderName = "CONSOLE";
        }

        if (args.length == 1) {
            SimplePlayer player = BungeePlayerResolver.getPlayer(BungeeMain.getInstance().getPluginData(), args[0]);

            if (player != null) {
                if (sender instanceof ProxiedPlayer player2 && player2.getUniqueId().equals(player.getUniqueId())) {
                    player2.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().moderation.unban.cantUnbanSelf)));
                } else {
                    if (BungeeMain.getInstance().getPluginData().getBoolean("players." + player.getUniqueId() + ".ban.banned")) {
                        BungeeMain.getInstance().getProxy().broadcast(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().moderation.unban.broadcast.noReason.replace("{player}", player.getDisplayName()).replace("{moderator}", senderName))));

                        BungeeMain.getInstance().getPluginData().set("players." + player.getUniqueId() + ".ban.banned", null);
                        BungeeMain.getInstance().getPluginData().set("players." + player.getUniqueId() + ".ban.reason", null);
                        BungeeMain.getInstance().getPluginData().set("players." + player.getUniqueId() + ".ban.by", null);
                        BungeeMain.getInstance().getPluginData().set("players." + player.getUniqueId() + ".ban.until", null);
                    } else {
                        sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().moderation.unban.notBanned.replace("{player}", player.getDisplayName()))));
                    }
                }
            } else {
                sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().error.playerNotFound.replace("{player}", args[0]))));
            }
        } else if (args.length > 1) {
            SimplePlayer player = BungeePlayerResolver.getPlayer(BungeeMain.getInstance().getPluginData(), args[0]);

            if (player != null) {
                if (sender instanceof ProxiedPlayer player2 && player2.getUniqueId().equals(player.getUniqueId())) {
                    player2.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().moderation.unban.cantUnbanSelf)));
                } else {
                    if (BungeeMain.getInstance().getPluginData().getBoolean("players." + player.getUniqueId() + ".ban.banned")) {
                        StringBuilder reason = new StringBuilder();

                        for (Integer i = 1; i < args.length; i++) {
                            reason.append(args[i] + " ");
                        }

                        BungeeMain.getInstance().getProxy().broadcast(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().moderation.unban.broadcast.reason.replace("{player}", player.getDisplayName()).replace("{moderator}", senderName).replace("{reason}", reason.substring(0, reason.length() - 1)))));

                        BungeeMain.getInstance().getPluginData().set("players." + player.getUniqueId() + ".ban.banned", null);
                        BungeeMain.getInstance().getPluginData().set("players." + player.getUniqueId() + ".ban.reason", null);
                        BungeeMain.getInstance().getPluginData().set("players." + player.getUniqueId() + ".ban.by", null);
                        BungeeMain.getInstance().getPluginData().set("players." + player.getUniqueId() + ".ban.until", null);
                    } else {
                        sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().moderation.unban.notBanned.replace("{player}", player.getDisplayName()))));
                    }
                }
            } else {
                sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().error.playerNotFound.replace("{player}", args[0]))));
            }
        } else {
            sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().error.invalidArgs)));
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