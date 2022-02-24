package com.kale_ko.kalesutilities.spigot.misc;

import com.kale_ko.kalesutilities.shared.util.ParamRunnable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

public class ImmovableItemAction extends ItemAction {
    public ImmovableItemAction(ItemStack item, ParamRunnable action) {
        super(item, action);
    }

    @EventHandler
    public void onItemClick(InventoryClickEvent event) {
        if (event.getCurrentItem() != null && event.getCurrentItem().getItemMeta() != null && event.getCurrentItem().getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemDrag(InventoryDragEvent event) {
        if ((event.getCursor() != null && event.getCursor().getItemMeta() != null && event.getCursor().getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName())) || (event.getOldCursor() != null && event.getOldCursor().getItemMeta() != null && event.getOldCursor().getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName()))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemMove(InventoryMoveItemEvent event) {
        if (event.getItem() != null && event.getItem().getItemMeta() != null && event.getItem().getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if (event.getItemDrop() != null && event.getItemDrop().getItemStack() != null && event.getItemDrop().getItemStack().getItemMeta() != null && event.getItemDrop().getItemStack().getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemSwap(PlayerSwapHandItemsEvent event) {
        if (event.getMainHandItem() != null && event.getMainHandItem().getItemMeta() != null && event.getMainHandItem().getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName())) {
            event.setCancelled(true);
        }
    }
}