package io.github.evercraftmc.evercraft.spigot.commands.warp;

import java.util.Arrays;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import io.github.evercraftmc.evercraft.spigot.SpigotMain;
import io.github.evercraftmc.evercraft.spigot.commands.SpigotCommand;
import io.github.evercraftmc.evercraft.spigot.util.formatting.ComponentFormatter;

public class HomeCommand extends SpigotCommand {
    public HomeCommand(String name, String description, List<String> aliases, String permission) {
        super(name, description, aliases, permission);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            if (player.getBedSpawnLocation() != null) {
                player.teleport(player.getBedSpawnLocation());

                player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().warp.warped.replace("{warp}", "home"))));
            } else {
                player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().warp.notFound.replace("{warp}", "home"))));
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