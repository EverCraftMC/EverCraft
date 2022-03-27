package com.kale_ko.evercraft.spigot.commands.staff.gamemode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.kale_ko.evercraft.shared.util.StringUtils;
import com.kale_ko.evercraft.shared.util.formatting.TextFormatter;
import com.kale_ko.evercraft.spigot.SpigotMain;
import com.kale_ko.evercraft.spigot.commands.SpigotCommand;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GameModeCommand extends SpigotCommand {
    public static final String name = "gamemode";
    public static final String description = "Change your gamemode";

    public static final List<String> aliases = Arrays.asList();

    public static final String permission = "evercraft.commands.staff.gamemode.use";

    @Override
    public void run(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("s") || args[0].equalsIgnoreCase("0")) {
                    player.setGameMode(GameMode.SURVIVAL);

                    player.sendMessage(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("staff.gamemode").replace("{gamemode}", "survival")));
                } else if (args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("c") || args[0].equalsIgnoreCase("1")) {
                    player.setGameMode(GameMode.CREATIVE);

                    player.sendMessage(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("staff.gamemode").replace("{gamemode}", "creative")));
                } else if (args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("a") || args[0].equalsIgnoreCase("2")) {
                    player.setGameMode(GameMode.ADVENTURE);

                    player.sendMessage(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("staff.gamemode").replace("{gamemode}", "adventure")));
                } else if (args[0].equalsIgnoreCase("spectator") || args[0].equalsIgnoreCase("sp") || args[0].equalsIgnoreCase("3")) {
                    player.setGameMode(GameMode.SPECTATOR);

                    player.sendMessage(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("staff.gamemode").replace("{gamemode}", "spectator")));
                }
            } else {
                sender.sendMessage(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("error.invalidArgs")));
            }
        } else {
            sender.sendMessage(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("error.noConsole")));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<String>();

        if (args.length == 0) {
            list.add("survival");
            list.add("s");
            list.add("0");
            list.add("creative");
            list.add("c");
            list.add("1");
            list.add("adventure");
            list.add("a");
            list.add("2");
            list.add("spectator");
            list.add("sp");
            list.add("3");
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