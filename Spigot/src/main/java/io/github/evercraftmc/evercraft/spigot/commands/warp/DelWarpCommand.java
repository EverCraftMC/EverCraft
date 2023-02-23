package io.github.evercraftmc.evercraft.spigot.commands.warp;

import java.io.IOException;
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

public class DelWarpCommand extends SpigotCommand {
    public DelWarpCommand(String name, String description, List<String> aliases, String permission) {
        super(name, description, aliases, permission);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            if (args.length >= 1) {
                if (SpigotMain.getInstance().getWarps().get().warps.containsKey(args[0])) {
                    SpigotMain.getInstance().getWarps().get().warps.remove(args[0]);
                    try {
                        SpigotMain.getInstance().getWarps().save();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().warp.delWarp.replace("{warp}", args[0]))));
                } else {
                    sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().error.invalidArgs)));
                }
            } else {
                sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().error.invalidArgs)));
            }
        } else {
            sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().error.noConsole)));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<String>();

        if (args.length == 1) {
            list = new ArrayList<String>(SpigotMain.getInstance().getWarps().get().warps.keySet());
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