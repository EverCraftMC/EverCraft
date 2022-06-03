package io.github.evercraftmc.evercraft.spigot.commands.games;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import io.github.evercraftmc.evercraft.shared.util.StringUtils;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import io.github.evercraftmc.evercraft.spigot.SpigotMain;
import io.github.evercraftmc.evercraft.spigot.commands.SpigotCommand;
import io.github.evercraftmc.evercraft.spigot.games.Game;
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
                            for (Game game : SpigotMain.getInstance().getRegisteredGames()) {
                                if (game.getName().equalsIgnoreCase(args[1])) {
                                    game.join(player);
                                }
                            }
                        } else {
                            // TODO Already in game message
                        }
                    } else {
                        sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("error.invalidArgs"))));
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
                        // TODO Not in game message
                    }
                }
            } else {
                sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("error.invalidArgs"))));
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
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("join")) {
                for (Game game : SpigotMain.getInstance().getRegisteredGames()) {
                    list.add(game.getName());
                }
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