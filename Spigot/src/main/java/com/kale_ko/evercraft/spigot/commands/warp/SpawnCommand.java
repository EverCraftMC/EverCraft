package com.kale_ko.evercraft.spigot.commands.warp;

import java.util.Arrays;
import java.util.List;
import com.kale_ko.evercraft.shared.util.formatting.TextFormatter;
import com.kale_ko.evercraft.spigot.SpigotMain;
import com.kale_ko.evercraft.spigot.commands.SpigotCommand;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand extends SpigotCommand {
    public static final String name = "spawn";
    public static final String description = "Go to the spawn";

    public static final List<String> aliases = Arrays.asList();

    public static final String permission = "evercraft.commands.warp.spawn";

    @Override
    public void run(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            if (SpigotMain.getInstance().getWarps().getSerializable("spawn", Location.class) != null) {
                player.teleport(SpigotMain.getInstance().getWarps().getSerializable("spawn", Location.class));

                player.sendMessage(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("warp.warped").replace("{warp}", "spawn")));
            } else {
                player.sendMessage(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("warp.notFound").replace("{warp}", "spawn")));
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