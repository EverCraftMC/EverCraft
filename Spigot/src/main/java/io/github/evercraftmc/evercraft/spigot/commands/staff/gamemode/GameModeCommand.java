package io.github.evercraftmc.evercraft.spigot.commands.staff.gamemode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import io.github.evercraftmc.evercraft.shared.util.StringUtils;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import io.github.evercraftmc.evercraft.spigot.SpigotMain;
import io.github.evercraftmc.evercraft.spigot.commands.SpigotCommand;
import io.github.evercraftmc.evercraft.spigot.util.formatting.ComponentFormatter;

public class GameModeCommand extends SpigotCommand {
    public GameModeCommand(String name, String description, List<String> aliases, String permission) {
        super(name, description, aliases, permission);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("s") || args[0].equalsIgnoreCase("0")) {
                SpigotMain.getInstance().getServer().dispatchCommand(sender, "gms");
            } else if (args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("c") || args[0].equalsIgnoreCase("1")) {
                SpigotMain.getInstance().getServer().dispatchCommand(sender, "gmc");
            } else if (args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("a") || args[0].equalsIgnoreCase("2")) {
                SpigotMain.getInstance().getServer().dispatchCommand(sender, "gma");
            } else if (args[0].equalsIgnoreCase("spectator") || args[0].equalsIgnoreCase("sp") || args[0].equalsIgnoreCase("3")) {
                SpigotMain.getInstance().getServer().dispatchCommand(sender, "gmsp");
            } else {
                sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().error.invalidArgs)));
            }
        } else if (args.length == 2) {
            if (args[1].equalsIgnoreCase("survival") || args[1].equalsIgnoreCase("s") || args[1].equalsIgnoreCase("0")) {
                SpigotMain.getInstance().getServer().dispatchCommand(sender, "gms " + args[0]);
            } else if (args[1].equalsIgnoreCase("creative") || args[1].equalsIgnoreCase("c") || args[1].equalsIgnoreCase("1")) {
                SpigotMain.getInstance().getServer().dispatchCommand(sender, "gmc " + args[0]);
            } else if (args[1].equalsIgnoreCase("adventure") || args[1].equalsIgnoreCase("a") || args[1].equalsIgnoreCase("2")) {
                SpigotMain.getInstance().getServer().dispatchCommand(sender, "gma " + args[0]);
            } else if (args[1].equalsIgnoreCase("spectator") || args[1].equalsIgnoreCase("sp") || args[1].equalsIgnoreCase("3")) {
                SpigotMain.getInstance().getServer().dispatchCommand(sender, "gmsp " + args[0]);
            } else {
                sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().error.invalidArgs)));
            }
        } else {
            sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().error.invalidArgs)));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<String>();

        if (args.length == 1) {
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

            for (Player player : SpigotMain.getInstance().getServer().getOnlinePlayers()) {
                list.add(player.getName());
            }
        } else if (args.length == 2) {
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