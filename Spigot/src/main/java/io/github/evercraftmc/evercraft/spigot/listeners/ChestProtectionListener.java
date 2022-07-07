package io.github.evercraftmc.evercraft.spigot.listeners;

import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import io.github.evercraftmc.evercraft.spigot.SpigotMain;
import io.github.evercraftmc.evercraft.spigot.util.formatting.ComponentFormatter;

public class ChestProtectionListener extends SpigotListener {
    @EventHandler
    public void onChestOpen(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && !event.getPlayer().isSneaking() && SpigotMain.getInstance().getPluginConfig().getStringList("protectable").contains(event.getClickedBlock().getType().toString())) {
            if (SpigotMain.getInstance().getChests().getBoolean(event.getClickedBlock().getX() + "," + event.getClickedBlock().getY() + "," + event.getClickedBlock().getZ() + "," + event.getClickedBlock().getWorld().getName() + ".protected") && !SpigotMain.getInstance().getChests().getString(event.getClickedBlock().getX() + "," + event.getClickedBlock().getY() + "," + event.getClickedBlock().getZ() + "," + event.getClickedBlock().getWorld().getName() + ".owner").equals(event.getPlayer().getUniqueId().toString())) {
                event.setCancelled(true);

                event.getPlayer().sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("chestProtection.notyours"))));
            } else if (SpigotMain.getInstance().getChests().getBoolean(event.getClickedBlock().getX() + "," + event.getClickedBlock().getY() + "," + event.getClickedBlock().getZ() + "," + event.getClickedBlock().getWorld().getName() + ".protected") == null && SpigotMain.getInstance().getChests().getString(event.getClickedBlock().getX() + "," + event.getClickedBlock().getY() + "," + event.getClickedBlock().getZ() + "," + event.getClickedBlock().getWorld().getName() + ".owner") == null) {
                SpigotMain.getInstance().getChests().set(event.getClickedBlock().getX() + "," + event.getClickedBlock().getY() + "," + event.getClickedBlock().getZ() + "," + event.getClickedBlock().getWorld().getName() + ".protected", null);
                SpigotMain.getInstance().getChests().set(event.getClickedBlock().getX() + "," + event.getClickedBlock().getY() + "," + event.getClickedBlock().getZ() + "," + event.getClickedBlock().getWorld().getName() + ".owner", null);
                SpigotMain.getInstance().getChests().save();

                event.getPlayer().sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("chestProtection.claimed"))));
            }
        }
    }

    @EventHandler
    public void onChestPlace(BlockPlaceEvent event) {
        if (SpigotMain.getInstance().getPluginConfig().getStringList("protectable").contains(event.getBlock().getType().toString())) {
            SpigotMain.getInstance().getChests().set(event.getBlock().getX() + "," + event.getBlock().getY() + "," + event.getBlock().getZ() + "," + event.getBlock().getWorld().getName() + ".protected", true);
            SpigotMain.getInstance().getChests().set(event.getBlock().getX() + "," + event.getBlock().getY() + "," + event.getBlock().getZ() + "," + event.getBlock().getWorld().getName() + ".owner", event.getPlayer().getUniqueId().toString());
            SpigotMain.getInstance().getChests().save();

            event.getPlayer().sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("chestProtection.claimed"))));
        }
    }

    @EventHandler
    public void onChestPlace(BlockMultiPlaceEvent event) {
        if (SpigotMain.getInstance().getPluginConfig().getStringList("protectable").contains(event.getBlock().getType().toString())) {
            SpigotMain.getInstance().getChests().set(event.getBlock().getX() + "," + event.getBlock().getY() + "," + event.getBlock().getZ() + "," + event.getBlock().getWorld().getName() + ".protected", true);
            SpigotMain.getInstance().getChests().set(event.getBlock().getX() + "," + event.getBlock().getY() + "," + event.getBlock().getZ() + "," + event.getBlock().getWorld().getName() + ".owner", event.getPlayer().getUniqueId().toString());

            for (BlockState block : event.getReplacedBlockStates()) {
                SpigotMain.getInstance().getChests().set(block.getX() + "," + block.getY() + "," + block.getZ() + "," + block.getWorld().getName() + ".protected", true);
                SpigotMain.getInstance().getChests().set(block.getX() + "," + block.getY() + "," + block.getZ() + "," + block.getWorld().getName() + ".owner", event.getPlayer().getUniqueId().toString());
            }

            event.getPlayer().sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("chestProtection.claimed"))));

            SpigotMain.getInstance().getChests().save();
        }
    }

    @EventHandler
    public void onChestBreak(BlockBreakEvent event) {
        if (SpigotMain.getInstance().getPluginConfig().getStringList("protectable").contains(event.getBlock().getType().toString()) && SpigotMain.getInstance().getChests().getBoolean(event.getBlock().getX() + "," + event.getBlock().getY() + "," + event.getBlock().getZ() + "," + event.getBlock().getWorld().getName() + ".protected")) {
            if (!SpigotMain.getInstance().getChests().getString(event.getBlock().getX() + "," + event.getBlock().getY() + "," + event.getBlock().getZ() + "," + event.getBlock().getWorld().getName() + ".owner").equals(event.getPlayer().getUniqueId().toString())) {
                event.setCancelled(true);

                event.getPlayer().sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("chestProtection.notyours"))));
            } else {
                SpigotMain.getInstance().getChests().set(event.getBlock().getX() + "," + event.getBlock().getY() + "," + event.getBlock().getZ() + "," + event.getBlock().getWorld().getName() + ".protected", null);
                SpigotMain.getInstance().getChests().set(event.getBlock().getX() + "," + event.getBlock().getY() + "," + event.getBlock().getZ() + "," + event.getBlock().getWorld().getName() + ".owner", null);
                SpigotMain.getInstance().getChests().save();

                event.getPlayer().sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("chestProtection.unclaimed"))));
            }
        }
    }

    @EventHandler
    public void onChestBreak(BlockExplodeEvent event) {
        System.out.println(event.getBlock().getType().toString());
        if (SpigotMain.getInstance().getPluginConfig().getStringList("protectable").contains(event.getBlock().getType().toString()) && SpigotMain.getInstance().getChests().getBoolean(event.getBlock().getX() + "," + event.getBlock().getY() + "," + event.getBlock().getZ() + "," + event.getBlock().getWorld().getName() + ".protected")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onChestBreak(BlockBurnEvent event) {
        if (SpigotMain.getInstance().getPluginConfig().getStringList("protectable").contains(event.getBlock().getType().toString()) && SpigotMain.getInstance().getChests().getBoolean(event.getBlock().getX() + "," + event.getBlock().getY() + "," + event.getBlock().getZ() + "," + event.getBlock().getWorld().getName() + ".protected")) {
            event.setCancelled(true);
        }
    }
}