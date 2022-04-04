package io.github.evercraftmc.evercraft.spigot.commands.warp;

import java.util.Arrays;
import java.util.List;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import io.github.evercraftmc.evercraft.spigot.SpigotMain;
import io.github.evercraftmc.evercraft.spigot.commands.SpigotCommand;
import io.github.evercraftmc.evercraft.spigot.util.formatting.ComponentFormatter;
import io.github.evercraftmc.evercraft.spigot.util.types.SerializableLocation;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand extends SpigotCommand {
    public SpawnCommand(String name, String description, List<String> aliases, String permission) {
        super(name, description, aliases, permission);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            if (SpigotMain.getInstance().getWarps().getSerializable("spawn", SerializableLocation.class) != null) {
                player.teleport(SpigotMain.getInstance().getWarps().getSerializable("spawn", SerializableLocation.class).toBukkitLocation());

                if (SpigotMain.getInstance().getPluginConfig().getBoolean("warp.clearonwarp")) {
                    player.getInventory().clear();
                    player.getActivePotionEffects().clear();
                }

                player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("warp.warped").replace("{warp}", "spawn"))));
            } else {
                player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("warp.notFound").replace("{warp}", "spawn"))));
            }
        } else {
            sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("error.noConsole"))));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return Arrays.asList();
    }
}