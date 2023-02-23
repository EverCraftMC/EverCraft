package io.github.evercraftmc.evercraft.bungee.listeners;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
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
    public static Map<UUID, Integer> warnings = new HashMap<UUID, Integer>();

    @EventHandler
    public void onMessage(PluginMessageEvent event) {
        if (event.getTag().equals("BungeeCord")) {
            ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());

            String subChannel = in.readUTF();

            if (subChannel.equals("globalChat")) {
                String sender = in.readUTF();

                ProxiedPlayer player = BungeeMain.getInstance().getProxy().getPlayer(UUID.fromString(in.readUTF()));

                String message = in.readUTF();

                if (!BungeeMain.getInstance().getPluginData().get().players.get(player.getUniqueId().toString()).mute.muted) {
                    if (ModerationUtil.isSuperInappropriateString(message.trim())) {
                        BungeeMain.getInstance().getProxy().getPluginManager().dispatchCommand(BungeeMain.getInstance().getProxy().getConsole(), "tempmute " + player.getName() + " 1h No");
                    } else if (ModerationUtil.isInappropriateString(message.trim())) {
                        if (warnings.containsKey(player.getUniqueId())) {
                            Integer value = warnings.get(player.getUniqueId()) + 1;
                            warnings.remove(player.getUniqueId());
                            warnings.put(player.getUniqueId(), value);
                        } else {
                            warnings.put(player.getUniqueId(), 1);
                        }

                        if (warnings.get(player.getUniqueId()) >= 1 && warnings.get(player.getUniqueId()) <= 5) {
                            for (ProxiedPlayer player2 : BungeeMain.getInstance().getProxy().getPlayers()) {
                                player2.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().get().moderation.chat.warning.replace("{player}", player.getDisplayName()))));
                            }
                        } else if (warnings.get(player.getUniqueId()) > 5) {
                            BungeeMain.getInstance().getProxy().getPluginManager().dispatchCommand(BungeeMain.getInstance().getProxy().getConsole(), "tempmute " + player.getName() + " " + (5 * (warnings.get(player.getUniqueId()) - 5)) + "m Inappropriate language");
                        }
                    } else {
                        for (ProxiedPlayer player2 : BungeeMain.getInstance().getProxy().getPlayers()) {
                            if (message.toLowerCase().contains("@" + player2.getName().toLowerCase())) {
                                message = TextFormatter.translateColors(Pattern.compile("@" + player2.getName(), Pattern.LITERAL | Pattern.CASE_INSENSITIVE).matcher(message).replaceAll(TextFormatter.translateColors("&a@") + player2.getName() + TextFormatter.translateColors("&r")));

                                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                                out.writeUTF("playSound");
                                out.writeUTF(player2.getUniqueId().toString());
                                out.writeUTF("ENTITY_EXPERIENCE_ORB_PICKUP");
                                out.writeUTF("PLAYERS");
                                out.writeFloat(1);
                                out.writeFloat(1);

                                player2.getServer().sendData("BungeeCord", out.toByteArray());
                            }
                        }

                        for (ProxiedPlayer player2 : BungeeMain.getInstance().getProxy().getPlayers()) {
                            if (player2.getServer().getInfo().getName().equalsIgnoreCase(sender)) {
                                player2.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().get().chat.main.replace("{player}", player.getDisplayName()).replace("{message}", message))));
                            } else {
                                player2.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().get().globalMessage.replace("{server}", sender).replace("{message}", BungeeMain.getInstance().getPluginMessages().get().chat.main.replace("{player}", player.getDisplayName()).replace("{message}", message)))));
                            }
                        }

                        BungeeMain.getInstance().getDiscordBot().getGuild().getTextChannelById(BungeeMain.getInstance().getPluginConfig().get().discord.channelId).sendMessage(TextFormatter.discordFormat(BungeeMain.getInstance().getPluginMessages().get().globalMessage.replace("{server}", sender).replace("{message}", BungeeMain.getInstance().getPluginMessages().get().chat.main.replace("{player}", player.getDisplayName()).replace("{message}", message)))).queue();
                    }
                } else if (BungeeMain.getInstance().getPluginData().get().chatLock) {
                    player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().get().moderation.chatLock.chat)));
                } else {
                    String time = BungeeMain.getInstance().getPluginData().get().players.get(player.getUniqueId().toString()).mute.until;

                    if (time.equalsIgnoreCase("forever") || Instant.parse(time).isAfter(Instant.now())) {
                        if (BungeeMain.getInstance().getPluginData().get().players.get(player.getUniqueId().toString()).mute.reason != null) {
                            player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().get().moderation.mute.reason.replace("{reason}", BungeeMain.getInstance().getPluginData().get().players.get(player.getUniqueId().toString()).mute.reason).replace("{moderator}", BungeeMain.getInstance().getPluginData().get().players.get(player.getUniqueId().toString()).mute.by).replace("{time}", time))));
                        } else {
                            player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().get().moderation.mute.noReason.replace("{moderator}", BungeeMain.getInstance().getPluginData().get().players.get(player.getUniqueId().toString()).mute.by).replace("{time}", time))));
                        }
                    } else {
                        BungeeMain.getInstance().getPluginData().get().players.get(player.getUniqueId().toString()).mute.muted = false;
                        BungeeMain.getInstance().getPluginData().get().players.get(player.getUniqueId().toString()).mute.reason = null;
                        BungeeMain.getInstance().getPluginData().get().players.get(player.getUniqueId().toString()).mute.by = null;
                        BungeeMain.getInstance().getPluginData().get().players.get(player.getUniqueId().toString()).mute.until = null;
                        try {
                            BungeeMain.getInstance().getPluginData().save();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        onMessage(event);
                    }
                }
            } else if (subChannel.equals("globalPlayerMessage")) {
                String sender = in.readUTF();

                ProxiedPlayer player = BungeeMain.getInstance().getProxy().getPlayer(UUID.fromString(in.readUTF()));

                Boolean isJson = in.readBoolean();

                String message = in.readUTF();

                for (ProxiedPlayer player2 : BungeeMain.getInstance().getProxy().getPlayers()) {
                    if (!isJson) {
                        if (player2.getServer().getInfo().getName().equalsIgnoreCase(sender)) {
                            player2.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(message.replace("{player}", player.getDisplayName() + "&r"))));
                        } else {
                            player2.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().get().globalMessage.replace("{server}", sender).replace("{message}", message.replace("{player}", player.getDisplayName() + "&r")))));
                        }
                    } else {
                        if (player2.getServer().getInfo().getName().equalsIgnoreCase(sender)) {
                            player2.sendMessage(ComponentFormatter.jsonToComponent(message.replace("{player}", TextFormatter.translateColors(player.getDisplayName() + "&r"))));
                        } else {
                            player2.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().get().globalMessage.replace("{server}", sender).replace("{message}", ComponentFormatter.componentToString(ComponentFormatter.jsonToComponent(message)).replace("{player}", player.getDisplayName() + "&r")))));
                        }
                    }
                }

                if (!isJson) {
                    BungeeMain.getInstance().getDiscordBot().getGuild().getTextChannelById(BungeeMain.getInstance().getPluginConfig().get().discord.channelId).sendMessage(TextFormatter.discordFormat(BungeeMain.getInstance().getPluginMessages().get().globalMessage.replace("{server}", sender).replace("{message}", message.replace("{player}", player.getDisplayName())))).queue();
                } else {
                    BungeeMain.getInstance().getDiscordBot().getGuild().getTextChannelById(BungeeMain.getInstance().getPluginConfig().get().discord.channelId).sendMessage(TextFormatter.discordFormat(BungeeMain.getInstance().getPluginMessages().get().globalMessage.replace("{server}", sender).replace("{message}", ComponentFormatter.componentToString(ComponentFormatter.jsonToComponent(message)).replace("{player}", player.getDisplayName() + "&r")))).queue();
                }
            } else if (subChannel.equals("globalMessage")) {
                String sender = in.readUTF();

                Boolean isJson = in.readBoolean();

                String message = in.readUTF();

                for (ProxiedPlayer player : BungeeMain.getInstance().getProxy().getPlayers()) {
                    if (!isJson) {
                        if (player.getServer().getInfo().getName().equalsIgnoreCase(sender)) {
                            player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(message)));
                        } else {
                            player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().get().globalMessage.replace("{server}", sender).replace("{message}", message))));
                        }
                    } else {
                        if (player.getServer().getInfo().getName().equalsIgnoreCase(sender)) {
                            player.sendMessage(ComponentFormatter.jsonToComponent(message));
                        } else {
                            player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().get().globalMessage.replace("{server}", sender).replace("{message}", ComponentFormatter.componentToString(ComponentFormatter.jsonToComponent(message))))));
                        }
                    }
                }

                if (!isJson) {
                    BungeeMain.getInstance().getDiscordBot().getGuild().getTextChannelById(BungeeMain.getInstance().getPluginConfig().get().discord.channelId).sendMessage(TextFormatter.discordFormat(BungeeMain.getInstance().getPluginMessages().get().globalMessage.replace("{server}", sender).replace("{message}", in.readUTF()))).queue();
                } else {
                    BungeeMain.getInstance().getDiscordBot().getGuild().getTextChannelById(BungeeMain.getInstance().getPluginConfig().get().discord.channelId).sendMessage(TextFormatter.discordFormat(BungeeMain.getInstance().getPluginMessages().get().globalMessage.replace("{server}", sender).replace("{message}", ComponentFormatter.componentToString(ComponentFormatter.jsonToComponent(message))))).queue();
                }
            } else if (subChannel.equals("globalCommandSpy")) {
                String sender = in.readUTF();

                ProxiedPlayer player = BungeeMain.getInstance().getProxy().getPlayer(UUID.fromString(in.readUTF()));

                String message = in.readUTF().trim();

                if (message.startsWith("/bungeecommand")) {
                    message = "/" + message.substring(15);
                }

                if (!message.equalsIgnoreCase("/")) {
                    for (ProxiedPlayer player2 : BungeeMain.getInstance().getProxy().getPlayers()) {
                        if (player2 != player && player2.hasPermission("evercraft.commands.staff.commandspy") && BungeeMain.getInstance().getPluginData().get().players.get(player2.getUniqueId().toString()).settings.commandspy) {
                            if (!(!player2.hasPermission("evercraft.commands.staff.commandspy.extra") && (message.startsWith("/message") || message.startsWith("/msg") || message.startsWith("/reply") || message.startsWith("/r") || message.startsWith("/staffchat") || message.startsWith("/sc")))) {
                                if (player2.getServer().getInfo().getName().equalsIgnoreCase(sender)) {
                                    player2.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().get().chat.commandSpy.replace("{player}", player.getDisplayName()).replace("{message}", message))));
                                } else {
                                    player2.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().get().globalMessage.replace("{server}", sender).replace("{message}", BungeeMain.getInstance().getPluginMessages().get().chat.commandSpy.replace("{player}", player.getDisplayName()).replace("{message}", message)))));
                                }
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

    @EventHandler
    public void onCommand(ChatEvent event) {
        if (event.isProxyCommand()) {
            String message = event.getMessage();

            if (message.startsWith("/spigotcommand")) {
                message = "/" + message.substring(15);
            }

            if (!message.equalsIgnoreCase("/") && !message.equalsIgnoreCase("/ ")) {
                ProxiedPlayer player = BungeePlayerResolver.bungeePlayerFromPlayer(BungeePlayerResolver.playerFromConnection(BungeeMain.getInstance().getPluginData(), event.getSender()));

                for (ProxiedPlayer player2 : BungeeMain.getInstance().getProxy().getPlayers()) {
                    if (player2 != player && player2.hasPermission("evercraft.commands.staff.commandspy") && BungeeMain.getInstance().getPluginData().get().players.get(player2.getUniqueId().toString()).settings.commandspy) {
                        player2.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().get().chat.commandSpy.replace("{player}", player.getDisplayName()).replace("{message}", message))));
                    }
                }
            }
        }
    }
}