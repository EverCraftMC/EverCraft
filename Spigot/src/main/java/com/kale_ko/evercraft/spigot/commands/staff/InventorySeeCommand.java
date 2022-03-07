package com.kale_ko.evercraft.spigot.commands.staff;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import com.kale_ko.evercraft.spigot.SpigotPlugin;
import com.kale_ko.evercraft.spigot.Util;
import com.kale_ko.evercraft.spigot.commands.SpigotCommand;
import com.kale_ko.evercraft.spigot.misc.ChestGUI;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTCompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class InventorySeeCommand extends SpigotCommand {
    public InventorySeeCommand(String name, String description, List<String> aliases, String usage, String permission) {
        super(name, description, aliases, usage, permission);
    }

    @Override
    public void run(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player player) {
            Player player2 = SpigotPlugin.Instance.getServer().getPlayer(args[0]);

            if (player2 != null) {
                ChestGUI gui = new ChestGUI(args[0] + "'s inventory", 45);

                ItemStack stack = player2.getInventory().getHelmet();
                gui.addItem(36, stack);

                stack = player2.getInventory().getChestplate();
                gui.addItem(37, stack);

                stack = player2.getInventory().getLeggings();
                gui.addItem(38, stack);

                stack = player2.getInventory().getBoots();
                gui.addItem(39, stack);

                stack = player2.getInventory().getItemInOffHand();
                gui.addItem(40, stack);

                gui.open(player);
            } else {
                OfflinePlayer player2offline = SpigotPlugin.Instance.getServer().getOfflinePlayer(args[0]);

                try {
                    Properties serverProperties = new Properties();
                    serverProperties.load(new FileInputStream(SpigotPlugin.Instance.getServer().getWorldContainer() + "/server.properties"));
                    String world = serverProperties.getProperty("level-name");

                    File file = new File(SpigotPlugin.Instance.getServer().getWorldContainer() + "/" + world + "/playerdata/" + player2offline.getUniqueId() + ".dat");

                    if (file.exists()) {
                        ChestGUI gui = new ChestGUI(args[0] + "'s inventory", 45);

                        NBTTagCompound nbt = NBTCompressedStreamTools.a((InputStream) (new FileInputStream(file)));

                        NBTTagList playerInventory = (NBTTagList) nbt.c("Inventory");

                        for (Integer i = 0; i < 36; i++) {
                            NBTTagCompound compound = (NBTTagCompound) getSlot(playerInventory, i);
                            if (compound != null) {
                                ItemStack stack = CraftItemStack.asBukkitCopy(net.minecraft.world.item.ItemStack.a(compound));
                                gui.addItem(i, stack);
                            }
                        }

                        NBTTagCompound compound = (NBTTagCompound) getSlot(playerInventory, 100);
                        if (compound != null) {
                            ItemStack stack = CraftItemStack.asBukkitCopy(net.minecraft.world.item.ItemStack.a(compound));
                            gui.addItem(39, stack);
                        }

                        compound = (NBTTagCompound) getSlot(playerInventory, 101);
                        if (compound != null) {
                            ItemStack stack = CraftItemStack.asBukkitCopy(net.minecraft.world.item.ItemStack.a(compound));
                            gui.addItem(38, stack);
                        }

                        compound = (NBTTagCompound) getSlot(playerInventory, 102);
                        if (compound != null) {
                            ItemStack stack = CraftItemStack.asBukkitCopy(net.minecraft.world.item.ItemStack.a(compound));
                            gui.addItem(37, stack);
                        }

                        compound = (NBTTagCompound) getSlot(playerInventory, 103);
                        if (compound != null) {
                            ItemStack stack = CraftItemStack.asBukkitCopy(net.minecraft.world.item.ItemStack.a(compound));
                            gui.addItem(36, stack);
                        }

                        compound = (NBTTagCompound) getSlot(playerInventory, -106);
                        if (compound != null) {
                            ItemStack stack = CraftItemStack.asBukkitCopy(net.minecraft.world.item.ItemStack.a(compound));
                            gui.addItem(40, stack);
                        }

                        gui.open(player);
                    } else {
                        Util.sendMessage(player, SpigotPlugin.Instance.config.getString("messages.playernotfound").replace("{player}", args[0]));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.noconsole"));
        }
    }

    private static NBTTagCompound getSlot(NBTTagList nbtList, Integer slot) {
        for (NBTBase nbt : nbtList) {
            if (((NBTTagCompound) nbt).h("Slot") == slot) {
                return (NBTTagCompound) nbt;
            }
        }

        return null;
    }
}