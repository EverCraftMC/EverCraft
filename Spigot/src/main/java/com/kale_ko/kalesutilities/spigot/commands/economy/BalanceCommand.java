package com.kale_ko.kalesutilities.spigot.commands.economy;

import java.util.List;
import com.kale_ko.kalesutilities.spigot.SpigotPlugin;
import com.kale_ko.kalesutilities.spigot.Util;
import com.kale_ko.kalesutilities.spigot.commands.SpigotCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BalanceCommand extends SpigotCommand {
    public BalanceCommand(String name, String description, List<String> aliases, String usage, String permission) {
        super(name, description, aliases, usage, permission);
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (args.length > 0) {
            if (Util.hasPermission(sender, "kalesutilities.commands.staff.sudo")) {
                Player player = SpigotPlugin.Instance.getServer().getPlayer(args[0]);

                if (player != null) {
                    Util.sendMessage(player, SpigotPlugin.Instance.config.getString("config.balance").replace("{balance}", SpigotPlugin.Instance.players.getString(player.getName() + ".balance")));
                } else {
                    Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.playernotfound").replace("{player}", args[0]));
                }
            } else {
                Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.noperms").replace("{permission}", "kalesutilities.commands.staff.sudo"));
            }
        } else {
            if (sender instanceof Player player) {
                Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("config.balance").replace("{balance}", SpigotPlugin.Instance.players.getString(player.getName() + ".balance")));
            } else {
                Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.noconsole"));
            }
        }

        return true;
    }
}