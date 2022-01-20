package com.kale_ko.kalesutilities.commands.kits;

import com.kale_ko.kalesutilities.Main;
import com.kale_ko.kalesutilities.Util;
import java.util.List;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class KitCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (args.length > 1) {
            if (Util.hasPermission(sender, "kalesutilities.commands.staff.sudo")) {
                Player player = Main.Instance.getServer().getPlayer(args[0]);

                if (player != null) {
                    List<String> items = Main.Instance.kits.getStringList(args[0]);
    
                    for (String item : items) {
                        NBTTagCompound nbt = Util.parseNBT(item);
                        ItemStack itemStack = CraftItemStack.asBukkitCopy(net.minecraft.world.item.ItemStack.a(nbt));
    
                        if (itemStack.getType().getEquipmentSlot() != EquipmentSlot.HAND && itemStack.getType().getEquipmentSlot() != EquipmentSlot.OFF_HAND && (player.getEquipment().getItem(itemStack.getType().getEquipmentSlot()) == null || player.getEquipment().getItem(itemStack.getType().getEquipmentSlot()).getType().isAir())) {
                            player.getEquipment().setItem(itemStack.getType().getEquipmentSlot(), itemStack);
                        } else {
                            player.getInventory().addItem(itemStack);
                        }
                    }

                    Util.sendMessage(player, Main.Instance.config.getString("messages.kit").replace("{kit}", args[1]));
                } else {
                    Util.sendMessage(sender, Main.Instance.config.getString("messages.playernotfound").replace("{player}", args[0]));
                }
            } else {
                Util.sendMessage(sender, Main.Instance.config.getString("messages.noperms").replace("{permission}", "kalesutilities.commands.staff.sudo"));
            }
        } else if (args.length > 0) {
            if (sender instanceof Player player) {
                List<String> items = Main.Instance.kits.getStringList(args[0]);

                for (String item : items) {
                    NBTTagCompound nbt = Util.parseNBT(item);
                    ItemStack itemStack = CraftItemStack.asBukkitCopy(net.minecraft.world.item.ItemStack.a(nbt));

                    if (itemStack.getType().getEquipmentSlot() != EquipmentSlot.HAND && itemStack.getType().getEquipmentSlot() != EquipmentSlot.OFF_HAND && (player.getEquipment().getItem(itemStack.getType().getEquipmentSlot()) == null || player.getEquipment().getItem(itemStack.getType().getEquipmentSlot()).getType().isAir())) {
                        player.getEquipment().setItem(itemStack.getType().getEquipmentSlot(), itemStack);
                    } else {
                        player.getInventory().addItem(itemStack);
                    }
                }

                Util.sendMessage(player, Main.Instance.config.getString("messages.kit").replace("{kit}", args[0]));
            } else {
                Util.sendMessage(sender, Main.Instance.config.getString("messages.noconsole"));
            }
        } else {
            Util.sendMessage(sender, Main.Instance.config.getString("messages.usage").replace("{usage}", Main.Instance.getCommand("kit").getUsage()));
        }

        return true;
    }
}