package io.github.evercraftmc.evercraft.spigot.commands.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import io.github.evercraftmc.evercraft.shared.util.StringUtils;
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
            Pig pig;
            if (args.length > 0 && args[0].equalsIgnoreCase("precise")) {
                pig = (Pig) player.getWorld().spawnEntity(player.getLocation().add(0, -1, 0), EntityType.PIG);
            } else {
                pig = (Pig) player.getWorld().spawnEntity(player.getLocation().toBlockLocation().add(0.5, player.getWorld().getBlockAt(player.getLocation().toBlockLocation().add(0, -1, 0)).getType().getKey().getKey().endsWith("_stairs") || player.getWorld().getBlockAt(player.getLocation().toBlockLocation().add(0, -1, 0)).getType().getKey().getKey().endsWith("_slab") ? -1.5 : (player.getWorld().getBlockAt(player.getLocation().toBlockLocation()).getType().getKey().getKey().endsWith("_stairs") || player.getWorld().getBlockAt(player.getLocation().toBlockLocation()).getType().getKey().getKey().endsWith("_slab") ? -0.5 : -1), 0.5), EntityType.PIG);
            }

            Pig pig = (Pig) player.getWorld().spawnEntity(player.getLocation().add(0, -1, 0), EntityType.PIG);
            Pig pig = (Pig) player.getWorld().spawnEntity(player.getLocation().add(0, player.getWorld().getBlockAt(player.getLocation().add(0, -2, 0)).getType().toString().endsWith("_STAIRS") || player.getWorld().getBlockAt(player.getLocation().add(0, -2, 0)).getType().toString().endsWith("_SLAB") ? -2 : -1, 0), EntityType.PIG);
            pig.setInvisible(true);
            pig.setInvulnerable(true);
            pig.setAI(false);
            pig.setGravity(false);
            pig.setSilent(true);
            pig.addScoreboardTag("playerSeat:" + player.getUniqueId().toString() + ":" + player.getLocation().getX() + "," + player.getLocation().getY() + "," + player.getLocation().getZ());
            pig.addScoreboardTag("playerSeat:" + player.getUniqueId().toString());
            pig.addScoreboardTag("playerSeat:" + player.getUniqueId().toString() + ":" + player.getLocation().getX() + "," + player.getLocation().getY() + "," + player.getLocation().getZ());
            pig.addPassenger(player);
        } else {
            sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getParsed().error.noConsole)));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<String>();

        if (args.length == 1) {
            list.add("precise");
        } else {
            return Arrays.asList();
        }

        if (args.length > 0) {
            return StringUtils.matchPartial(args[args.length - 1], list);
        } else {
            return list;
        }
        return Arrays.asList();
    }
}