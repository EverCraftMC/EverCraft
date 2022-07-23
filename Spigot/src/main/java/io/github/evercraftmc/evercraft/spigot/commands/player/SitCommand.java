package io.github.evercraftmc.evercraft.spigot.commands.player;

import java.util.Arrays;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import io.github.evercraftmc.evercraft.spigot.SpigotMain;
import io.github.evercraftmc.evercraft.spigot.commands.SpigotCommand;
import io.github.evercraftmc.evercraft.spigot.util.formatting.ComponentFormatter;

public class SitCommand extends SpigotCommand {
    public SitCommand(String name, String description, List<String> aliases, String permission) {
        super(name, description, aliases, permission);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            Pig pig = (Pig) player.getWorld().spawnEntity(player.getLocation().add(0, player.getWorld().getBlockAt(player.getLocation().add(0, -2, 0)).getType().toString().endsWith("_STAIRS") || player.getWorld().getBlockAt(player.getLocation().add(0, -2, 0)).getType().toString().endsWith("_SLAB") ? -2 : -1, 0), EntityType.PIG);
            pig.setInvisible(true);
            pig.setInvulnerable(true);
            pig.setAI(false);
            pig.setGravity(false);
            pig.addScoreboardTag("playerSeat:" + player.getUniqueId().toString() + ":" + player.getLocation().getX() + "," + player.getLocation().getY() + "," + player.getLocation().getZ());
            pig.addPassenger(player);
        } else {
            sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getParsed().error.noConsole)));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return Arrays.asList();
    }
}