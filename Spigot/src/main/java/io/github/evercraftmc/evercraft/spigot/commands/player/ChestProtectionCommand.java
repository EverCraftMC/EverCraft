package io.github.evercraftmc.evercraft.spigot.commands.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import io.github.evercraftmc.evercraft.shared.util.StringUtils;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import io.github.evercraftmc.evercraft.spigot.SpigotMain;
import io.github.evercraftmc.evercraft.spigot.commands.SpigotCommand;
import io.github.evercraftmc.evercraft.spigot.util.formatting.ComponentFormatter;

public class ChestProtectionCommand extends SpigotCommand {
    public ChestProtectionCommand(String name, String description, List<String> aliases, String permission) {
        super(name, description, aliases, permission);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            if (args.length > 0) {
                Block block = player.getTargetBlock(8);

                if (block != null) {
                    Boolean protectable = false;
                    Boolean allowUse = false;

                    for (String protectableSetting : SpigotMain.getInstance().getPluginConfig().getParsed().chestProtection.protectable) {
                        if (protectableSetting.split(":")[0].equalsIgnoreCase(block.getType().toString().toLowerCase().replace("red__", "{color}__").replace("orange_", "{color}_").replace("yellow_", "{color}_").replace("lime_", "{color}_").replace("green_", "{color}_").replace("cyan_", "{color}_").replace("light_blue_", "{color}_").replace("blue_", "{color}_").replace("purple_", "{color}_").replace("magenta_", "{color}_").replace("pink_", "{color}_").replace("brown_", "{color}_").replace("white_", "{color}_").replace("light_gray_", "{color}_").replace("gray_", "{color}_").replace("black_", "{color}_"))) {
                            protectable = true;

                            if (protectableSetting.split(":")[1].equals("0")) {
                                allowUse = false;
                            } else if (protectableSetting.split(":")[1].equals("1")) {
                                allowUse = true;
                            }

                            break;
                        }
                    }

                    if (protectable && (!SpigotMain.getInstance().getChests().getParsed().blocks.containsKey(block.getX() + "," + block.getY() + "," + block.getZ() + "," + block.getWorld().getName()) || SpigotMain.getInstance().getChests().getParsed().blocks.get(block.getX() + "," + block.getY() + "," + block.getZ() + "," + block.getWorld().getName()).owner.equals(player.getUniqueId().toString()))) {
                        if (args[0].equalsIgnoreCase("protect") && SpigotMain.getInstance().getChests().getParsed().blocks.get(block.getX() + "," + block.getY() + "," + block.getZ() + "," + block.getWorld().getName()).owner.equals(player.getUniqueId().toString())) {
                            SpigotMain.getInstance().getChests().getParsed().blocks.get(block.getX() + "," + block.getY() + "," + block.getZ() + "," + block.getWorld().getName()).isProtected = true;

                            player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getParsed().chestProtection.wasProtected)));
                        } else if (args[0].equalsIgnoreCase("unprotect") && SpigotMain.getInstance().getChests().getParsed().blocks.get(block.getX() + "," + block.getY() + "," + block.getZ() + "," + block.getWorld().getName()).owner.equals(player.getUniqueId().toString())) {
                            SpigotMain.getInstance().getChests().getParsed().blocks.get(block.getX() + "," + block.getY() + "," + block.getZ() + "," + block.getWorld().getName()).isProtected = false;

                            player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getParsed().chestProtection.unprotected)));
                        } else if (args[0].equalsIgnoreCase("claim") && !SpigotMain.getInstance().getChests().getParsed().blocks.containsKey(block.getX() + "," + block.getY() + "," + block.getZ() + "," + block.getWorld().getName())) {
                            SpigotMain.getInstance().getChests().getParsed().blocks.get(block.getX() + "," + block.getY() + "," + block.getZ() + "," + block.getWorld().getName()).isProtected = true;
                            SpigotMain.getInstance().getChests().getParsed().blocks.get(block.getX() + "," + block.getY() + "," + block.getZ() + "," + block.getWorld().getName()).owner = player.getUniqueId().toString();
                            SpigotMain.getInstance().getChests().getParsed().blocks.get(block.getX() + "," + block.getY() + "," + block.getZ() + "," + block.getWorld().getName()).allowUse = allowUse;
                            SpigotMain.getInstance().getChests().save();

                            player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getParsed().chestProtection.claimed)));
                        } else if (args[0].equalsIgnoreCase("unclaim") && SpigotMain.getInstance().getChests().getParsed().blocks.get(block.getX() + "," + block.getY() + "," + block.getZ() + "," + block.getWorld().getName()).owner.equals(player.getUniqueId().toString())) {
                            SpigotMain.getInstance().getChests().getParsed().blocks.remove(block.getX() + "," + block.getY() + "," + block.getZ() + "," + block.getWorld().getName());
                            SpigotMain.getInstance().getChests().save();

                            player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getParsed().chestProtection.unclaimed)));
                        } else if (args[0].equalsIgnoreCase("allowUse") && SpigotMain.getInstance().getChests().getParsed().blocks.get(block.getX() + "," + block.getY() + "," + block.getZ() + "," + block.getWorld().getName()).owner.equals(player.getUniqueId().toString())) {
                            SpigotMain.getInstance().getChests().getParsed().blocks.get(block.getX() + "," + block.getY() + "," + block.getZ() + "," + block.getWorld().getName()).allowUse = true;

                            player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getParsed().chestProtection.allowedUse)));
                        } else if (args[0].equalsIgnoreCase("disallowUse") && SpigotMain.getInstance().getChests().getParsed().blocks.get(block.getX() + "," + block.getY() + "," + block.getZ() + "," + block.getWorld().getName()).owner.equals(player.getUniqueId().toString())) {
                            SpigotMain.getInstance().getChests().getParsed().blocks.get(block.getX() + "," + block.getY() + "," + block.getZ() + "," + block.getWorld().getName()).allowUse = false;

                            player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getParsed().chestProtection.disallowedUse)));
                        }
                    } else {
                        player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getParsed().chestProtection.notYours)));
                    }
                } else {
                    player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getParsed().chestProtection.noBlock)));
                }
            } else {
                player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getParsed().error.invalidArgs)));
            }
        } else {
            sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getParsed().error.noConsole)));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<String>();

        if (args.length == 1) {
            list.add("protect");
            list.add("unprotect");
            list.add("claim");
            list.add("unclaim");
            list.add("allowUse");
            list.add("disallowUse");
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