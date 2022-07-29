package io.github.evercraftmc.evercraft.spigot.util.bukkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import io.github.evercraftmc.evercraft.spigot.listeners.SpigotListener;
import io.github.evercraftmc.evercraft.spigot.util.formatting.ComponentFormatter;

public class ChestGUI extends SpigotListener {
    private Inventory inv;

    private Boolean lockSlots;
    private Boolean lockEmptySlots;

    private List<Integer> lockedSlots = new ArrayList<Integer>();

    private Map<Integer, Consumer<Player>> slotCallbacks = new HashMap<Integer, Consumer<Player>>();

    public ChestGUI(String title, Integer rows, Boolean lockSlots, Boolean lockEmptySlots) {
        this.inv = Bukkit.createInventory(null, rows * 9, ComponentFormatter.stringToComponent(title));

        this.register();

        this.lockSlots = lockSlots;
        this.lockEmptySlots = lockEmptySlots;
    }

    public ChestGUI(String title, Integer rows) {
        this(title, rows, true, true);
    }

    public ItemStack[] getContents() {
        return this.inv.getContents();
    }

    public Integer getSize() {
        return this.inv.getSize();
    }

    public Integer getRows() {
        return this.inv.getSize() / 9;
    }

    public Boolean getLockSlots() {
        return this.lockSlots;
    }

    public void setLockSlots(Boolean value) {
        this.lockSlots = value;
    }

    public Boolean getLockEmptySlots() {
        return this.lockEmptySlots;
    }

    public void setLockEmptySlots(Boolean value) {
        this.lockEmptySlots = value;
    }

    public void addItem(ItemStack stack, Integer slot) {
        this.inv.setItem(slot, stack);

        this.lockedSlots.add(slot);
    }

    public void addItem(ItemStack stack, Integer slot, Consumer<Player> callbacks) {
        this.addItem(stack, slot);

        this.lockedSlots.add(slot);

        this.slotCallbacks.put(slot, callbacks);
    }

    public void removeItem(Integer slot) {
        this.inv.setItem(slot, new ItemStack(Material.AIR));

        if (this.lockedSlots.contains(slot)) {
            this.lockedSlots.remove(slot);
        }

        if (this.slotCallbacks.containsKey(slot)) {
            this.slotCallbacks.remove(slot);
        }
    }

    public void open(Player player) {
        player.openInventory(inv);
    }

    public void close(Player player) {
        if (player.getOpenInventory().getTopInventory() == inv || player.getOpenInventory().getBottomInventory() == inv) {
            player.closeInventory();
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().equals(this.inv)) {
            if (this.slotCallbacks.containsKey(event.getSlot())) {
                if (this.lockSlots) {
                    event.setCancelled(true);
                }

                HumanEntity clicker = event.getWhoClicked();

                if (clicker instanceof Player player) {
                    this.slotCallbacks.get(event.getSlot()).accept(player);
                }
            } else if ((this.lockSlots && this.lockedSlots.contains(event.getSlot())) || this.lockEmptySlots) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getInventory().equals(this.inv) && this.lockEmptySlots) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryMove(InventoryMoveItemEvent event) {
        if ((event.getSource() == this.inv || event.getDestination() == this.inv) && this.lockEmptySlots) {
            event.setCancelled(true);
        }
    }
}