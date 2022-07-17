package io.github.evercraftmc.evercraft.spigot.commands.warp;

import java.util.Arrays;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import io.github.evercraftmc.evercraft.spigot.SpigotMain;
import io.github.evercraftmc.evercraft.spigot.commands.SpigotCommand;
import io.github.evercraftmc.evercraft.spigot.util.formatting.ComponentFormatter;

public class SpawnCommand extends SpigotCommand {
    public SpawnCommand(String name, String description, List<String> aliases, String permission) {
        super(name, description, aliases, permission);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            if (SpigotMain.getInstance().getWarps().getParsed().warps.containsKey("spawn")) {
                player.teleport(SpigotMain.getInstance().getWarps().getParsed().warps.get("spawn").toBukkitLocation());

                if (SpigotMain.getInstance().getPluginConfig().getParsed().warp.clearOnWarp) {
                    player.getInventory().clear();
                    player.getActivePotionEffects().clear();
                }

                if (!(args.length >= 1 && args[0].equalsIgnoreCase("true"))) {
                    player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getParsed().warp.warped.replace("{warp}", "spawn"))));
                }
            } else {
                player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getParsed().warp.notFound.replace("{warp}", "spawn"))));
            }
        } else {
            sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getParsed().error.noConsole)));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return Arrays.asList();
    }
}