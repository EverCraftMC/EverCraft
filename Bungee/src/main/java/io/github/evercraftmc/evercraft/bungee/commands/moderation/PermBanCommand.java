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

public class PermBanCommand extends BungeeCommand {
    public PermBanCommand(String name, String description, List<String> aliases, String permission) {
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
                if (!BungeeMain.getInstance().getPluginData().getBoolean("players." + player.getUniqueId() + ".ban.banned")) {
                    if (sender instanceof ProxiedPlayer player2 && player2.getUniqueId().equals(player.getUniqueId())) {
                        player2.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().moderation.ban.cantBanSelf)));
                    } else {
                        BungeeMain.getInstance().getProxy().broadcast(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().moderation.ban.broadcast.noReason.replace("{player}", player.getDisplayName()).replace("{moderator}", senderName).replace("{time}", "forever"))));

                        BungeeMain.getInstance().getPluginData().set("players." + player.getUniqueId() + ".ban.banned", true);
                        BungeeMain.getInstance().getPluginData().set("players." + player.getUniqueId() + ".ban.reason", null);
                        BungeeMain.getInstance().getPluginData().set("players." + player.getUniqueId() + ".ban.by", senderName);
                        BungeeMain.getInstance().getPluginData().set("players." + player.getUniqueId() + ".ban.until", "forever");

                        ProxiedPlayer bungeeplayer = BungeeMain.getInstance().getProxy().getPlayer(args[0]);
                        if (bungeeplayer != null) {
                            bungeeplayer.disconnect(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().moderation.ban.noReason.replace("{moderator}", senderName).replace("{time}", "forever"))));
                        }
                    }
                } else {
                    sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().moderation.ban.alreadyBanned.replace("{player}", player.getDisplayName()))));
                }
            } else {
                sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().error.playerNotFound.replace("{player}", args[0]))));
            }
        } else if (args.length > 1) {
            SimplePlayer player = BungeePlayerResolver.getPlayer(BungeeMain.getInstance().getPluginData(), args[0]);

            if (player != null) {
                if (!BungeeMain.getInstance().getPluginData().getBoolean("players." + player.getUniqueId() + ".ban.banned")) {
                    StringBuilder reason = new StringBuilder();

                    Boolean confirm = false;

                    for (Integer i = 1; i < args.length; i++) {
                        if (args[i].equalsIgnoreCase("--confirm")) {
                            confirm = true;
                        } else {
                            reason.append(args[i] + " ");
                        }
                    }

                    if (sender instanceof ProxiedPlayer player2 && player2.getUniqueId().equals(player.getUniqueId()) && !confirm) {
                        player2.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().moderation.ban.cantBanSelf)));
                    } else {
                        BungeeMain.getInstance().getProxy().broadcast(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().moderation.ban.broadcast.reason.replace("{player}", player.getDisplayName()).replace("{moderator}", senderName).replace("{reason}", reason.substring(0, reason.length() - 1)).replace("{time}", "forever"))));

                        BungeeMain.getInstance().getPluginData().set("players." + player.getUniqueId() + ".ban.banned", true);
                        BungeeMain.getInstance().getPluginData().set("players." + player.getUniqueId() + ".ban.reason", reason.substring(0, reason.length() - 1));
                        BungeeMain.getInstance().getPluginData().set("players." + player.getUniqueId() + ".ban.by", senderName);
                        BungeeMain.getInstance().getPluginData().set("players." + player.getUniqueId() + ".ban.until", "forever");

                        ProxiedPlayer bungeeplayer = BungeeMain.getInstance().getProxy().getPlayer(args[0]);
                        if (bungeeplayer != null) {
                            bungeeplayer.disconnect(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().moderation.ban.reason.replace("{moderator}", senderName).replace("{reason}", reason.substring(0, reason.length() - 1)).replace("{time}", "forever"))));
                        }
                    }
                } else {
                    sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().moderation.ban.alreadyBanned.replace("{player}", player.getDisplayName()))));
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