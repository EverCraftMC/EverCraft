package io.github.evercraftmc.evercraft.spigot.commands.staff;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.CommandSender;
import io.github.evercraftmc.evercraft.shared.util.StringUtils;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import io.github.evercraftmc.evercraft.spigot.SpigotMain;
import io.github.evercraftmc.evercraft.spigot.commands.SpigotCommand;
import io.github.evercraftmc.evercraft.spigot.util.formatting.ComponentFormatter;
import io.github.kale_ko.bjsl.BJSL;
import io.github.kale_ko.bjsl.elements.ParsedElement;
import io.github.kale_ko.ejcl.PathResolver;

public class DebugCommand extends SpigotCommand {
    public DebugCommand(String name, String description, List<String> aliases, String permission) {
        super(name, description, aliases, permission);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (args.length > 1) {
            ParsedElement json = null;
            if (args[0].equalsIgnoreCase("config")) {
                json = BJSL.parseJson(BJSL.stringifyJson(SpigotMain.getInstance().getPluginConfig().get()));
            } else if (args[0].equalsIgnoreCase("messages")) {
                json = BJSL.parseJson(BJSL.stringifyJson(SpigotMain.getInstance().getPluginMessages().get()));
            } else if (args[0].equalsIgnoreCase("data")) {
                json = BJSL.parseJson(BJSL.stringifyJson(SpigotMain.getInstance().getPluginData().get()));
            } else if (args[0].equalsIgnoreCase("warps")) {
                json = BJSL.parseJson(BJSL.stringifyJson(SpigotMain.getInstance().getWarps().get()));
            } else if (args[0].equalsIgnoreCase("kits")) {
                json = BJSL.parseJson(BJSL.stringifyJson(SpigotMain.getInstance().getKits().get()));
            } else if (args[0].equalsIgnoreCase("chests")) {
                json = BJSL.parseJson(BJSL.stringifyJson(SpigotMain.getInstance().getChests().get()));
            }

            if (json != null) {
                String path = "";
                for (Integer i = 1; i < args.length; i++) {
                    path += args[i] + " ";
                }
                path = path.substring(0, path.length() - 1);

                String string = BJSL.stringifyJson(PathResolver.resolve(json, path));

                sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.removeColors(args[1] + " in " + args[0] + " has the value of \n" + string + "")));
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
            list.add("config");
            list.add("messages");
            list.add("data");
            list.add("warps");
            list.add("kits");
            list.add("chests");
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