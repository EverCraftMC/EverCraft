package io.github.evercraftmc.evercraft.spigot.commands.player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import io.github.evercraftmc.evercraft.shared.util.StringUtils;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import io.github.evercraftmc.evercraft.spigot.SpigotChests;
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
                if (args[0].equalsIgnoreCase("disableAutoClaim")) {
                    SpigotMain.getInstance().getChests().get().players.get(player.getUniqueId().toString()).autoClaim = false;

                    player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().chestProtection.disabledAutoClaim)));
                } else if (args[0].equalsIgnoreCase("enableAutoClaim")) {
                    SpigotMain.getInstance().getChests().get().players.get(player.getUniqueId().toString()).autoClaim = true;

                    player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().chestProtection.enabledAutoClaim)));
                } else if (args[0].equalsIgnoreCase("addFriend")) {
                    if (args.length > 1) {
                        Player player2 = SpigotMain.getInstance().getServer().getPlayer(args[1]);

                        if (player2 != null) {
                            if (!SpigotMain.getInstance().getChests().get().players.get(player.getUniqueId().toString()).friends.contains(player2.getUniqueId().toString())) {
                                SpigotMain.getInstance().getChests().get().players.get(player.getUniqueId().toString()).friends.add(player2.getUniqueId().toString());
                            }

                            player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().chestProtection.addedFriend.replace("{player}", args[1]))));
                        } else {
                            sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().error.playerNotFound.replace("{player}", args[1]))));
                        }
                    } else {
                        player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().error.invalidArgs)));
                    }
                } else if (args[0].equalsIgnoreCase("removeFriend")) {
                    if (args.length > 1) {
                        Player player2 = SpigotMain.getInstance().getServer().getPlayer(args[1]);

                        if (player2 != null) {
                            if (SpigotMain.getInstance().getChests().get().players.get(player.getUniqueId().toString()).friends.contains(player2.getUniqueId().toString())) {
                                SpigotMain.getInstance().getChests().get().players.get(player.getUniqueId().toString()).friends.remove(player2.getUniqueId().toString());
                            }

                            player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().chestProtection.removedFriend.replace("{player}", args[1]))));
                        } else {
                            sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().error.playerNotFound.replace("{player}", args[1]))));
                        }
                    } else {
                        player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().error.invalidArgs)));
                    }
                } else {
                    Block block = player.getTargetBlock(Set.of(Material.AIR, Material.WATER, Material.LAVA), 8);

                    if (block != null) {
                        Boolean protectable = false;
                        Boolean allowUse = false;

                        for (String protectableSetting : SpigotMain.getInstance().getPluginConfig().get().chestProtection.protectable) {
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

                        if (protectable && (!SpigotMain.getInstance().getChests().get().blocks.containsKey(block.getX() + "," + block.getY() + "," + block.getZ() + "," + block.getWorld().getName()) || SpigotMain.getInstance().getChests().get().blocks.get(block.getX() + "," + block.getY() + "," + block.getZ() + "," + block.getWorld().getName()).owner.equals(player.getUniqueId().toString()))) {
                            if (args[0].equalsIgnoreCase("protect") && SpigotMain.getInstance().getChests().get().blocks.get(block.getX() + "," + block.getY() + "," + block.getZ() + "," + block.getWorld().getName()).owner.equals(player.getUniqueId().toString())) {
                                SpigotMain.getInstance().getChests().get().blocks.get(block.getX() + "," + block.getY() + "," + block.getZ() + "," + block.getWorld().getName()).isProtected = true;

                                player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().chestProtection.wasProtected)));
                            } else if (args[0].equalsIgnoreCase("unprotect") && SpigotMain.getInstance().getChests().get().blocks.get(block.getX() + "," + block.getY() + "," + block.getZ() + "," + block.getWorld().getName()).owner.equals(player.getUniqueId().toString())) {
                                SpigotMain.getInstance().getChests().get().blocks.get(block.getX() + "," + block.getY() + "," + block.getZ() + "," + block.getWorld().getName()).isProtected = false;

                                player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().chestProtection.unprotected)));
                            } else if (args[0].equalsIgnoreCase("claim") && !SpigotMain.getInstance().getChests().get().blocks.containsKey(block.getX() + "," + block.getY() + "," + block.getZ() + "," + block.getWorld().getName())) {
                                SpigotMain.getInstance().getChests().get().blocks.put(block.getX() + "," + block.getY() + "," + block.getZ() + "," + block.getWorld().getName(), new SpigotChests.Chest());
                                SpigotMain.getInstance().getChests().get().blocks.get(block.getX() + "," + block.getY() + "," + block.getZ() + "," + block.getWorld().getName()).isProtected = true;
                                SpigotMain.getInstance().getChests().get().blocks.get(block.getX() + "," + block.getY() + "," + block.getZ() + "," + block.getWorld().getName()).owner = player.getUniqueId().toString();
                                SpigotMain.getInstance().getChests().get().blocks.get(block.getX() + "," + block.getY() + "," + block.getZ() + "," + block.getWorld().getName()).allowUse = allowUse;
                                try {
                                    SpigotMain.getInstance().getChests().save();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().chestProtection.claimed)));
                            } else if (args[0].equalsIgnoreCase("unclaim") && SpigotMain.getInstance().getChests().get().blocks.get(block.getX() + "," + block.getY() + "," + block.getZ() + "," + block.getWorld().getName()).owner.equals(player.getUniqueId().toString())) {
                                SpigotMain.getInstance().getChests().get().blocks.remove(block.getX() + "," + block.getY() + "," + block.getZ() + "," + block.getWorld().getName());
                                try {
                                    SpigotMain.getInstance().getChests().save();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().chestProtection.unclaimed)));
                            } else if (args[0].equalsIgnoreCase("allowUse") && SpigotMain.getInstance().getChests().get().blocks.get(block.getX() + "," + block.getY() + "," + block.getZ() + "," + block.getWorld().getName()).owner.equals(player.getUniqueId().toString())) {
                                SpigotMain.getInstance().getChests().get().blocks.get(block.getX() + "," + block.getY() + "," + block.getZ() + "," + block.getWorld().getName()).allowUse = true;

                                player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().chestProtection.allowedUse)));
                            } else if (args[0].equalsIgnoreCase("disallowUse") && SpigotMain.getInstance().getChests().get().blocks.get(block.getX() + "," + block.getY() + "," + block.getZ() + "," + block.getWorld().getName()).owner.equals(player.getUniqueId().toString())) {
                                SpigotMain.getInstance().getChests().get().blocks.get(block.getX() + "," + block.getY() + "," + block.getZ() + "," + block.getWorld().getName()).allowUse = false;

                                player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().chestProtection.disallowedUse)));
                            }
                        } else {
                            player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().chestProtection.notYours)));
                        }
                    } else {
                        player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().chestProtection.noBlock)));
                    }
                }
            } else {
                player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().error.invalidArgs)));
            }
        } else {
            sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().error.noConsole)));
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
            list.add("disableAutoClaim");
            list.add("enableAutoClaim");
            list.add("addFriend");
            list.add("removeFriend");
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