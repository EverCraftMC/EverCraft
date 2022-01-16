package com.kale_ko.kalesutilities.listeners;

import com.kale_ko.kalesutilities.Util;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignEditorListener implements Listener {
    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        if (Util.hasPermission(event.getPlayer(), "kalesutilities.features.colorsigns")) {
            event.setLine(0, Util.formatMessage(event.getLine(0)));
            event.setLine(1, Util.formatMessage(event.getLine(1)));
            event.setLine(2, Util.formatMessage(event.getLine(2)));
            event.setLine(3, Util.formatMessage(event.getLine(3)));
        }
    }

    @EventHandler
    public void onSignClick(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null && (event.getClickedBlock().getType() == Material.OAK_SIGN
                || event.getClickedBlock().getType() == Material.OAK_WALL_SIGN
                || event.getClickedBlock().getType() == Material.DARK_OAK_SIGN
                || event.getClickedBlock().getType() == Material.DARK_OAK_WALL_SIGN
                || event.getClickedBlock().getType() == Material.BIRCH_SIGN
                || event.getClickedBlock().getType() == Material.BIRCH_WALL_SIGN
                || event.getClickedBlock().getType() == Material.SPRUCE_SIGN
                || event.getClickedBlock().getType() == Material.SPRUCE_WALL_SIGN
                || event.getClickedBlock().getType() == Material.ACACIA_SIGN
                || event.getClickedBlock().getType() == Material.ACACIA_WALL_SIGN
                || event.getClickedBlock().getType() == Material.JUNGLE_SIGN
                || event.getClickedBlock().getType() == Material.JUNGLE_WALL_SIGN
                || event.getClickedBlock().getType() == Material.CRIMSON_SIGN
                || event.getClickedBlock().getType() == Material.CRIMSON_WALL_SIGN
                || event.getClickedBlock().getType() == Material.WARPED_SIGN
                || event.getClickedBlock().getType() == Material.WARPED_WALL_SIGN)) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getPlayer().getItemOnCursor().getType() == Material.AIR && event.getPlayer().isSneaking() && Util.hasPermission(event.getPlayer(), "kalesutilities.features.editsigns")) {
                Sign sign = (Sign) event.getClickedBlock().getState();
                sign.setEditable(true);
                sign.setLine(0, Util.unFormatMessage(sign.getLine(0)));
                sign.setLine(1, Util.unFormatMessage(sign.getLine(1)));
                sign.setLine(2, Util.unFormatMessage(sign.getLine(2)));
                sign.setLine(3, Util.unFormatMessage(sign.getLine(3)));
                sign.update();
                event.getPlayer().sendSignChange(sign.getLocation(), sign.getLines());
                event.getPlayer().openSign(sign);
                sign.setEditable(false);
            }
        }
    }
}