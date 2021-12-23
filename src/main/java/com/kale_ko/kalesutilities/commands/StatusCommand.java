package com.kale_ko.kalesutilities.commands;

import com.kale_ko.kalesutilities.Main;
import com.kale_ko.kalesutilities.Util;
import java.util.List;
import java.util.UUID;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

public class StatusCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (Util.hasPermission(sender, "kalesutilities.setstatus")) {
            if (args.length > 0) {
                if (sender instanceof Player player) {
                    StringBuilder statusMessageBuilder = new StringBuilder();

                    for (Integer i = 0; i < args.length; i++) {
                        statusMessageBuilder.append(args[i] + " ");
                    }

                    String statusMessage = statusMessageBuilder.toString().substring(0, statusMessageBuilder.length() - 1);

                    List<MetadataValue> statusEntityUUID = player.getMetadata("statusEntityUUID");

                    String found = null;
                    for (MetadataValue metadata : statusEntityUUID) {
                        if (metadata.getOwningPlugin() == Main.Instance) {
                            found = metadata.asString();
                        }
                    }

                    if (found == null) {
                        ArmorStand armorstand = (ArmorStand) player.getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
                        armorstand.setCustomName(statusMessage);
                        armorstand.setCustomNameVisible(true);
                        armorstand.setCollidable(false);
                        armorstand.setGravity(false);
                        armorstand.setInvisible(true);
                        armorstand.setInvulnerable(true);
                        armorstand.setPersistent(false);
                        player.setMetadata("statusEntityUUID", new FixedMetadataValue(Main.Instance, armorstand.getUniqueId().toString()));
                    } else {
                        Entity armorstand = Main.Instance.getServer().getEntity(UUID.fromString(found));
                        armorstand.setCustomName(statusMessage);
                    }
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