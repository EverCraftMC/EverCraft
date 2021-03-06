package io.github.evercraftmc.evercraft.bungee.commands.staff;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import io.github.evercraftmc.evercraft.bungee.BungeeMain;
import io.github.evercraftmc.evercraft.bungee.commands.BungeeCommand;
import io.github.evercraftmc.evercraft.bungee.util.formatting.ComponentFormatter;
import io.github.evercraftmc.evercraft.shared.util.StringUtils;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class SudoCommand extends BungeeCommand {
    public SudoCommand(String name, String description, List<String> aliases, String permission) {
        super(name, description, aliases, permission);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (args.length > 1) {
            ProxiedPlayer player = BungeeMain.getInstance().getProxy().getPlayer(args[0]);

            if (player != null || args[0].equalsIgnoreCase("*")) {
                StringBuilder message = new StringBuilder();

                for (Integer i = 1; i < args.length; i++) {
                    message.append(args[i] + " ");
                }

                if (message.toString().startsWith("/")) {
                    Boolean found = false;

                    for (Map.Entry<String, Command> command : BungeeMain.getInstance().getProxy().getPluginManager().getCommands()) {
                        if (command.getValue().getName().equalsIgnoreCase(args[1].substring(1))) {
                            found = true;

                            if (args[0].equalsIgnoreCase("*")) {
                                for (ProxiedPlayer player2 : BungeeMain.getInstance().getProxy().getPlayers()) {
                                    BungeeMain.getInstance().getProxy().getPluginManager().dispatchCommand(player2, message.toString().trim().substring(1));
                                }
                            } else {
                                BungeeMain.getInstance().getProxy().getPluginManager().dispatchCommand(player, message.toString().trim().substring(1));
                            }

                            return;
                        } else {
                            for (String alias : command.getValue().getAliases()) {
                                if (alias.equalsIgnoreCase(args[1].substring(1))) {
                                    found = true;

                                    if (args[0].equalsIgnoreCase("*")) {
                                        for (ProxiedPlayer player2 : BungeeMain.getInstance().getProxy().getPlayers()) {
                                            BungeeMain.getInstance().getProxy().getPluginManager().dispatchCommand(player2, message.toString().trim().substring(1));
                                        }
                                    } else {
                                        BungeeMain.getInstance().getProxy().getPluginManager().dispatchCommand(player, message.toString().trim().substring(1));
                                    }

                                    return;
                                }
                            }
                        }
                    }

                    if (!found) {
                        if (args[0].equalsIgnoreCase("*")) {
                            for (ProxiedPlayer player2 : BungeeMain.getInstance().getProxy().getPlayers()) {
                                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                                out.writeUTF("crossCommand");
                                out.writeUTF(player2.getUniqueId().toString());
                                out.writeUTF(message.toString().trim().substring(1));

                                player2.getServer().sendData("BungeeCord", out.toByteArray());
                            }
                        } else {
                            ByteArrayDataOutput out = ByteStreams.newDataOutput();
                            out.writeUTF("crossCommand");
                            out.writeUTF(player.getUniqueId().toString());
                            out.writeUTF(message.toString().trim().substring(1));

                            player.getServer().sendData("BungeeCord", out.toByteArray());
                        }
                    }

                    if (!args[0].equalsIgnoreCase("*")) {
                        sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().sudo.command.replace("{command}", message.toString().trim()).replace("{player}", player.getDisplayName()))));
                    } else {
                        sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().sudo.command.replace("{command}", message.toString().trim()).replace("{player}", "*"))));
                    }
                } else {
                    if (args[0].equalsIgnoreCase("*")) {
                        for (ProxiedPlayer player2 : BungeeMain.getInstance().getProxy().getPlayers()) {
                            for (ProxiedPlayer player3 : BungeeMain.getInstance().getProxy().getPlayers()) {
                                if (player3.getServer().getInfo().getName().equalsIgnoreCase(player2.getServer().getInfo().getName())) {
                                    player3.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().chat.main.replace("{player}", player2.getDisplayName()).replace("{message}", message))));
                                } else {
                                    player3.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().globalMessage.replace("{server}", player2.getServer().getInfo().getName()).replace("{message}", BungeeMain.getInstance().getPluginMessages().getParsed().chat.main.replace("{player}", player2.getDisplayName()).replace("{message}", message)))));
                                }
                            }

                            BungeeMain.getInstance().getDiscordBot().getGuild().getTextChannelById(BungeeMain.getInstance().getPluginConfig().getParsed().discord.channelId).sendMessage(TextFormatter.discordFormat(BungeeMain.getInstance().getPluginMessages().getParsed().globalMessage.replace("{server}", player2.getServer().getInfo().getName()).replace("{message}", BungeeMain.getInstance().getPluginMessages().getParsed().chat.main.replace("{player}", player2.getDisplayName()).replace("{message}", message)))).queue();
                        }
                    } else {
                        for (ProxiedPlayer player2 : BungeeMain.getInstance().getProxy().getPlayers()) {
                            if (player2.getServer().getInfo().getName().equalsIgnoreCase(player.getServer().getInfo().getName())) {
                                player2.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().chat.main.replace("{player}", player.getDisplayName()).replace("{message}", message))));
                            } else {
                                player2.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().globalMessage.replace("{server}", player.getServer().getInfo().getName()).replace("{message}", BungeeMain.getInstance().getPluginMessages().getParsed().chat.main.replace("{player}", player.getDisplayName()).replace("{message}", message)))));
                            }
                        }

                        BungeeMain.getInstance().getDiscordBot().getGuild().getTextChannelById(BungeeMain.getInstance().getPluginConfig().getParsed().discord.channelId).sendMessage(TextFormatter.discordFormat(BungeeMain.getInstance().getPluginMessages().getParsed().globalMessage.replace("{server}", player.getServer().getInfo().getName()).replace("{message}", BungeeMain.getInstance().getPluginMessages().getParsed().chat.main.replace("{player}", player.getDisplayName()).replace("{message}", message)))).queue();
                    }

                    if (!args[0].equalsIgnoreCase("*")) {
                        sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().sudo.message.replace("{message}", message.toString().trim()).replace("{player}", player.getDisplayName()))));
                    } else {
                        sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().sudo.message.replace("{message}", message.toString().trim()).replace("{player}", "*"))));
                    }
                }
            } else {
                sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().error.playerNotFound.replace("{player}", args[0]))));
            }
        } else {
            sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().error.invalidArgs)));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<String>();

        if (args.length == 1) {
            list.add("*");

            for (ProxiedPlayer player : BungeeMain.getInstance().getProxy().getPlayers()) {
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