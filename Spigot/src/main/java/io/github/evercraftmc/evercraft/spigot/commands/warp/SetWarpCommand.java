package io.github.evercraftmc.evercraft.spigot.commands.warp;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import io.github.evercraftmc.evercraft.spigot.SpigotMain;
import io.github.evercraftmc.evercraft.spigot.commands.SpigotCommand;
import io.github.evercraftmc.evercraft.spigot.util.formatting.ComponentFormatter;
import io.github.evercraftmc.evercraft.spigot.util.types.SerializableLocation;

public class SetWarpCommand extends SpigotCommand {
    public SetWarpCommand(String name, String description, List<String> aliases, String permission) {
        super(name, description, aliases, permission);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            if (args.length >= 1) {
                if (SpigotMain.getInstance().getWarps().get().warps.containsKey(args[0])) {
                    SpigotMain.getInstance().getWarps().get().warps.remove(args[0]);
                }

                SpigotMain.getInstance().getWarps().get().warps.put(args[0], SerializableLocation.fromBukkitLocation(player.getLocation()));
                try {
                    SpigotMain.getInstance().getWarps().save();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().warp.setWarp.replace("{warp}", args[0]))));
            } else {
                sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().error.invalidArgs)));
            }
        } else {
            sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().error.noConsole)));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return Arrays.asList();
    }
}