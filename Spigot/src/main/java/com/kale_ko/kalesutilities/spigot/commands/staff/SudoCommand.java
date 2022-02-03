package com.kale_ko.kalesutilities.spigot.commands.staff;

import java.util.List;
import com.kale_ko.kalesutilities.spigot.SpigotPlugin;
import com.kale_ko.kalesutilities.spigot.Util;
import com.kale_ko.kalesutilities.spigot.commands.SpigotCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SudoCommand extends SpigotCommand {
    public SudoCommand(String name, String description, List<String> aliases, String usage, String permission) {
        super(name, description, aliases, usage, permission);
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (args.length > 1) {
            if (args[0].equalsIgnoreCase("*")) {
                StringBuilder sudoMessageBuilder = new StringBuilder();

                for (Integer i = 1; i < args.length; i++) {
                    sudoMessageBuilder.append(args[i] + " ");
                }

                String sudoMessage = sudoMessageBuilder.toString();

                if (sudoMessage.startsWith("/")) {
                    for (Player player : SpigotPlugin.Instance.getServer().getOnlinePlayers()) {
                        SpigotPlugin.Instance.getServer().dispatchCommand(player, sudoMessage.substring(1).replace("{player}", player.getName()));
                    }

                    Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.sudocommand").replace("{player}", args[0]).replace("{command}", sudoMessage));
                } else {
                    for (Player player : SpigotPlugin.Instance.getServer().getOnlinePlayers()) {
                        player.chat(sudoMessage.replace("{player}", player.getName()));
                    }

                    Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.sudomessage").replace("{player}", args[0]).replace("{message}", sudoMessage));
                }
            } else {
                Player sudoedPlayer = SpigotPlugin.Instance.getServer().getPlayer(args[0]);

                if (sudoedPlayer != null) {
                    StringBuilder sudoMessageBuilder = new StringBuilder();

                    for (Integer i = 1; i < args.length; i++) {
                        sudoMessageBuilder.append(args[i] + " ");
                    }

                    String sudoMessage = sudoMessageBuilder.toString();

                    if (sudoMessage.startsWith("/")) {
                        SpigotPlugin.Instance.getServer().dispatchCommand(sudoedPlayer, sudoMessage.substring(1));

                        Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.sudocommand").replace("{player}", args[0]).replace("{command}", sudoMessage));
                    } else {
                        sudoedPlayer.chat(sudoMessage);

                        Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.sudomessage").replace("{player}", args[0]).replace("{message}", sudoMessage));
                    }
                } else {
                    Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.playernotfound").replace("{player}", args[0]));
                }
            }
        } else {
            Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.usage").replace("{usage}", SpigotPlugin.Instance.getCommand("sudo").getUsage()));
        }

        return true;
    }
}