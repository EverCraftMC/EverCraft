package io.github.evercraftmc.evercraft.spigot.commands.staff;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_19_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryEvent;
import io.github.evercraftmc.evercraft.shared.util.StringUtils;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import io.github.evercraftmc.evercraft.spigot.SpigotMain;
import io.github.evercraftmc.evercraft.spigot.commands.SpigotCommand;
import io.github.evercraftmc.evercraft.spigot.util.bukkit.ChestGUI;
import io.github.evercraftmc.evercraft.spigot.util.formatting.ComponentFormatter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

public class InviSeeCommand extends SpigotCommand {
    public InviSeeCommand(String name, String description, List<String> aliases, String permission) {
        super(name, description, aliases, permission);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            OfflinePlayer player2 = SpigotMain.getInstance().getServer().getOfflinePlayer(args[0]);

            if (player2 != null) {
                ChestGUI gui = new ChestGUI("&7" + player2.getName() + "&r&7's inventory", 5, false, false) {
                    @EventHandler
                    public void onItemMove(InventoryEvent event) {

                    }
                };

                Player onlinePlayer2 = SpigotMain.getInstance().getServer().getPlayer(args[0]);

                if (onlinePlayer2 != null) {
                    for (Integer i = 9; i < 36; i++) {
                        gui.addItem(onlinePlayer2.getInventory().getItem(i), i - 9);
                    }

                    for (Integer i = 0; i < 9; i++) {
                        gui.addItem(onlinePlayer2.getInventory().getItem(i), i + 27);
                    }

                    gui.addItem(onlinePlayer2.getInventory().getHelmet(), 36);
                    gui.addItem(onlinePlayer2.getInventory().getChestplate(), 37);
                    gui.addItem(onlinePlayer2.getInventory().getLeggings(), 38);
                    gui.addItem(onlinePlayer2.getInventory().getBoots(), 39);

                    gui.addItem(onlinePlayer2.getInventory().getItemInOffHand(), 40);
                } else {
                    File file = new File(SpigotMain.getInstance().getServer().getWorldContainer() + "/" + SpigotMain.getInstance().getServer().getWorlds().get(0).getName() + "/playerdata/" + player2.getUniqueId().toString() + ".dat");

                    if (file.exists()) {
                        try {
                            CompoundTag nbt = NbtIo.read(file);
                            ListTag items = (ListTag) nbt.getList("Inventory", ListTag.TAG_COMPOUND);
                            for (Tag item : items) {
                                Integer slot = (int) ((CompoundTag) item).getByte("Slot");

                                if (slot >= 0 && slot < 9) {
                                    slot = slot + 27;
                                } else if (slot >= 9 && slot < 36) {
                                    slot = slot - 9;
                                } else if (slot == 103) {
                                    slot = 36;
                                } else if (slot == 102) {
                                    slot = 37;
                                } else if (slot == 101) {
                                    slot = 38;
                                } else if (slot == 100) {
                                    slot = 39;
                                } else if (slot == -106) {
                                    slot = 40;
                                }

                                gui.addItem(CraftItemStack.asBukkitCopy(ItemStack.of((CompoundTag) item)), slot);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().error.playerNotFound.replace("{player}", args[0]))));
                    }
                }

                gui.open(player);
            } else {
                sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().error.playerNotFound.replace("{player}", args[0]))));
            }
        } else {
            sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().error.noConsole)));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<String>();

        if (args.length == 1) {
            for (Player player : SpigotMain.getInstance().getServer().getOnlinePlayers()) {
                list.add(player.getName());
            }
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