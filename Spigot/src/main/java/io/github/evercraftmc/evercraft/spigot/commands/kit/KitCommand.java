package io.github.evercraftmc.evercraft.spigot.commands.kit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import io.github.evercraftmc.evercraft.shared.util.StringUtils;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import io.github.evercraftmc.evercraft.spigot.SpigotMain;
import io.github.evercraftmc.evercraft.spigot.commands.SpigotCommand;
import io.github.evercraftmc.evercraft.spigot.util.formatting.ComponentFormatter;
import io.github.evercraftmc.evercraft.spigot.util.types.SerializableItemStack;

public class KitCommand extends SpigotCommand {
    public KitCommand(String name, String description, List<String> aliases, String permission) {
        super(name, description, aliases, permission);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            if (args.length >= 1) {
                List<SerializableItemStack> serializableItems = SpigotMain.getInstance().getKits().get().kits.get(args[0]);
                if (serializableItems != null) {
                    List<ItemStack> items = new ArrayList<ItemStack>();

                    for (SerializableItemStack item : serializableItems) {
                        items.add(item.toBukkitItemStack());
                    }

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

                    if (!(args.length >= 2 && args[1].equalsIgnoreCase("true"))) {
                        player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().kit.kit.replace("{kit}", args[0]))));
                    }
                } else {
                    player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().kit.notFound.replace("{kit}", args[0]))));
                }
            } else {
                sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().error.invalidArgs)));
            }
        } else {
            sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().error.noConsole)));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<String>();

        if (args.length == 1) {
            list = new ArrayList<String>(SpigotMain.getInstance().getKits().get().kits.keySet());
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