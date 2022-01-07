package com.kale_ko.kalesutilities.commands.kits;

import com.kale_ko.kalesutilities.Main;
import com.kale_ko.kalesutilities.Util;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class KitCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (Util.hasPermission(sender, "kalesutilities.kit")) {
            if (args.length > 0) {
                if (sender instanceof Player player) {
                    List<String> items = Main.Instance.kits.getConfig().getStringList(args[0]);

                    for (String item : items) {
                        ItemStack itemStack = new ItemStack(Material.matchMaterial(item), 1);

                        if (itemStack.getType().getEquipmentSlot() != EquipmentSlot.HAND && itemStack.getType().getEquipmentSlot() != EquipmentSlot.OFF_HAND && player.getEquipment().getItem(itemStack.getType().getEquipmentSlot()).getType().isAir()) {
                            player.getEquipment().setItem(itemStack.getType().getEquipmentSlot(), itemStack);
                        } else {
                            player.getInventory().addItem(itemStack);
                        }
                    }

                    Util.sendMessage(player, Main.Instance.config.getConfig().getString("message.kit").replace("{kit}", args[0]));
                } else {
                    Util.sendMessage(sender, Main.Instance.config.getConfig().getString("messages.noconsole"));
                }
            } else {
                Util.sendMessage(sender, Main.Instance.config.getConfig().getString("messages.usage").replace("{usage}", Main.Instance.getCommand("kit").getUsage()));
            }
        } else {
            Util.sendMessage(sender, Main.Instance.config.getConfig().getString("messages.noperms").replace("{permission}", "kalesutilities.kit"));
        }

        return true;
    }
}