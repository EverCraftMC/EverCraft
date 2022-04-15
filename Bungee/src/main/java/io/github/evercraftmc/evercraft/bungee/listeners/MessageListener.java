package io.github.evercraftmc.evercraft.bungee.listeners;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import io.github.evercraftmc.evercraft.bungee.BungeeMain;
import io.github.evercraftmc.evercraft.bungee.util.formatting.ComponentFormatter;
import io.github.evercraftmc.evercraft.bungee.util.player.BungeePlayerResolver;
import io.github.evercraftmc.evercraft.shared.util.ModerationUtil;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.event.EventHandler;

public class MessageListener extends BungeeListener {
    public Map<UUID, Integer> warnings = new HashMap<UUID, Integer>();

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
                    if (!ModerationUtil.isInappropriateString(message)) {
                        for (ProxiedPlayer player2 : BungeeMain.getInstance().getProxy().getPlayers()) {
                            if (player2.getServer().getInfo().getName().equals(sender)) {
                                player2.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("chat.default").replace("{player}", player.getDisplayName()).replace("{message}", message))));
                            } else {
                                player2.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("globalMessage").replace("{server}", sender).replace("{message}", BungeeMain.getInstance().getPluginMessages().getString("chat.default").replace("{player}", player.getDisplayName()).replace("{message}", message)))));
                            }
                        }

                        BungeeMain.getInstance().getDiscordBot().getGuild().getTextChannelById(BungeeMain.getInstance().getPluginConfig().getString("discord.channelId")).sendMessage(TextFormatter.discordFormat(BungeeMain.getInstance().getPluginMessages().getString("globalMessage").replace("{server}", sender).replace("{message}", BungeeMain.getInstance().getPluginMessages().getString("chat.default").replace("{player}", player.getDisplayName()).replace("{message}", message)))).queue();
                    } else if (message.matches("nigg(a)?(s)?") || message.contains("nig(g)?er(s)?")) {
                        BungeeMain.getInstance().getProxy().getPluginManager().dispatchCommand(BungeeMain.getInstance().getProxy().getConsole(), "tempmute " + player.getName() + " 15m Saying the freaking n word");
                    } else {
                        if (warnings.containsKey(player.getUniqueId())) {
                            Integer value = warnings.get(player.getUniqueId()) + 1;
                            warnings.remove(player.getUniqueId());
                            warnings.put(player.getUniqueId(), value);
                        } else {
                            warnings.put(player.getUniqueId(), 1);
                        }

                        if (warnings.get(player.getUniqueId()) == 1) {
                            player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("moderation.chat.warning1"))));
                        } else if (warnings.get(player.getUniqueId()) == 2) {
                            player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("moderation.chat.warning2"))));
                        } else if (warnings.get(player.getUniqueId()) == 3) {
                            player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("moderation.chat.warning3"))));
                        } else if (warnings.get(player.getUniqueId()) >= 4) {
                            BungeeMain.getInstance().getProxy().getPluginManager().dispatchCommand(BungeeMain.getInstance().getProxy().getConsole(), "tempmute " + player.getName() + " " + (5 * (warnings.get(player.getUniqueId()) - 3)) + "m Inappropriate language");
                        }
                    }
                } else {
                    String time = BungeeMain.getInstance().getData().getString("players." + player.getUniqueId() + ".mute.until");

                    if (time.equalsIgnoreCase("forever") || Instant.parse(time).isAfter(Instant.now())) {
                        if (BungeeMain.getInstance().getData().getString("players." + player.getUniqueId() + ".mute.reason") != null) {
                            player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("moderation.mute.reason").replace("{reason}", BungeeMain.getInstance().getData().getString("players." + player.getUniqueId() + ".mute.reason")).replace("{moderator}", BungeeMain.getInstance().getData().getString("players." + player.getUniqueId() + ".mute.by")).replace("{time}", time))));
                        } else {
                            player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("moderation.mute.noreason").replace("{moderator}", BungeeMain.getInstance().getData().getString("players." + player.getUniqueId() + ".mute.by")).replace("{time}", time))));
                        }
                    } else {
                        BungeeMain.getInstance().getData().set("players." + player.getUniqueId() + ".mute.muted", null);
                        BungeeMain.getInstance().getData().set("players." + player.getUniqueId() + ".mute.reason", null);
                        BungeeMain.getInstance().getData().set("players." + player.getUniqueId() + ".mute.by", null);
                        BungeeMain.getInstance().getData().set("players." + player.getUniqueId() + ".mute.until", null);
                    }
                }
            } else if (subChannel.equals("globalPlayerMessage")) {
                String sender = in.readUTF();

                ProxiedPlayer player = BungeeMain.getInstance().getProxy().getPlayer(UUID.fromString(in.readUTF()));

                String message = in.readUTF();

                for (ProxiedPlayer player2 : BungeeMain.getInstance().getProxy().getPlayers()) {
                    if (player2.getServer().getInfo().getName().equals(sender)) {
                        player2.sendMessage(ComponentFormatter.stringToComponent(message.replace("{player}", player.getDisplayName() + "&r")));
                    } else {
                        player2.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("globalMessage").replace("{server}", sender).replace("{message}", message.replace("{player}", player.getDisplayName() + "&r")))));
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

                if (message.startsWith("/bungeecommand")) {
                    message = "/" + message.substring(15);
                }

                if (!message.equalsIgnoreCase("/") && !message.equalsIgnoreCase("/ ")) {
                    for (ProxiedPlayer player2 : BungeeMain.getInstance().getProxy().getPlayers()) {
                        if (player2 != player && player2.hasPermission("evercraft.commands.staff.commandspy") && BungeeMain.getInstance().getData().getBoolean("players." + player2.getUniqueId() + ".commandspy")) {
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

    public void onCommand(ChatEvent event) {
        if (event.isProxyCommand()) {
            String message = event.getMessage();

            if (message.startsWith("/bungeecommand")) {
                message = "/" + message.substring(15);
            }

            if (!message.equalsIgnoreCase("/") && !message.equalsIgnoreCase("/ ")) {
                ProxiedPlayer player = BungeePlayerResolver.bungeePlayerFromPlayer(BungeePlayerResolver.playerFromConnection(event.getSender()));

                for (ProxiedPlayer player2 : BungeeMain.getInstance().getProxy().getPlayers()) {
                    if (player2 != player && player2.hasPermission("evercraft.commands.staff.commandspy") && BungeeMain.getInstance().getData().getBoolean("players." + player2.getUniqueId() + ".commandspy")) {
                        if (player2.getServer().getInfo().getName().equals(player.getServer().getInfo().getName())) {
                            player2.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("chat.commandSpy").replace("{player}", player.getDisplayName()).replace("{message}", message))));
                        } else {
                            player2.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("globalMessage").replace("{server}", player.getServer().getInfo().getName()).replace("{message}", BungeeMain.getInstance().getPluginMessages().getString("chat.commandSpy").replace("{player}", player.getDisplayName()).replace("{message}", message)))));
                        }
                    }
                }
            }
        }
    }
}