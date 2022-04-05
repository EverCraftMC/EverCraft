package io.github.evercraftmc.evercraft.spigot.commands.kit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import io.github.evercraftmc.evercraft.shared.util.StringUtils;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import io.github.evercraftmc.evercraft.spigot.SpigotMain;
import io.github.evercraftmc.evercraft.spigot.commands.SpigotCommand;
import io.github.evercraftmc.evercraft.spigot.util.formatting.ComponentFormatter;
import io.github.evercraftmc.evercraft.spigot.util.types.SerializableItemStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class KitCommand extends SpigotCommand {
    public KitCommand(String name, String description, List<String> aliases, String permission) {
        super(name, description, aliases, permission);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            if (args.length >= 1) {
                List<SerializableItemStack> serializableitems = SpigotMain.getInstance().getKits().getSerializableList(args[0], SerializableItemStack.class);
                if (serializableitems != null) {
                    List<ItemStack> items = new ArrayList<ItemStack>();

                    for (SerializableItemStack item : serializableitems) {
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

                    player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("kit.kit").replace("{kit}", args[0]))));
                } else {
                    player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("kit.notFound").replace("{kit}", args[0]))));
                }
            } else {
                sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("error.invalidArgs"))));
            }
        } else {
            sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("error.noConsole"))));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<String>();

        if (args.length == 1) {
            list = new ArrayList<String>(SpigotMain.getInstance().getKits().getKeys(false));
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