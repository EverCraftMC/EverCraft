package com.kale_ko.kalesutilities.commands.staff;

import com.kale_ko.kalesutilities.Main;
import com.kale_ko.kalesutilities.Util;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SudoCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (args.length > 1) {
            if (args[0].equalsIgnoreCase("*")) {
                StringBuilder sudoMessageBuilder = new StringBuilder();

                for (Integer i = 1; i < args.length; i++) {
                    sudoMessageBuilder.append(args[i] + " ");
                }

                String sudoMessage = sudoMessageBuilder.toString();

                if (sudoMessage.startsWith("/")) {
                    for (Player player : Main.Instance.getServer().getOnlinePlayers()) {
                        Main.Instance.getServer().dispatchCommand(player, sudoMessage.substring(1).replace("{player}", player.getName()));
                    }

                    Util.sendMessage(sender, Main.Instance.config.getString("messages.sudocommand").replace("{player}", args[0]).replace("{command}", sudoMessage));
                } else {
                    for (Player player : Main.Instance.getServer().getOnlinePlayers()) {
                        player.chat(sudoMessage.replace("{player}", player.getName()));
                    }

                    Util.sendMessage(sender, Main.Instance.config.getString("messages.sudomessage").replace("{player}", args[0]).replace("{message}", sudoMessage));
                }
            } else {
                Player sudoedPlayer = Main.Instance.getServer().getPlayer(args[0]);

                if (sudoedPlayer != null) {
                    StringBuilder sudoMessageBuilder = new StringBuilder();

                    for (Integer i = 1; i < args.length; i++) {
                        sudoMessageBuilder.append(args[i] + " ");
                    }

                    String sudoMessage = sudoMessageBuilder.toString();

                    if (sudoMessage.startsWith("/")) {
                        Main.Instance.getServer().dispatchCommand(sudoedPlayer, sudoMessage.substring(1));

                        Util.sendMessage(sender, Main.Instance.config.getString("messages.sudocommand").replace("{player}", args[0]).replace("{command}", sudoMessage));
                    } else {
                        sudoedPlayer.chat(sudoMessage);

                        Util.sendMessage(sender, Main.Instance.config.getString("messages.sudomessage").replace("{player}", args[0]).replace("{message}", sudoMessage));
                    }
                } else {
                    Util.sendMessage(sender, Main.Instance.config.getString("messages.playernotfound").replace("{player}", args[0]));
                }
            }
        } else {
            Util.sendMessage(sender, Main.Instance.config.getString("messages.usage").replace("{usage}", Main.Instance.getCommand("sudo").getUsage()));
        }

        return true;
    }
}