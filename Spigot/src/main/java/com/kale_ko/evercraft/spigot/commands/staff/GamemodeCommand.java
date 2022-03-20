package com.kale_ko.evercraft.spigot.commands.staff;

import java.util.List;
import com.kale_ko.evercraft.spigot.SpigotPlugin;
import com.kale_ko.evercraft.spigot.Util;
import com.kale_ko.evercraft.spigot.commands.SpigotCommand;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GamemodeCommand extends SpigotCommand {
    public GamemodeCommand(String name, String description, List<String> aliases, String usage, String permission) {
        super(name, description, aliases, usage, permission);
    }

    @Override
    public void run(CommandSender sender, String label, String[] args) {
        if (label.equalsIgnoreCase("gamemode")) {
            if (args.length == 1) {
                if (sender instanceof Player player) {
                    if (args[0].equalsIgnoreCase("c") || args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("1")) {
                        if (Util.hasPermission(player, "evercraft.commands.staff.gamemode.creative")) {
                            player.setGameMode(GameMode.CREATIVE);

                            Util.sendMessage(player, SpigotPlugin.Instance.config.getString("messages.gamemode").replace("{gamemode}", "creative"));
                        } else {
                            Util.sendMessage(player, SpigotPlugin.Instance.config.getString("messages.noperms").replace("{permission}", "evercraft.commands.staff.gamemode.creative"));
                        }
                    } else if (args[0].equalsIgnoreCase("s") || args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("0")) {
                        if (Util.hasPermission(player, "evercraft.commands.staff.gamemode.survival")) {
                            player.setGameMode(GameMode.SURVIVAL);

                            Util.sendMessage(player, SpigotPlugin.Instance.config.getString("messages.gamemode").replace("{gamemode}", "survival"));
                        } else {
                            Util.sendMessage(player, SpigotPlugin.Instance.config.getString("messages.noperms").replace("{permission}", "evercraft.commands.staff.gamemode.survival"));
                        }
                    } else if (args[0].equalsIgnoreCase("a") || args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("2")) {
                        if (Util.hasPermission(player, "evercraft.commands.staff.gamemode.adventure")) {
                            player.setGameMode(GameMode.ADVENTURE);

                            Util.sendMessage(player, SpigotPlugin.Instance.config.getString("messages.gamemode").replace("{gamemode}", "adventure"));
                        } else {
                            Util.sendMessage(player, SpigotPlugin.Instance.config.getString("messages.noperms").replace("{permission}", "evercraft.commands.staff.gamemode.adventure"));
                        }
                    } else if (args[0].equalsIgnoreCase("sp") || args[0].equalsIgnoreCase("spectator") || args[0].equalsIgnoreCase("3")) {
                        if (Util.hasPermission(player, "evercraft.commands.staff.gamemode.spectator")) {
                            player.setGameMode(GameMode.SPECTATOR);

                            Util.sendMessage(player, SpigotPlugin.Instance.config.getString("messages.gamemode").replace("{gamemode}", "spectator"));
                        } else {
                            Util.sendMessage(player, SpigotPlugin.Instance.config.getString("messages.noperms").replace("{permission}", "evercraft.commands.staff.gamemode.spectator"));
                        }
                    }
                }
            } else if (args.length == 2) {
                if (Util.hasPermission(sender, "evercraft.commands.staff.sudo")) {
                    Player player = SpigotPlugin.Instance.getServer().getPlayer(args[0]);

                    if (player != null) {
                        if (args[1].equalsIgnoreCase("c") || args[1].equalsIgnoreCase("creative") || args[1].equalsIgnoreCase("1")) {
                            if (Util.hasPermission(player, "evercraft.commands.staff.gamemode.creative")) {
                                player.setGameMode(GameMode.CREATIVE);
    
                                Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.gamemode").replace("{gamemode}", "creative"));
                            } else {
                                Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.noperms").replace("{permission}", "evercraft.commands.staff.gamemode.creative"));
                            }
                        } else if (args[1].equalsIgnoreCase("s") || args[1].equalsIgnoreCase("survival") || args[1].equalsIgnoreCase("0")) {
                            if (Util.hasPermission(player, "evercraft.commands.staff.gamemode.survival")) {
                                player.setGameMode(GameMode.SURVIVAL);
    
                                Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.gamemode").replace("{gamemode}", "survival"));
                            } else {
                                Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.noperms").replace("{permission}", "evercraft.commands.staff.gamemode.survival"));
                            }
                        } else if (args[1].equalsIgnoreCase("a") || args[1].equalsIgnoreCase("adventure") || args[1].equalsIgnoreCase("2")) {
                            if (Util.hasPermission(player, "evercraft.commands.staff.gamemode.adventure")) {
                                player.setGameMode(GameMode.ADVENTURE);
    
                                Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.gamemode").replace("{gamemode}", "adventure"));
                            } else {
                                Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.noperms").replace("{permission}", "evercraft.commands.staff.gamemode.adventure"));
                            }
                        } else if (args[1].equalsIgnoreCase("sp") || args[1].equalsIgnoreCase("spectator") || args[1].equalsIgnoreCase("3")) {
                            if (Util.hasPermission(player, "evercraft.commands.staff.gamemode.spectator")) {
                                player.setGameMode(GameMode.SPECTATOR);
    
                                Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.gamemode").replace("{gamemode}", "spectator"));
                            } else {
                                Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.noperms").replace("{permission}", "evercraft.commands.staff.gamemode.spectator"));
                            }
                        }
                    } else {
                        Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.playernotfound").replace("{player}", args[0]));
                    }
                } else {
                    Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.noperms").replace("{permission}", "evercraft.commands.staff.sudo"));
                }
            }
        } else {
            if (args.length == 0) {
                if (sender instanceof Player player) {
                    if (label.equalsIgnoreCase("gmc")) {
                        if (Util.hasPermission(player, "evercraft.commands.staff.gamemode.creative")) {
                            player.setGameMode(GameMode.CREATIVE);

                            Util.sendMessage(player, SpigotPlugin.Instance.config.getString("messages.gamemode").replace("{gamemode}", "creative"));
                        } else {
                            Util.sendMessage(player, SpigotPlugin.Instance.config.getString("messages.noperms").replace("{permission}", "evercraft.commands.staff.gamemode.creative"));
                        }
                    } else if (label.equalsIgnoreCase("gms")) {
                        if (Util.hasPermission(player, "evercraft.commands.staff.gamemode.survival")) {
                            player.setGameMode(GameMode.SURVIVAL);

                            Util.sendMessage(player, SpigotPlugin.Instance.config.getString("messages.gamemode").replace("{gamemode}", "survival"));
                        } else {
                            Util.sendMessage(player, SpigotPlugin.Instance.config.getString("messages.noperms").replace("{permission}", "evercraft.commands.staff.gamemode.survival"));
                        }
                    } else if (label.equalsIgnoreCase("gma")) {
                        if (Util.hasPermission(player, "evercraft.commands.staff.gamemode.adventure")) {
                            player.setGameMode(GameMode.ADVENTURE);

                            Util.sendMessage(player, SpigotPlugin.Instance.config.getString("messages.gamemode").replace("{gamemode}", "adventure"));
                        } else {
                            Util.sendMessage(player, SpigotPlugin.Instance.config.getString("messages.noperms").replace("{permission}", "evercraft.commands.staff.gamemode.adventure"));
                        }
                    } else if (label.equalsIgnoreCase("gmsp")) {
                        if (Util.hasPermission(player, "evercraft.commands.staff.gamemode.spectator")) {
                            player.setGameMode(GameMode.SPECTATOR);

                            Util.sendMessage(player, SpigotPlugin.Instance.config.getString("messages.gamemode").replace("{gamemode}", "spectator"));
                        } else {
                            Util.sendMessage(player, SpigotPlugin.Instance.config.getString("messages.noperms").replace("{permission}", "evercraft.commands.staff.gamemode.spectator"));
                        }
                    }
                } else {
                    Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.noconsole"));
                }
            } else {
                if (Util.hasPermission(sender, "evercraft.commands.staff.sudo")) {
                    Player player = SpigotPlugin.Instance.getServer().getPlayer(args[0]);

                    if (player != null) {
                        if (label.equalsIgnoreCase("gmc")) {
                            if (Util.hasPermission(player, "evercraft.commands.staff.gamemode.creative")) {
                                player.setGameMode(GameMode.CREATIVE);
    
                                Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.gamemode").replace("{gamemode}", "creative"));
                            } else {
                                Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.noperms").replace("{permission}", "evercraft.commands.staff.gamemode.creative"));
                            }
                        } else if (label.equalsIgnoreCase("gms")) {
                            if (Util.hasPermission(player, "evercraft.commands.staff.gamemode.survival")) {
                                player.setGameMode(GameMode.SURVIVAL);
    
                                Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.gamemode").replace("{gamemode}", "survival"));
                            } else {
                                Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.noperms").replace("{permission}", "evercraft.commands.staff.gamemode.survival"));
                            }
                        } else if (label.equalsIgnoreCase("gma")) {
                            if (Util.hasPermission(player, "evercraft.commands.staff.gamemode.adventure")) {
                                player.setGameMode(GameMode.ADVENTURE);
    
                                Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.gamemode").replace("{gamemode}", "adventure"));
                            } else {
                                Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.noperms").replace("{permission}", "evercraft.commands.staff.gamemode.adventure"));
                            }
                        } else if (label.equalsIgnoreCase("gmsp")) {
                            if (Util.hasPermission(player, "evercraft.commands.staff.gamemode.spectator")) {
                                player.setGameMode(GameMode.SPECTATOR);
    
                                Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.gamemode").replace("{gamemode}", "spectator"));
                            } else {
                                Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.noperms").replace("{permission}", "evercraft.commands.staff.gamemode.spectator"));
                            }
                        }
                    } else {
                        Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.playernotfound").replace("{player}", args[0]));
                    }
                } else {
                    Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.noperms").replace("{permission}", "evercraft.commands.staff.sudo"));
                }
            }
        }
    }
}