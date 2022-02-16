package com.kale_ko.kalesutilities.spigot.misc;

import java.util.HashMap;
import java.util.Map;
import com.kale_ko.kalesutilities.spigot.SpigotPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ChestGUI implements Listener {
    private Inventory inventory;

    private Map<Integer, Runnable> slotActions = new HashMap<Integer, Runnable>();

    public ChestGUI(String title, Integer size) {
        this.inventory = Bukkit.createInventory(null, size, title);

        SpigotPlugin.Instance.getServer().getPluginManager().registerEvents(this, SpigotPlugin.Instance);
    }

    public void addItem(Integer slot, ItemStack item) {
        this.inventory.setItem(slot, item);
    }

    public void addItem(Integer slot, ItemStack item, Runnable callback) {
        this.inventory.setItem(slot, item);
        this.slotActions.put(slot, callback);
    }

    public void updateItem(Integer slot, ItemStack item) {
        this.inventory.setItem(slot, item);
    }

    public void removeItem(Integer slot) {
        this.inventory.setItem(slot, new ItemStack(Material.AIR));
        this.slotActions.remove(slot);
    }

    public void open(Player player) {
        player.openInventory(this.inventory);
    }

    public void close(Player player) {
        if (player.getOpenInventory().getTopInventory() == this.inventory) {
            player.closeInventory();
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory() == this.inventory) {
            event.setCancelled(true);

            Integer slot = event.getSlot();
            if (this.slotActions.containsKey(slot)) {
                this.slotActions.get(slot).run();
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getInventory() == this.inventory) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryMove(InventoryMoveItemEvent event) {
        if (event.getSource() == this.inventory || event.getDestination() == this.inventory) {
            event.setCancelled(true);
        }
    }
}