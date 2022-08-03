package io.github.evercraftmc.evercraft.bungee.commands.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import io.github.evercraftmc.evercraft.bungee.BungeeMain;
import io.github.evercraftmc.evercraft.bungee.commands.BungeeCommand;
import io.github.evercraftmc.evercraft.bungee.util.formatting.ComponentFormatter;
import io.github.evercraftmc.evercraft.bungee.util.player.BungeePlayerResolver;
import io.github.evercraftmc.evercraft.shared.PluginData;
import io.github.evercraftmc.evercraft.shared.util.StringUtils;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import io.github.evercraftmc.evercraft.shared.util.player.SimplePlayer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class FriendCommand extends BungeeCommand {
    public FriendCommand(String name, String description, List<String> aliases, String permission) {
        super(name, description, aliases, permission);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer player) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("add")) {
                    if (args.length > 1) {
                        ProxiedPlayer player2 = BungeeMain.getInstance().getProxy().getPlayer(args[1]);

                        if (player2 != null) {
                            Boolean hasInvite = false;

                            for (PluginData.Player.FriendInvite friendInvite : BungeeMain.getInstance().getPluginData().getParsed().players.get(player.getUniqueId().toString()).friendInvites) {
                                if (friendInvite.uuid.equals(player2.getUniqueId().toString()) && friendInvite.inbound == true) {
                                    hasInvite = true;
                                }
                            }

                            if (hasInvite) {
                                if (!BungeeMain.getInstance().getPluginData().getParsed().players.get(player.getUniqueId().toString()).friends.contains(player2.getUniqueId().toString())) {
                                    for (PluginData.Player.FriendInvite friendInvite : new ArrayList<PluginData.Player.FriendInvite>(BungeeMain.getInstance().getPluginData().getParsed().players.get(player.getUniqueId().toString()).friendInvites)) {
                                        if (friendInvite.uuid.equals(player2.getUniqueId().toString())) {
                                            BungeeMain.getInstance().getPluginData().getParsed().players.get(player2.getUniqueId().toString()).friendInvites.remove(friendInvite);
                                        }
                                    }

                                    for (PluginData.Player.FriendInvite friendInvite : new ArrayList<PluginData.Player.FriendInvite>(BungeeMain.getInstance().getPluginData().getParsed().players.get(player2.getUniqueId().toString()).friendInvites)) {
                                        if (friendInvite.uuid.equals(player.getUniqueId().toString())) {
                                            BungeeMain.getInstance().getPluginData().getParsed().players.get(player2.getUniqueId().toString()).friendInvites.remove(friendInvite);
                                        }
                                    }

                                    BungeeMain.getInstance().getPluginData().getParsed().players.get(player.getUniqueId().toString()).friends.add(player2.getUniqueId().toString());
                                    BungeeMain.getInstance().getPluginData().getParsed().players.get(player2.getUniqueId().toString()).friends.add(player.getUniqueId().toString());
                                    BungeeMain.getInstance().getPluginData().save();

                                    player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().friend.add.replace("{player}", player2.getDisplayName()))));
                                    player2.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().friend.added.replace("{player}", player.getDisplayName()))));
                                } else {
                                    player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().friend.alreadyFriends.replace("{player}", player2.getDisplayName()))));
                                }
                            } else {
                                Boolean alreadyInvited = false;

                                for (PluginData.Player.FriendInvite friendInvite : BungeeMain.getInstance().getPluginData().getParsed().players.get(player.getUniqueId().toString()).friendInvites) {
                                    if (friendInvite.uuid.equals(player2.getUniqueId().toString()) && friendInvite.inbound == false) {
                                        alreadyInvited = true;
                                    }
                                }

                                if (!alreadyInvited) {
                                    BungeeMain.getInstance().getPluginData().getParsed().players.get(player.getUniqueId().toString()).friendInvites.add(new PluginData.Player.FriendInvite(player2.getUniqueId().toString(), false));
                                    BungeeMain.getInstance().getPluginData().getParsed().players.get(player2.getUniqueId().toString()).friendInvites.add(new PluginData.Player.FriendInvite(player.getUniqueId().toString(), true));
                                    BungeeMain.getInstance().getPluginData().save();

                                    player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().friend.invite.replace("{player}", player2.getDisplayName()))));
                                    player2.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().friend.invited.replace("{player}", player.getDisplayName()))));
                                } else {
                                    player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().friend.alreadyInvited.replace("{player}", player2.getDisplayName()))));
                                }
                            }
                        } else {
                            player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().error.playerNotFound.replace("{player}", args[0]))));
                        }
                    } else {
                        player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().error.invalidArgs)));
                    }
                } else if (args[0].equalsIgnoreCase("remove")) {
                    if (args.length > 1) {
                        ProxiedPlayer player2 = BungeeMain.getInstance().getProxy().getPlayer(args[1]);

                        if (player2 != null) {
                            Boolean removed = false;

                            for (PluginData.Player.FriendInvite friendInvite : new ArrayList<PluginData.Player.FriendInvite>(BungeeMain.getInstance().getPluginData().getParsed().players.get(player.getUniqueId().toString()).friendInvites)) {
                                if (friendInvite.uuid.equals(player2.getUniqueId().toString())) {
                                    BungeeMain.getInstance().getPluginData().getParsed().players.get(player2.getUniqueId().toString()).friendInvites.remove(friendInvite);

                                    removed = true;
                                }
                            }

                            for (PluginData.Player.FriendInvite friendInvite : new ArrayList<PluginData.Player.FriendInvite>(BungeeMain.getInstance().getPluginData().getParsed().players.get(player2.getUniqueId().toString()).friendInvites)) {
                                if (friendInvite.uuid.equals(player.getUniqueId().toString())) {
                                    BungeeMain.getInstance().getPluginData().getParsed().players.get(player2.getUniqueId().toString()).friendInvites.remove(friendInvite);

                                    removed = true;
                                }
                            }

                            if (BungeeMain.getInstance().getPluginData().getParsed().players.get(player.getUniqueId().toString()).friends.add(player2.getUniqueId().toString())) {
                                BungeeMain.getInstance().getPluginData().getParsed().players.get(player.getUniqueId().toString()).friends.remove(player2.getUniqueId().toString());
                                BungeeMain.getInstance().getPluginData().getParsed().players.get(player2.getUniqueId().toString()).friends.remove(player.getUniqueId().toString());

                                removed = true;
                            }

                            if (removed) {
                                BungeeMain.getInstance().getPluginData().save();

                                player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().friend.remove.replace("{player}", player2.getDisplayName()))));
                                player2.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().friend.removed.replace("{player}", player.getDisplayName()))));
                            } else {
                                player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().friend.notFriends.replace("{player}", player2.getDisplayName()))));
                            }
                        } else {
                            player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().error.playerNotFound.replace("{player}", args[0]))));
                        }
                    } else {
                        player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().error.invalidArgs)));
                    }
                } else if (args[0].equalsIgnoreCase("list")) {
                    StringBuilder friends = new StringBuilder();

                    for (String friend : BungeeMain.getInstance().getPluginData().getParsed().players.get(player.getUniqueId().toString()).friends) {
                        SimplePlayer player2 = BungeePlayerResolver.getPlayer(BungeeMain.getInstance().getPluginData(), UUID.fromString(friend));

                        friends.append(player2.getDisplayName() + "\n&r&a");
                    }

                    if (BungeeMain.getInstance().getPluginData().getParsed().players.get(player.getUniqueId().toString()).friendInvites.size() > 0) {
                        StringBuilder friendInvites = new StringBuilder();

                        for (PluginData.Player.FriendInvite friend : BungeeMain.getInstance().getPluginData().getParsed().players.get(player.getUniqueId().toString()).friendInvites) {
                            SimplePlayer player2 = BungeePlayerResolver.getPlayer(BungeeMain.getInstance().getPluginData(), UUID.fromString(friend.uuid));

                            friendInvites.append((friend.inbound ? "&aInbound " : "&aOutbound ") + player2.getDisplayName() + "\n&r&a");
                        }

                        player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().friend.listInvites.replace("{friends}", friends.toString()).replace("{invites}", friendInvites.toString()))));
                    } else {
                        player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().friend.list.replace("{friends}", friends.toString()))));
                    }
                } else {
                    player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().error.invalidArgs)));
                }
            } else {
                player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().error.invalidArgs)));
            }
        } else {
            sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().error.noConsole)));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<String>();

        if (args.length == 1) {
            list.add("add");
            list.add("remove");
            list.add("list");
        } else if (args.length == 2 && (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove"))) {
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