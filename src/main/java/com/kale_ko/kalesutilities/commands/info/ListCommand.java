package com.kale_ko.kalesutilities.commands.info;

import com.kale_ko.kalesutilities.Main;
import com.kale_ko.kalesutilities.Util;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ListCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (args.length > 0) {
            Player player = Main.Instance.getServer().getPlayer(args[0]);

            if (player != null) {
                StringBuilder list = new StringBuilder();
    
                for (Player player2 : Main.Instance.getServer().getOnlinePlayers()) {
                    list.append(Util.getPlayerName(player2) + ", ");
                }
    
                Util.sendMessage(player, Main.Instance.config.getString("messages.list").replace("{playerList}", list.toString().substring(list.length() - 2)));
            } else {
                Util.sendMessage(sender, Main.Instance.config.getString("messages.playernotfound").replace("{player}", args[0]));
            }
        } else {
            StringBuilder list = new StringBuilder();

            for (Player player : Main.Instance.getServer().getOnlinePlayers()) {
                list.append(Util.getPlayerName(player) + ", ");
            }

            Util.sendMessage(sender, Main.Instance.config.getString("messages.list").replace("{playerList}", list.toString().substring(list.length() - 2)));
        }

        return true;
    }
}