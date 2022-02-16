package com.kale_ko.kalesutilities.spigot.misc;

import com.kale_ko.kalesutilities.shared.util.ParamRunnable;
import com.kale_ko.kalesutilities.spigot.SpigotPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ItemAction implements Listener {
    public ItemStack item;

    private ParamRunnable action;

    public ItemAction(ItemStack item, ParamRunnable action) {
        this.item = item;

        this.action = action;

        SpigotPlugin.Instance.getServer().getPluginManager().registerEvents(this, SpigotPlugin.Instance);
    }

    @EventHandler
    public void onInventoryClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (event.getItem() != null && event.getItem().getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName())) {
                action.init(event.getPlayer());
                action.run();

                event.setCancelled(true);
            }
        }
    }
}