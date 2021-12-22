package com.kale_ko.kalesutilities.commands;

import com.kale_ko.kalesutilities.Main;
import com.kale_ko.kalesutilities.Util;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class StatusCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (Util.hasPermission(sender, "kalesutilities.setstatus")) {
            if (args.length > 0) {
                if (sender instanceof Player player) {
                    StringBuilder statusMessageBuilder = new StringBuilder();

                    for (Integer i = 1; i < args.length; i++) {
                        statusMessageBuilder.append(args[i] + " ");
                    }

                    String statusMessage = statusMessageBuilder.toString();

                    ArmorStand armorstand = (ArmorStand) player.getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
                    armorstand.setAI(false);
                    armorstand.setCollidable(false);
                    armorstand.setGravity(false);
                    armorstand.setInvisible(true);
                    armorstand.setInvulnerable(true);
                    armorstand.setMarker(true);
                    armorstand.setPersistent(false);
                    armorstand.setRemoveWhenFarAway(false);
                    armorstand.setCustomName(statusMessage);
                    armorstand.setCustomNameVisible(true);
                    player.setMetadata("statusEntityUUID", new FixedMetadataValue(Main.Instance, armorstand.getUniqueId().toString()));
                } else {
                    Util.sendMessage(sender, Main.Instance.config.getString("messages.noconsole"));
                }
            } else {
                Util.sendMessage(sender, Main.Instance.config.getString("messages.usage").replace("{usage}", Main.Instance.getCommand("status").getUsage()));
            }
        } else {
            Util.sendMessage(sender, Main.Instance.config.getString("messages.noperms").replace("{permission}", "kalesutilities.setstatus"));
        }

        return true;
    }
}