package io.github.evercraftmc.evercraft.spigot.commands.warp;

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

public class WarpCommand extends SpigotCommand {
    public WarpCommand(String name, String description, List<String> aliases, String permission) {
        super(name, description, aliases, permission);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            if (args.length >= 1) {
                if (SpigotMain.getInstance().getWarps().getParsed().warps.containsKey(args[0]) && !(args[0].startsWith("~") && (player.isOp() || player.hasPermission("evercraft.commands.warp.secretWarp")))) {
                    player.teleport(SpigotMain.getInstance().getWarps().getParsed().warps.get(args[0]).toBukkitLocation());

                    if (SpigotMain.getInstance().getPluginConfig().getParsed().warp.clearOnWarp) {
                        player.getInventory().clear();
                        player.getActivePotionEffects().clear();
                    }

                    player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getParsed().warp.warped.replace("{warp}", args[0]))));
                } else {
                    player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getParsed().warp.notFound.replace("{warp}", args[0]))));
                }
            } else {
                sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getParsed().error.invalidArgs)));
            }
        } else {
            sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getParsed().error.noConsole)));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<String>();

        if (args.length == 1) {
            List<String> warps = new ArrayList<String>();

            for (String warp : SpigotMain.getInstance().getWarps().getParsed().warps.keySet()) {
                if (!(args[0].startsWith("~") && (sender.isOp() || sender.hasPermission("evercraft.commands.warp.secretWarp")))) {
                    warps.add(warp);
                }
            }

            list = new ArrayList<String>(warps);
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