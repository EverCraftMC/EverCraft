package io.github.evercraftmc.global.commands;

import io.github.evercraftmc.core.api.commands.ECCommand;
import io.github.evercraftmc.core.api.server.player.ECConsole;
import io.github.evercraftmc.core.api.server.player.ECPlayer;
import io.github.evercraftmc.core.impl.util.ECTextFormatter;
import io.github.evercraftmc.global.GlobalModule;
import java.util.ArrayList;
import java.util.List;

public class PrefixCommand implements ECCommand {
    protected final GlobalModule parent;

    public PrefixCommand(GlobalModule parent) {
        this.parent = parent;
    }

    @Override
    public String getName() {
        return "prefix";
    }

    @Override
    public String getDescription() {
        return "Change your prefix";
    }

    @Override
    public List<String> getAlias() {
        return List.of("setPrefix");
    }

    @Override
    public String getPermission() {
        return "evercraft.global.commands.prefix";
    }

    @Override
    public void run(ECPlayer player, String[] args, boolean sendFeedback) {
        if (!(player instanceof ECConsole)) {
            if (args.length > 0) {
                ECPlayer otherPlayer = parent.getPlugin().getServer().getOnlinePlayer(args[0]);

                if (otherPlayer != null && player.hasPermission("evercraft.global.commands.prefix.other")) {
                    if (args.length == 1) {
                        this.parent.getPlugin().getPlayerData().players.get(otherPlayer.getUuid().toString()).prefix = otherPlayer.getName();
                        this.parent.getPlugin().saveData();

                        if (sendFeedback) {
                            player.sendMessage(ECTextFormatter.translateColors("&a" + otherPlayer.getDisplayName() + "&r&a's prefix has been reset."));
                        }
                    } else if (args.length == 2) {
                        if (ECTextFormatter.stripColors(args[0]).length() <= 16 && args[0].length() <= 32) {
                            if (args[0].equalsIgnoreCase("reset")) {
                                this.parent.getPlugin().getPlayerData().players.get(otherPlayer.getUuid().toString()).prefix = otherPlayer.getName();
                                this.parent.getPlugin().saveData();

                                if (sendFeedback) {
                                    player.sendMessage(ECTextFormatter.translateColors("&a" + otherPlayer.getDisplayName() + "&r&a's prefix has been reset."));
                                }
                            } else {
                                this.parent.getPlugin().getPlayerData().players.get(otherPlayer.getUuid().toString()).prefix = args[0];
                                this.parent.getPlugin().saveData();

                                if (sendFeedback) {
                                    player.sendMessage(ECTextFormatter.translateColors("&aSuccessfully set " + otherPlayer.getDisplayName() + "&r&a's prefix to &r" + args[0] + "&r&a."));
                                }
                            }
                        } else if (sendFeedback) {
                            player.sendMessage(ECTextFormatter.translateColors("&cThat prefix is too long."));
                        }
                    } else if (sendFeedback) {
                        player.sendMessage(ECTextFormatter.translateColors("&cYour prefix can't contain spaces."));
                    }
                } else {
                    if (args.length == 1) {
                        if (ECTextFormatter.stripColors(args[0]).length() <= 16 && args[0].length() <= 32) {
                            if (args[0].equalsIgnoreCase("reset")) {
                                this.parent.getPlugin().getPlayerData().players.get(player.getUuid().toString()).prefix = null;
                                this.parent.getPlugin().saveData();

                                if (sendFeedback) {
                                    player.sendMessage(ECTextFormatter.translateColors("&aYour prefix has been reset."));
                                }
                            } else {
                                this.parent.getPlugin().getPlayerData().players.get(player.getUuid().toString()).prefix = args[0];
                                this.parent.getPlugin().saveData();

                                if (sendFeedback) {
                                    player.sendMessage(ECTextFormatter.translateColors("&aSuccessfully set your prefix to &r" + args[0] + "&r&a."));
                                }
                            }
                        } else if (sendFeedback) {
                            player.sendMessage(ECTextFormatter.translateColors("&cThat prefix is too long."));
                        }
                    } else if (sendFeedback) {
                        player.sendMessage(ECTextFormatter.translateColors("&cYour prefix can't contain spaces."));
                    }
                }
            } else {
                this.parent.getPlugin().getPlayerData().players.get(player.getUuid().toString()).prefix = null;
                this.parent.getPlugin().saveData();

                if (sendFeedback) {
                    player.sendMessage(ECTextFormatter.translateColors("&aYour prefix has been reset."));
                }
            }

            player.setDisplayName(ECTextFormatter.translateColors((parent.getPlugin().getPlayerData().players.get(player.getUuid().toString()).prefix != null ? parent.getPlugin().getPlayerData().players.get(player.getUuid().toString()).prefix + "&r " : "&r") + parent.getPlugin().getPlayerData().players.get(player.getUuid().toString()).displayName + "&r"));
        } else if (sendFeedback) {
            player.sendMessage(ECTextFormatter.translateColors("&cYou can't do that from the console."));
        }
    }

    @Override
    public List<String> tabComplete(ECPlayer player, String[] args) {
        if (args.length == 1) {
            if (player.hasPermission("evercraft.global.commands.prefix.other")) {
                List<String> players = new ArrayList<>();
                players.add("reset");
                for (ECPlayer player2 : parent.getPlugin().getServer().getOnlinePlayers()) {
                    players.add(player2.getName());
                }
                return players;
            }

            return List.of("reset");
        } else if (args.length == 2 && player.hasPermission("evercraft.global.commands.prefix.other")) {
            return List.of("reset");
        } else {
            return List.of();
        }
    }
}