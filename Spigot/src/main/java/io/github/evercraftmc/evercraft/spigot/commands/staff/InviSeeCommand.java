package io.github.evercraftmc.evercraft.spigot.commands.staff;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryEvent;
import io.github.evercraftmc.evercraft.shared.util.StringUtils;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import io.github.evercraftmc.evercraft.spigot.SpigotMain;
import io.github.evercraftmc.evercraft.spigot.commands.SpigotCommand;
import io.github.evercraftmc.evercraft.spigot.util.bukkit.ChestGUI;
import io.github.evercraftmc.evercraft.spigot.util.formatting.ComponentFormatter;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTCompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
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
                File file = new File(SpigotMain.getInstance().getServer().getWorldContainer() + "/" + SpigotMain.getInstance().getServer().getWorlds().get(0).getName() + "/playerdata/" + player.getUniqueId());

                if (file.exists()) {
                    try {
                        ChestGUI gui = new ChestGUI("&7" + player.getName() + "&r&7's inventory", 3, false, false) {
                            @EventHandler
                            public void onItemMove(InventoryEvent event) {

                            }
                        };

                        NBTTagCompound nbt = NBTCompressedStreamTools.a(file);
                        NBTTagList items = (NBTTagList) nbt.c("Inventory");
                        for (NBTBase item: items) {
                            gui.addItem(CraftItemStack.asBukkitCopy(ItemStack.a((NBTTagCompound) item)), ((NBTTagInt) ((NBTTagCompound) item).c("Slot")).f());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getParsed().error.playerNotFound.replace("{player}", args[0]))));
                }
            } else {
                sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getParsed().error.playerNotFound.replace("{player}", args[0]))));
            }
        } else {
            sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getParsed().error.noConsole)));
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