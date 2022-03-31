package com.kale_ko.evercraft.bungee.listeners;

import java.util.UUID;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.kale_ko.evercraft.bungee.BungeeMain;
import com.kale_ko.evercraft.bungee.util.formatting.ComponentFormatter;
import com.kale_ko.evercraft.shared.util.formatting.TextFormatter;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.event.EventHandler;

public class MessageListener extends BungeeListener {
    @EventHandler
    public void onMessage(PluginMessageEvent event) {
        if (event.getTag().equals("BungeeCord")) {
            ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());

            String subChannel = in.readUTF();

            if (subChannel.equals("globalChat")) {
                String sender = in.readUTF();

                ProxiedPlayer player = BungeeMain.getInstance().getProxy().getPlayer(UUID.fromString(in.readUTF()));

                String message = in.readUTF();

                if (!BungeeMain.getInstance().getData().getBoolean("players." + player.getUniqueId() + ".mute.muted")) {
                    for (ProxiedPlayer player2 : BungeeMain.getInstance().getProxy().getPlayers()) {
                        if (player2.getServer().getInfo().getName().equals(sender)) {
                            player2.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("chat.default").replace("{player}", player.getDisplayName()).replace("{message}", message))));
                        } else {
                            player2.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("globalMessage").replace("{server}", sender).replace("{message}", BungeeMain.getInstance().getPluginMessages().getString("chat.default").replace("{player}", player.getDisplayName()).replace("{message}", message)))));
                        }
                    }

                    BungeeMain.getInstance().getDiscordBot().getGuild().getTextChannelById(BungeeMain.getInstance().getPluginConfig().getString("discord.channelId")).sendMessage(TextFormatter.discordFormat(BungeeMain.getInstance().getPluginMessages().getString("globalMessage").replace("{server}", sender).replace("{message}", BungeeMain.getInstance().getPluginMessages().getString("chat.default").replace("{player}", player.getDisplayName()).replace("{message}", message)))).queue();
                } else {
                    String time = BungeeMain.getInstance().getData().getString("players." + player.getUniqueId() + ".mute.until");

                    if (BungeeMain.getInstance().getData().getString("players." + player.getUniqueId() + ".mute.reason") != null) {
                        player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("moderation.mute.reason").replace("{reason}", BungeeMain.getInstance().getData().getString("players." + player.getUniqueId() + ".mute.reason")).replace("{moderator}", BungeeMain.getInstance().getData().getString("players." + player.getUniqueId() + ".mute.by")).replace("{time}", time))));
                    } else {
                        player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("moderation.mute.noreason").replace("{moderator}", BungeeMain.getInstance().getData().getString("players." + player.getUniqueId() + ".mute.by")).replace("{time}", time))));
                    }
                }
            } else if (subChannel.equals("globalDeathMessage")) {
                String sender = in.readUTF();

                ProxiedPlayer player = BungeeMain.getInstance().getProxy().getPlayer(UUID.fromString(in.readUTF()));

                String message = in.readUTF();

                for (ProxiedPlayer player2 : BungeeMain.getInstance().getProxy().getPlayers()) {
                    if (player2.getServer().getInfo().getName().equals(sender)) {
                        player2.sendMessage(ComponentFormatter.stringToComponent(message.replace("{player}", player.getDisplayName())));
                    } else {
                        player2.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("globalMessage").replace("{server}", sender).replace("{message}", message.replace("{player}", player.getDisplayName())))));
                    }
                }

                BungeeMain.getInstance().getDiscordBot().getGuild().getTextChannelById(BungeeMain.getInstance().getPluginConfig().getString("discord.channelId")).sendMessage(TextFormatter.discordFormat(BungeeMain.getInstance().getPluginMessages().getString("globalMessage").replace("{server}", sender).replace("{message}", message.replace("{player}", player.getDisplayName())))).queue();
            } else if (subChannel.equals("globalMessage")) {
                String sender = in.readUTF();

                String message = in.readUTF();

                for (ProxiedPlayer player : BungeeMain.getInstance().getProxy().getPlayers()) {
                    if (player.getServer().getInfo().getName().equals(sender)) {
                        player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(message)));
                    } else {
                        player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("globalMessage").replace("{server}", sender).replace("{message}", message))));
                    }
                }

                BungeeMain.getInstance().getDiscordBot().getGuild().getTextChannelById(BungeeMain.getInstance().getPluginConfig().getString("discord.channelId")).sendMessage(TextFormatter.discordFormat(BungeeMain.getInstance().getPluginMessages().getString("globalMessage").replace("{server}", sender).replace("{message}", in.readUTF()))).queue();
            } else if (subChannel.equals("globalCommandSpy")) {
                String sender = in.readUTF();

                ProxiedPlayer player = BungeeMain.getInstance().getProxy().getPlayer(UUID.fromString(in.readUTF()));

                String message = in.readUTF();

                if (!message.equalsIgnoreCase("/") && !message.equalsIgnoreCase("/ ")) {
                    for (ProxiedPlayer player2 : BungeeMain.getInstance().getProxy().getPlayers()) {
                        if (player2 != player && player2.hasPermission("evercraft.commands.staff.commandspy") && BungeeMain.getInstance().getData().getBoolean("players." + player.getUniqueId() + ".commandspy")) {
                            if (player2.getServer().getInfo().getName().equals(sender)) {
                                player2.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("chat.commandSpy").replace("{player}", player.getDisplayName()).replace("{message}", message))));
                            } else {
                                player2.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("globalMessage").replace("{server}", sender).replace("{message}", BungeeMain.getInstance().getPluginMessages().getString("chat.commandSpy").replace("{player}", player.getDisplayName()).replace("{message}", message)))));
                            }
                        }
                    }
                }
            } else if (subChannel.equals("crossCommand")) {
                ProxiedPlayer player = BungeeMain.getInstance().getProxy().getPlayer(UUID.fromString(in.readUTF()));

                String command = in.readUTF();

                BungeeMain.getInstance().getProxy().getPluginManager().dispatchCommand(player, command);
            }
        }
    }
}