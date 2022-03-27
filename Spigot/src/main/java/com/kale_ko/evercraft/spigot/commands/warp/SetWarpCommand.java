package com.kale_ko.evercraft.spigot.commands.warp;

import java.util.Arrays;
import java.util.List;
import com.kale_ko.evercraft.shared.util.formatting.TextFormatter;
import com.kale_ko.evercraft.spigot.SpigotMain;
import com.kale_ko.evercraft.spigot.commands.SpigotCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetWarpCommand extends SpigotCommand {
    public static final String name = "setwarp";
    public static final String description = "Set a warp to your location";

    public static final List<String> aliases = Arrays.asList();

    public static final String permission = "evercraft.commands.warp.setwarp";

    @Override
    public void run(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            if (args.length >= 1) {
                SpigotMain.getInstance().getWarps().set(args[0], player.getLocation());

                player.sendMessage(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("warp.setWarp").replace("{warp}", args[0])));
            } else {
                sender.sendMessage(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("error.invalidArgs")));
            }
        } else {
            sender.sendMessage(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("error.noConsole")));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return Arrays.asList();
    }
}