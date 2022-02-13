package com.kale_ko.kalesutilities.spigot.commands.info;

import java.util.List;
import com.kale_ko.kalesutilities.spigot.SpigotPlugin;
import com.kale_ko.kalesutilities.spigot.Util;
import com.kale_ko.kalesutilities.spigot.commands.SpigotCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ListCommand extends SpigotCommand {
    public ListCommand(String name, String description, List<String> aliases, String usage, String permission) {
        super(name, description, aliases, usage, permission);
    }

    @Override
    public void run(CommandSender sender, String label, String[] args) {
        if (args.length > 0) {
            Player player = SpigotPlugin.Instance.getServer().getPlayer(args[0]);

            if (player != null) {
                StringBuilder list = new StringBuilder();

                for (Player player2 : SpigotPlugin.Instance.getServer().getOnlinePlayers()) {
                    list.append(Util.getPlayerName(player2) + ", ");
                }

                Util.sendMessage(player, SpigotPlugin.Instance.config.getString("messages.list").replace("{playerList}", list.toString().substring(0, list.length() - 2)));
            } else {
                Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.playernotfound").replace("{player}", args[0]));
            }
        } else {
            StringBuilder list = new StringBuilder();

            for (Player player : SpigotPlugin.Instance.getServer().getOnlinePlayers()) {
                list.append(Util.getPlayerName(player) + ", ");
            }

            Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.list").replace("{playerList}", list.toString().substring(0, list.length() - 2)));
        }
    }
}