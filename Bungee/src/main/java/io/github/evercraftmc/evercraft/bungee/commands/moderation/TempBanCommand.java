package io.github.evercraftmc.evercraft.bungee.commands.moderation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import io.github.evercraftmc.evercraft.bungee.BungeeMain;
import io.github.evercraftmc.evercraft.bungee.commands.BungeeCommand;
import io.github.evercraftmc.evercraft.bungee.util.formatting.ComponentFormatter;
import io.github.evercraftmc.evercraft.shared.util.StringUtils;
import io.github.evercraftmc.evercraft.shared.util.TimeUtil;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import io.github.evercraftmc.evercraft.shared.util.player.PlayerResolver;
import io.github.evercraftmc.evercraft.shared.util.player.SimplePlayer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class TempBanCommand extends BungeeCommand {
    public TempBanCommand(String name, String description, List<String> aliases, String permission) {
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

        if (args.length == 2) {
            SimplePlayer player = PlayerResolver.getPlayer(BungeeMain.getInstance().getData(), args[0]);

            if (player != null) {
                if (!BungeeMain.getInstance().getData().getBoolean("players." + player.getUniqueId() + ".ban.banned")) {
                    BungeeMain.getInstance().getProxy().broadcast(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("moderation.ban.brodcast.noreason").replace("{player}", player.getDisplayName()).replace("{moderator}", senderName).replace("{time}", args[1]))));

                    BungeeMain.getInstance().getData().set("players." + player.getUniqueId() + ".ban.banned", true);
                    BungeeMain.getInstance().getData().set("players." + player.getUniqueId() + ".ban.reason", null);
                    BungeeMain.getInstance().getData().set("players." + player.getUniqueId() + ".ban.by", senderName);
                    BungeeMain.getInstance().getData().set("players." + player.getUniqueId() + ".ban.until", TimeUtil.parseFuture(args[1]));

                    ProxiedPlayer bungeeplayer = BungeeMain.getInstance().getProxy().getPlayer(args[0]);
                    if (bungeeplayer != null) {
                        bungeeplayer.disconnect(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("moderation.ban.noreason").replace("{moderator}", senderName).replace("{time}", args[1]))));
                    }
                } else {
                    sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("moderation.ban.alreadybanned").replace("{player}", player.getDisplayName()))));
                }
            } else {
                sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("error.playerNotFound").replace("{player}", args[0]))));
            }
        } else if (args.length > 2) {
            SimplePlayer player = PlayerResolver.getPlayer(BungeeMain.getInstance().getData(), args[0]);

            if (player != null) {
                if (!BungeeMain.getInstance().getData().getBoolean("players." + player.getUniqueId() + ".ban.banned")) {
                    StringBuilder reason = new StringBuilder();

                    for (Integer i = 1; i < args.length; i++) {
                        reason.append(args[i] + " ");
                    }

                    BungeeMain.getInstance().getProxy().broadcast(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("moderation.ban.brodcast.reason").replace("{player}", player.getDisplayName()).replace("{moderator}", senderName).replace("{reason}", reason.substring(0, reason.length() - 1)).replace("{time}", args[1]))));

                    BungeeMain.getInstance().getData().set("players." + player.getUniqueId() + ".ban.banned", true);
                    BungeeMain.getInstance().getData().set("players." + player.getUniqueId() + ".ban.reason", reason.substring(0, reason.length() - 1));
                    BungeeMain.getInstance().getData().set("players." + player.getUniqueId() + ".ban.by", senderName);
                    BungeeMain.getInstance().getData().set("players." + player.getUniqueId() + ".ban.until", TimeUtil.parseFuture(args[1]));

                    ProxiedPlayer bungeeplayer = BungeeMain.getInstance().getProxy().getPlayer(args[0]);
                    if (bungeeplayer != null) {
                        bungeeplayer.disconnect(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("moderation.ban.reason").replace("{moderator}", senderName).replace("{reason}", reason.substring(0, reason.length() - 1)).replace("{time}", args[1]))));
                    }
                } else {
                    sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("moderation.ban.alreadybanned").replace("{player}", player.getDisplayName()))));
                }
            } else {
                sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("error.playerNotFound").replace("{player}", args[0]))));
            }
        } else {
            sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("error.invalidArgs"))));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<String>();

        if (args.length == 1) {
            for (ProxiedPlayer player : BungeeMain.getInstance().getProxy().getPlayers()) {
                list.add(player.getName());
            }
        } else if (args.length == 2) {
            list.add("1m");
            list.add("5m");
            list.add("10m");
            list.add("15m");
            list.add("30m");
            list.add("1h");
            list.add("2h");
            list.add("6h");
            list.add("12h");
            list.add("1d");
            list.add("2d");
            list.add("3d");
            list.add("1w");
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