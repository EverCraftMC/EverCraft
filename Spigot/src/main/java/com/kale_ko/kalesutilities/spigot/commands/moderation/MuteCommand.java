package com.kale_ko.kalesutilities.spigot.commands.moderation;

import java.util.List;
import com.kale_ko.kalesutilities.spigot.SpigotPlugin;
import com.kale_ko.kalesutilities.spigot.Util;
import com.kale_ko.kalesutilities.spigot.commands.SpigotCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MuteCommand extends SpigotCommand {
    public MuteCommand(String name, String description, List<String> aliases, String usage, String permission) {
        super(name, description, aliases, usage, permission);
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (args.length > 1) {
            Player player = SpigotPlugin.Instance.getServer().getPlayer(args[0]);

            StringBuilder muteMessageBuilder = new StringBuilder();

            for (Integer i = 1; i < args.length; i++) {
                muteMessageBuilder.append(args[i] + " ");
            }

            String muteMessage = muteMessageBuilder.toString();

            SpigotPlugin.Instance.players.set(player.getPlayer().getName() + ".muted", true);
            SpigotPlugin.Instance.players.set(player.getPlayer().getName() + ".mutedMessage", SpigotPlugin.Instance.config.getString("messages.mute").replace("{player}", "You").replace("{moderator}", Util.getPlayerName(sender)).replace("{reason}", muteMessage).replace("was", "are"));

            if (SpigotPlugin.Instance.getServer().getPlayer(args[0]) != null) {
                Util.broadcastMessage(SpigotPlugin.Instance.config.getString("messages.mute").replace("{player}", Util.getPlayerName(SpigotPlugin.Instance.getServer().getPlayer(args[0]))).replace("{moderator}", Util.getPlayerName(sender)).replace("{reason}", muteMessage));
            } else {
                Util.broadcastMessage(SpigotPlugin.Instance.config.getString("messages.mute").replace("{player}", args[0]).replace("{moderator}", Util.getPlayerName(sender)).replace("{reason}", muteMessage));
            }
        }

        return true;
    }
}