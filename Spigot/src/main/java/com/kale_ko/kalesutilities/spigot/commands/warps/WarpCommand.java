package com.kale_ko.kalesutilities.spigot.commands.warps;

import java.util.List;
import com.kale_ko.kalesutilities.spigot.SpigotPlugin;
import com.kale_ko.kalesutilities.spigot.Util;
import com.kale_ko.kalesutilities.spigot.commands.SpigotCommand;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class WarpCommand extends SpigotCommand {
    public WarpCommand(String name, String description, List<String> aliases, String usage, String permission) {
        super(name, description, aliases, usage, permission);
    }

    @Override
    public void run(CommandSender sender, String label, String[] args) {
        if (args.length == 1 && sender instanceof Player player) {
            player.teleport(SpigotPlugin.Instance.warps.getSerializable(args[0], Location.class));

            Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.warped").replace("{warp}", args[0]));
        } else if (args.length == 1) {
            Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.noconsole"));
        } else {
            if (Util.hasPermission(sender, "kalesutilities.commands.staff.sudo")) {
                Player player = SpigotPlugin.Instance.getServer().getPlayer(args[0]);

                if (player != null) {
                    player.teleport(SpigotPlugin.Instance.warps.getSerializable(args[1], Location.class));
                    player.getInventory().clear();
                    for (PotionEffect effect : player.getActivePotionEffects()) {
                        player.removePotionEffect(effect.getType());
                    }

                    Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.warpedplayer").replace("{warp}", args[1]).replace("{player}", args[0]));
                    Util.sendMessage(player, SpigotPlugin.Instance.config.getString("messages.warped").replace("{warp}", args[1]));
                } else {
                    Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.playernotfound").replace("{player}", args[0]));
                }
            } else {
                Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.noperms").replace("{permission}", "kalesutilities.commands.staff.sudo"));
            }
        }
    }
}