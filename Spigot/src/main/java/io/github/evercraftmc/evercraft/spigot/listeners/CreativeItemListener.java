package io.github.evercraftmc.evercraft.spigot.listeners;

import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.item.ItemStack;

public class CreativeItemListener extends SpigotListener {
    @EventHandler
    public void onItemCreate(InventoryCreativeEvent event) {
        ItemStack itemStack = CraftItemStack.asNMSCopy(event.getCurrentItem());
        NBTTagCompound nbtCompound = itemStack.u();

        if (nbtCompound.b("BlockTag") || nbtCompound.b("EntityTag") || nbtCompound.b("BlockState")) {
            event.setCancelled(true);
        }
    }
}