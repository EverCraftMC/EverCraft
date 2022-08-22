package io.github.evercraftmc.evercraft.spigot.listeners;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import io.github.evercraftmc.evercraft.spigot.SpigotChests;
import io.github.evercraftmc.evercraft.spigot.SpigotMain;
import io.github.evercraftmc.evercraft.spigot.util.formatting.ComponentFormatter;

public class ChestProtectionListener extends SpigotListener {
    @EventHandler
    public void onChestOpen(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && SpigotMain.getInstance().getChests().getParsed().blocks.containsKey(event.getClickedBlock().getX() + "," + event.getClickedBlock().getY() + "," + event.getClickedBlock().getZ() + "," + event.getClickedBlock().getWorld().getName())) {
            if (SpigotMain.getInstance().getChests().getParsed().blocks.get(event.getClickedBlock().getX() + "," + event.getClickedBlock().getY() + "," + event.getClickedBlock().getZ() + "," + event.getClickedBlock().getWorld().getName()).isProtected && !SpigotMain.getInstance().getChests().getParsed().blocks.get(event.getClickedBlock().getX() + "," + event.getClickedBlock().getY() + "," + event.getClickedBlock().getZ() + "," + event.getClickedBlock().getWorld().getName()).allowUse && !SpigotMain.getInstance().getChests().getParsed().blocks.get(event.getClickedBlock().getX() + "," + event.getClickedBlock().getY() + "," + event.getClickedBlock().getZ() + "," + event.getClickedBlock().getWorld().getName()).owner.equals(event.getPlayer().getUniqueId().toString()) && !(SpigotMain.getInstance().getChests().getParsed().players.containsKey(SpigotMain.getInstance().getChests().getParsed().blocks.get(event.getClickedBlock().getX() + "," + event.getClickedBlock().getY() + "," + event.getClickedBlock().getZ() + "," + event.getClickedBlock().getWorld().getName()).owner) && SpigotMain.getInstance().getChests().getParsed().players.get(SpigotMain.getInstance().getChests().getParsed().blocks.get(event.getClickedBlock().getX() + "," + event.getClickedBlock().getY() + "," + event.getClickedBlock().getZ() + "," + event.getClickedBlock().getWorld().getName()).owner).friends.contains(event.getPlayer().getUniqueId().toString()))) {
                event.setCancelled(true);

                event.getPlayer().sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getParsed().chestProtection.notYours)));
            }
        }
    }

    @EventHandler
    public void onChestPlace(BlockPlaceEvent event) {
        if (event instanceof BlockMultiPlaceEvent) {
            return;
        }

        Boolean protectable = false;
        Boolean allowUse = false;

        for (String protectableSetting : SpigotMain.getInstance().getPluginConfig().getParsed().chestProtection.protectable) {
            if (protectableSetting.split(":")[0].equalsIgnoreCase(event.getBlock().getType().toString().toLowerCase().replace("red__", "{color}__").replace("orange_", "{color}_").replace("yellow_", "{color}_").replace("lime_", "{color}_").replace("green_", "{color}_").replace("cyan_", "{color}_").replace("light_blue_", "{color}_").replace("blue_", "{color}_").replace("purple_", "{color}_").replace("magenta_", "{color}_").replace("pink_", "{color}_").replace("brown_", "{color}_").replace("white_", "{color}_").replace("light_gray_", "{color}_").replace("gray_", "{color}_").replace("black_", "{color}_"))) {
                protectable = true;

                if (protectableSetting.split(":")[1].equals("0")) {
                    allowUse = false;
                } else if (protectableSetting.split(":")[1].equals("1")) {
                    allowUse = true;
                }

                break;
            }
        }

        if (protectable && SpigotMain.getInstance().getChests().getParsed().players.get(event.getPlayer().getUniqueId().toString()).autoClaim) {
            SpigotMain.getInstance().getChests().getParsed().blocks.put(event.getBlock().getX() + "," + event.getBlock().getY() + "," + event.getBlock().getZ() + "," + event.getBlock().getWorld().getName(), new SpigotChests.Chest());
            SpigotMain.getInstance().getChests().getParsed().blocks.get(event.getBlock().getX() + "," + event.getBlock().getY() + "," + event.getBlock().getZ() + "," + event.getBlock().getWorld().getName()).isProtected = true;
            SpigotMain.getInstance().getChests().getParsed().blocks.get(event.getBlock().getX() + "," + event.getBlock().getY() + "," + event.getBlock().getZ() + "," + event.getBlock().getWorld().getName()).owner = event.getPlayer().getUniqueId().toString();
            SpigotMain.getInstance().getChests().getParsed().blocks.get(event.getBlock().getX() + "," + event.getBlock().getY() + "," + event.getBlock().getZ() + "," + event.getBlock().getWorld().getName()).allowUse = allowUse;
            SpigotMain.getInstance().getChests().save();

            event.getPlayer().sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getParsed().chestProtection.claimed)));
        }
    }

    @EventHandler
    public void onChestPlace(BlockMultiPlaceEvent event) {
        Boolean protectable = false;
        Boolean allowUse = false;

        for (String protectableSetting : SpigotMain.getInstance().getPluginConfig().getParsed().chestProtection.protectable) {
            if (protectableSetting.split(":")[0].equalsIgnoreCase(event.getBlock().getType().toString().toLowerCase().replace("red__", "{color}__").replace("orange_", "{color}_").replace("yellow_", "{color}_").replace("lime_", "{color}_").replace("green_", "{color}_").replace("cyan_", "{color}_").replace("light_blue_", "{color}_").replace("blue_", "{color}_").replace("purple_", "{color}_").replace("magenta_", "{color}_").replace("pink_", "{color}_").replace("brown_", "{color}_").replace("white_", "{color}_").replace("light_gray_", "{color}_").replace("gray_", "{color}_").replace("black_", "{color}_"))) {
                protectable = true;

                if (protectableSetting.split(":")[1].equals("0")) {
                    allowUse = false;
                } else if (protectableSetting.split(":")[1].equals("1")) {
                    allowUse = true;
                }

                break;
            }
        }

        if (protectable && SpigotMain.getInstance().getChests().getParsed().players.get(event.getPlayer().getUniqueId().toString()).autoClaim) {
            SpigotMain.getInstance().getChests().getParsed().blocks.put(event.getBlock().getX() + "," + event.getBlock().getY() + "," + event.getBlock().getZ() + "," + event.getBlock().getWorld().getName(), new SpigotChests.Chest());
            SpigotMain.getInstance().getChests().getParsed().blocks.get(event.getBlock().getX() + "," + event.getBlock().getY() + "," + event.getBlock().getZ() + "," + event.getBlock().getWorld().getName()).isProtected = true;
            SpigotMain.getInstance().getChests().getParsed().blocks.get(event.getBlock().getX() + "," + event.getBlock().getY() + "," + event.getBlock().getZ() + "," + event.getBlock().getWorld().getName()).owner = event.getPlayer().getUniqueId().toString();
            SpigotMain.getInstance().getChests().getParsed().blocks.get(event.getBlock().getX() + "," + event.getBlock().getY() + "," + event.getBlock().getZ() + "," + event.getBlock().getWorld().getName()).allowUse = allowUse;

            for (BlockState block : event.getReplacedBlockStates()) {
                SpigotMain.getInstance().getChests().getParsed().blocks.put(block.getX() + "," + block.getY() + "," + block.getZ() + "," + block.getWorld().getName(), new SpigotChests.Chest());
                SpigotMain.getInstance().getChests().getParsed().blocks.get(block.getX() + "," + block.getY() + "," + block.getZ() + "," + block.getWorld().getName()).isProtected = true;
                SpigotMain.getInstance().getChests().getParsed().blocks.get(block.getX() + "," + block.getY() + "," + block.getZ() + "," + block.getWorld().getName()).owner = event.getPlayer().getUniqueId().toString();
                SpigotMain.getInstance().getChests().getParsed().blocks.get(block.getX() + "," + block.getY() + "," + block.getZ() + "," + block.getWorld().getName()).allowUse = allowUse;
            }

            SpigotMain.getInstance().getChests().save();

            event.getPlayer().sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getParsed().chestProtection.claimed)));
        }
    }

    @EventHandler
    public void onChestBreak(BlockBreakEvent event) {
        if (SpigotMain.getInstance().getChests().getParsed().blocks.containsKey(event.getBlock().getX() + "," + event.getBlock().getY() + "," + event.getBlock().getZ() + "," + event.getBlock().getWorld().getName())) {
            if (!SpigotMain.getInstance().getChests().getParsed().blocks.get(event.getBlock().getX() + "," + event.getBlock().getY() + "," + event.getBlock().getZ() + "," + event.getBlock().getWorld().getName()).owner.equals(event.getPlayer().getUniqueId().toString())) {
                event.setCancelled(true);

                event.getPlayer().sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getParsed().chestProtection.notYours)));
            } else {
                SpigotMain.getInstance().getChests().getParsed().blocks.remove(event.getBlock().getX() + "," + event.getBlock().getY() + "," + event.getBlock().getZ() + "," + event.getBlock().getWorld().getName());
                SpigotMain.getInstance().getChests().save();

                event.getPlayer().sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getParsed().chestProtection.unclaimed)));
            }
        }
    }

    @EventHandler
    public void onChestBreak(EntityExplodeEvent event) {
        for (Block block : event.blockList()) {
            if (SpigotMain.getInstance().getChests().getParsed().blocks.containsKey(block.getX() + "," + block.getY() + "," + block.getZ() + "," + block.getWorld().getName())) {
                event.blockList().remove(block);
            }
        }
    }

    @EventHandler
    public void onChestBreak(BlockExplodeEvent event) {
        for (Block block : event.blockList()) {
            if (SpigotMain.getInstance().getChests().getParsed().blocks.containsKey(block.getX() + "," + block.getY() + "," + block.getZ() + "," + block.getWorld().getName())) {
                event.blockList().remove(block);
            }
        }
    }

    @EventHandler
    public void onChestBreak(BlockBurnEvent event) {
        if (SpigotMain.getInstance().getChests().getParsed().blocks.containsKey(event.getBlock().getX() + "," + event.getBlock().getY() + "," + event.getBlock().getZ() + "," + event.getBlock().getWorld().getName())) {
            event.setCancelled(true);
        }
    }
}