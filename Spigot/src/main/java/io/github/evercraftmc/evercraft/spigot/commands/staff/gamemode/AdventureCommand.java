package io.github.evercraftmc.evercraft.spigot.commands.staff.gamemode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import io.github.evercraftmc.evercraft.shared.util.StringUtils;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import io.github.evercraftmc.evercraft.spigot.SpigotMain;
import io.github.evercraftmc.evercraft.spigot.commands.SpigotCommand;
import io.github.evercraftmc.evercraft.spigot.util.formatting.ComponentFormatter;

public class AdventureCommand extends SpigotCommand {
    public AdventureCommand(String name, String description, List<String> aliases, String permission) {
        super(name, description, aliases, permission);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player player) {
                player.setGameMode(GameMode.ADVENTURE);

                player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().gamemode.replace("{gamemode}", "adventure"))));
            } else {
                sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().error.noConsole)));
            }
        } else if (args.length == 1) {
            Player player = SpigotMain.getInstance().getServer().getPlayer(args[0]);

            if (player != null) {
                player.setGameMode(GameMode.ADVENTURE);

                sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().gamemodeOther.replace("{player}", ComponentFormatter.componentToString(player.displayName())).replace("{gamemode}", "adventure"))));
            } else {
                sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().error.playerNotFound.replace("{player}", args[0]))));
            }
        } else {
            sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().get().error.invalidArgs)));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<String>();

        if (args.length == 1) {
            for (Player player : SpigotMain.getInstance().getServer().getOnlinePlayers()) {
                list.add(player.getName());
            }
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