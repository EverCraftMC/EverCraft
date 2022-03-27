package com.kale_ko.evercraft.spigot.commands.kit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.kale_ko.evercraft.shared.util.StringUtils;
import com.kale_ko.evercraft.shared.util.formatting.TextFormatter;
import com.kale_ko.evercraft.spigot.SpigotMain;
import com.kale_ko.evercraft.spigot.commands.SpigotCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class KitCommand extends SpigotCommand {
    public static final String name = "kit";
    public static final String description = "Get a kit";

    public static final List<String> aliases = Arrays.asList();

    public static final String permission = "evercraft.commands.kit.kit";

    @Override
    public void run(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            if (args.length >= 1) {
                List<ItemStack> items = SpigotMain.getInstance().getKits().getSerializableList(args[0], ItemStack.class);

                for (ItemStack item : items) {
                    if (item.getType().getEquipmentSlot() != null && item.getType().getEquipmentSlot() != EquipmentSlot.HAND && item.getType().getEquipmentSlot() != EquipmentSlot.OFF_HAND) {
                        if (player.getInventory().getItem(item.getType().getEquipmentSlot()) == null || player.getInventory().getItem(item.getType().getEquipmentSlot()).getType().isAir()) {
                            player.getInventory().setItem(item.getType().getEquipmentSlot(), item);
                        } else {
                            player.getInventory().addItem(item);
                        }
                    } else {
                        player.getInventory().addItem(item);
                    }
                }
            } else {
                sender.sendMessage(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("error.invalidArgs")));
            }
        } else {
            sender.sendMessage(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("error.noConsole")));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<String>();

        if (args.length == 0) {
            list = Arrays.asList(SpigotMain.getInstance().getKits().getKeys(false).toArray(new String[] {}));
        } else {
            return Arrays.asList();
        }

        if (args.length > 0) {
            return StringUtils.matchPartial(args[args.length - 1], list);
        } else {
            return list;
        }
    }
}