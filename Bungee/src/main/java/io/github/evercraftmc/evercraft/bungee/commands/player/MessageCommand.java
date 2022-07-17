package io.github.evercraftmc.evercraft.bungee.commands.player;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.github.evercraftmc.evercraft.bungee.BungeeMain;
import io.github.evercraftmc.evercraft.bungee.commands.BungeeCommand;
import io.github.evercraftmc.evercraft.bungee.listeners.MessageListener;
import io.github.evercraftmc.evercraft.bungee.util.formatting.ComponentFormatter;
import io.github.evercraftmc.evercraft.shared.util.ModerationUtil;
import io.github.evercraftmc.evercraft.shared.util.StringUtils;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class MessageCommand extends BungeeCommand {
    public static Map<ProxiedPlayer, ProxiedPlayer> lastMessages = new HashMap<ProxiedPlayer, ProxiedPlayer>();

    public MessageCommand(String name, String description, List<String> aliases, String permission) {
        super(name, description, aliases, permission);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer player) {
            if (args.length > 1) {
                ProxiedPlayer player2 = BungeeMain.getInstance().getProxy().getPlayer(args[0]);

                if (player2 != null) {
                    StringBuilder message = new StringBuilder();

                    for (Integer i = 1; i < args.length; i++) {
                        message.append(args[i] + " ");
                    }

                    if (!BungeeMain.getInstance().getPluginData().getBoolean("players." + player.getUniqueId() + ".mute.muted")) {
                        if (ModerationUtil.isSuperInappropriateString(message.toString().trim())) {
                            BungeeMain.getInstance().getProxy().getPluginManager().dispatchCommand(BungeeMain.getInstance().getProxy().getConsole(), "tempmute " + player.getName() + " 1h No");
                        } else if (ModerationUtil.isInappropriateString(message.toString().trim())) {
                            if (MessageListener.warnings.containsKey(player.getUniqueId())) {
                                Integer value = MessageListener.warnings.get(player.getUniqueId()) + 1;
                                MessageListener.warnings.remove(player.getUniqueId());
                                MessageListener.warnings.put(player.getUniqueId(), value);
                            } else {
                                MessageListener.warnings.put(player.getUniqueId(), 1);
                            }

                            if (MessageListener.warnings.get(player.getUniqueId()) >= 1 && MessageListener.warnings.get(player.getUniqueId()) <= 5) {
                                for (ProxiedPlayer player3 : BungeeMain.getInstance().getProxy().getPlayers()) {
                                    player3.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().moderation.chat.warning.replace("{player}", player.getDisplayName()))));
                                }
                            } else if (MessageListener.warnings.get(player.getUniqueId()) > 5) {
                                BungeeMain.getInstance().getProxy().getPluginManager().dispatchCommand(BungeeMain.getInstance().getProxy().getConsole(), "tempmute " + player.getName() + " " + (5 * (MessageListener.warnings.get(player.getUniqueId()) - 5)) + "m Inappropriate language");
                            }
                        } else {
                            player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().chat.dm.replace("{player1}", "You").replace("{player2}", player2.getDisplayName()).replace("{message}", message.toString()))));
                            player2.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().chat.dm.replace("{player1}", player.getDisplayName()).replace("{player2}", "You").replace("{message}", message.toString()))));

                            if (lastMessages.containsKey(player)) {
                                lastMessages.remove(player);
                            }
                            lastMessages.put(player, player2);

                            if (lastMessages.containsKey(player2)) {
                                lastMessages.remove(player2);
                            }
                            lastMessages.put(player2, player);
                        }
                    } else {
                        String time = BungeeMain.getInstance().getPluginData().getString("players." + player.getUniqueId() + ".mute.until");

                        if (time.equalsIgnoreCase("forever") || Instant.parse(time).isAfter(Instant.now())) {
                            if (BungeeMain.getInstance().getPluginData().getString("players." + player.getUniqueId() + ".mute.reason") != null) {
                                player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().moderation.mute.reason.replace("{reason}", BungeeMain.getInstance().getPluginData().getString("players." + player.getUniqueId() + ".mute.reason")).replace("{moderator}", BungeeMain.getInstance().getPluginData().getString("players." + player.getUniqueId() + ".mute.by")).replace("{time}", time))));
                            } else {
                                player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().moderation.mute.noReason.replace("{moderator}", BungeeMain.getInstance().getPluginData().getString("players." + player.getUniqueId() + ".mute.by")).replace("{time}", time))));
                            }
                        } else {
                            BungeeMain.getInstance().getPluginData().set("players." + player.getUniqueId() + ".mute.muted", null);
                            BungeeMain.getInstance().getPluginData().set("players." + player.getUniqueId() + ".mute.reason", null);
                            BungeeMain.getInstance().getPluginData().set("players." + player.getUniqueId() + ".mute.by", null);
                            BungeeMain.getInstance().getPluginData().set("players." + player.getUniqueId() + ".mute.until", null);

                            run(sender, args);
                        }
                    }
                } else {
                    sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().error.playerNotFound.replace("{player}", args[0]))));
                }
            } else {
                sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().error.invalidArgs)));
            }
        } else {
            sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().error.noConsole)));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<String>();

        if (args.length == 1) {
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