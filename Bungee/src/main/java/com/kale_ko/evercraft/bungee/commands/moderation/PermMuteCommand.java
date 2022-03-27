package com.kale_ko.evercraft.bungee.commands.moderation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.kale_ko.evercraft.bungee.BungeeMain;
import com.kale_ko.evercraft.bungee.commands.BungeeCommand;
import com.kale_ko.evercraft.shared.util.StringUtils;
import com.kale_ko.evercraft.shared.util.formatting.TextFormatter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PermMuteCommand extends BungeeCommand {
    public static final String name = "permmute";
    public static final String description = "Permanently mute a player on the server";

    public static final List<String> aliases = Arrays.asList();

    public static final String permission = "evercraft.commands.moderation.permmute";

    @Override
    public void run(CommandSender sender, String[] args) {
        String senderName;
        if (sender instanceof ProxiedPlayer player) {
            senderName = player.getDisplayName();
        } else {
            senderName = "CONSOLE";
        }

        if (args.length == 1) {
            ProxiedPlayer player = BungeeMain.getInstance().getProxy().getPlayer(args[0]);

            if (player != null) {
                BungeeMain.getInstance().getProxy().broadcast(TextComponent.fromLegacyText(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("moderation.mute.brodcast.noreason").replace("{player}", player.getDisplayName()).replace("{moderator}", senderName).replace("{time}", "forever"))));

                BungeeMain.getInstance().getData().set("players." + player.getUniqueId() + ".mute.muted", true);
                BungeeMain.getInstance().getData().set("players." + player.getUniqueId() + ".mute.reason", null);
                BungeeMain.getInstance().getData().set("players." + player.getUniqueId() + ".ban.by", senderName);
                BungeeMain.getInstance().getData().set("players." + player.getUniqueId() + ".mute.until", "forever");
            }
        } else if (args.length > 1) {
            ProxiedPlayer player = BungeeMain.getInstance().getProxy().getPlayer(args[0]);

            if (player != null) {
                StringBuilder reason = new StringBuilder();

                for (Integer i = 1; i < args.length; i++) {
                    reason.append(args[i] + " ");
                }

                BungeeMain.getInstance().getProxy().broadcast(TextComponent.fromLegacyText(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("moderation.mute.brodcast.reason").replace("{player}", player.getDisplayName()).replace("{moderator}", senderName).replace("{reason}", reason.substring(0, reason.length() - 1)).replace("{time}", "forever"))));

                BungeeMain.getInstance().getData().set("players." + player.getUniqueId() + ".mute.muted", true);
                BungeeMain.getInstance().getData().set("players." + player.getUniqueId() + ".mute.reason", reason.substring(0, reason.length() - 1));
                BungeeMain.getInstance().getData().set("players." + player.getUniqueId() + ".ban.by", senderName);
                BungeeMain.getInstance().getData().set("players." + player.getUniqueId() + ".mute.until", "forever");
            }
        } else {
            sender.sendMessage(TextComponent.fromLegacyText(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("error.invalidArgs"))));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<String>();

        if (args.length == 0) {
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