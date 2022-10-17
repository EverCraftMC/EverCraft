package io.github.evercraftmc.evercraft.spigot.listeners;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.item.ItemStack;

public class CreativeItemListener extends SpigotListener {
    @EventHandler
    public void onItemCreate(InventoryClickEvent event) {
        ItemStack itemStack = CraftItemStack.asNMSCopy(event.getCursor());
        NBTTagCompound nbtCompound = itemStack.v();

        if (nbtCompound.e("BlockTag") || nbtCompound.e("EntityTag") || nbtCompound.e("BlockState")) {
            event.setCancelled(true);

            event.getCursor().setType(Material.AIR);
        }
    }

    @EventHandler
    public void onItemCreate(InventoryMoveItemEvent event) {
        ItemStack itemStack = CraftItemStack.asNMSCopy(event.getItem());
        NBTTagCompound nbtCompound = itemStack.v();

        if (nbtCompound.e("BlockTag") || nbtCompound.e("EntityTag") || nbtCompound.e("BlockState")) {
            event.setCancelled(true);

            event.getItem().setType(Material.AIR);
        }
    }

    @EventHandler
    public void onItemCreate(ItemSpawnEvent event) {
        ItemStack itemStack = CraftItemStack.asNMSCopy(event.getEntity().getItemStack());
        NBTTagCompound nbtCompound = itemStack.v();

        if (nbtCompound.e("BlockTag") || nbtCompound.e("EntityTag") || nbtCompound.e("BlockState")) {
            event.setCancelled(true);

            event.getEntity().remove();
        }
    }

    @EventHandler
    public void onItemCreate(PlayerDropItemEvent event) {
        ItemStack itemStack = CraftItemStack.asNMSCopy(event.getItemDrop().getItemStack());
        NBTTagCompound nbtCompound = itemStack.v();

        if (nbtCompound.e("BlockTag") || nbtCompound.e("EntityTag") || nbtCompound.e("BlockState")) {
            event.setCancelled(true);

            event.getItemDrop().remove();
        }
    }

    @EventHandler
    public void onItemCreate(InventoryPickupItemEvent event) {
        ItemStack itemStack = CraftItemStack.asNMSCopy(event.getItem().getItemStack());
        NBTTagCompound nbtCompound = itemStack.v();

        if (nbtCompound.e("BlockTag") || nbtCompound.e("EntityTag") || nbtCompound.e("BlockState")) {
            event.setCancelled(true);

            event.getItem().remove();
        }
    }

    @EventHandler
    public void onItemCreate(PlayerItemHeldEvent event) {
        ItemStack itemStack = CraftItemStack.asNMSCopy(event.getPlayer().getInventory().getItem(event.getNewSlot()));
        NBTTagCompound nbtCompound = itemStack.v();

        if (nbtCompound.e("BlockTag") || nbtCompound.e("EntityTag") || nbtCompound.e("BlockState")) {
            event.setCancelled(true);

            event.getPlayer().getInventory().setItem(event.getNewSlot(), null);
        }
    }

    @EventHandler
    public void onItemCreate(PlayerInteractEvent event) {
        if ((event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) && event.getItem() != null) {
            ItemStack itemStack = CraftItemStack.asNMSCopy(event.getItem());
            NBTTagCompound nbtCompound = itemStack.v();

            if (nbtCompound.e("BlockTag") || nbtCompound.e("EntityTag") || nbtCompound.e("BlockState")) {
                event.getItem().setType(Material.AIR);

                event.setCancelled(true);
            }
        }
    }
}