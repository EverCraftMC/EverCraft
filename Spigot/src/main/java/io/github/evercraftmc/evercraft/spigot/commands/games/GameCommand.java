package io.github.evercraftmc.evercraft.spigot.commands.games;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import io.github.evercraftmc.evercraft.shared.util.StringUtils;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import io.github.evercraftmc.evercraft.spigot.SpigotMain;
import io.github.evercraftmc.evercraft.spigot.commands.SpigotCommand;
import io.github.evercraftmc.evercraft.spigot.games.Game;
import io.github.evercraftmc.evercraft.spigot.games.TeamedGame;
import io.github.evercraftmc.evercraft.spigot.games.Game.LeaveReason;
import io.github.evercraftmc.evercraft.spigot.util.formatting.ComponentFormatter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GameCommand extends SpigotCommand {
    public GameCommand(String name, String description, List<String> aliases, String permission) {
        super(name, description, aliases, permission);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("join")) {
                    if (args.length >= 2) {
                        Boolean inGame = false;
                        for (Game game : SpigotMain.getInstance().getRegisteredGames()) {
                            if (game.isPlaying(player)) {
                                inGame = true;
                            }
                        }

                        if (!inGame) {
                            Boolean joinedGame = false;
                            for (Game game : SpigotMain.getInstance().getRegisteredGames()) {
                                if (game.getName().equalsIgnoreCase(args[1])) {
                                    game.join(player);

                                    joinedGame = true;
                                }
                            }

                            if (!joinedGame) {
                                player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("games.notFound").replace("{game}", args[1]))));
                            }
                        } else {
                            player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("games.alreadyInGame"))));
                        }
                    } else {
                        player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("error.invalidArgs"))));
                    }
                } else if (args[0].equalsIgnoreCase("leave")) {
                    Boolean inGame = false;
                    for (Game game : SpigotMain.getInstance().getRegisteredGames()) {
                        if (game.isPlaying(player)) {
                            inGame = true;
                        }
                    }

                    if (inGame) {
                        for (Game game : SpigotMain.getInstance().getRegisteredGames()) {
                            if (game.isPlaying(player)) {
                                game.leave(player, LeaveReason.COMMAND);
                            }
                        }
                    } else {
                        player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("games.notInGame"))));
                    }
                } else if (args[0].equalsIgnoreCase("team")) {
                    if (args.length >= 2) {
                        if (args[1].equalsIgnoreCase("join")) {
                            if (args.length >= 3) {
                                Boolean inGame = false;
                                for (Game game : SpigotMain.getInstance().getRegisteredGames()) {
                                    if (game.isPlaying(player)) {
                                        inGame = true;
                                    }
                                }

                                if (inGame) {
                                    for (Game game : SpigotMain.getInstance().getRegisteredGames()) {
                                        if (game instanceof TeamedGame teamedGame) {
                                            if (teamedGame.isPlaying(player)) {
                                                if (teamedGame.getTeam(player) == null) {
                                                    teamedGame.joinTeam(player, args[2]);
                                                } else {
                                                    teamedGame.leaveTeam(player);
                                                    teamedGame.joinTeam(player, args[2]);
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("games.notInGame"))));
                                }
                            }
                        } else if (args[1].equalsIgnoreCase("leave")) {
                            Boolean inGame = false;
                            Boolean inTeam = false;
                            for (Game game : SpigotMain.getInstance().getRegisteredGames()) {
                                if (game.isPlaying(player)) {
                                    inGame = true;

                                    if (game instanceof TeamedGame teamGame) {
                                        if (teamGame.getTeam(player) != null) {
                                            inTeam = true;
                                        }
                                    }
                                }
                            }

                            if (inGame) {
                                if (inTeam) {
                                    for (Game game : SpigotMain.getInstance().getRegisteredGames()) {
                                        if (game instanceof TeamedGame teamedGame) {
                                            if (teamedGame.isPlaying(player)) {
                                                teamedGame.leaveTeam(player);
                                            }
                                        }
                                    }
                                } else {
                                    player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("games.notInGame"))));
                                }
                            } else {
                                player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("games.notInGame"))));
                            }
                        } else {
                            player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("error.invalidArgs"))));
                        }
                    } else {
                        player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("error.invalidArgs"))));
                    }
                } else {
                    player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("error.invalidArgs"))));
                }
            } else {
                player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("error.invalidArgs"))));
            }
        } else {
            sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("error.noConsole"))));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<String>();

        if (args.length == 1) {
            list.add("join");
            list.add("leave");
            list.add("team");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("join")) {
                for (Game game : SpigotMain.getInstance().getRegisteredGames()) {
                    list.add(game.getName());
                }
            } else if (args[0].equalsIgnoreCase("team")) {
                list.add("join");
                list.add("leave");
            } else {
                return Arrays.asList();
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