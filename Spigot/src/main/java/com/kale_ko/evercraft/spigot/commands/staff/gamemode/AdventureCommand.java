package com.kale_ko.evercraft.spigot.commands.staff.gamemode;

import java.util.Arrays;
import java.util.List;
import com.kale_ko.evercraft.shared.util.formatting.TextFormatter;
import com.kale_ko.evercraft.spigot.SpigotMain;
import com.kale_ko.evercraft.spigot.commands.SpigotCommand;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdventureCommand extends SpigotCommand {
    public AdventureCommand(String name, String description, List<String> aliases, String permission) {
        super(name, description, aliases, permission);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            player.setGameMode(GameMode.ADVENTURE);

            player.sendMessage(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("staff.gamemode").replace("{gamemode}", "adventure")));
        } else {
            sender.sendMessage(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("error.noConsole")));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return Arrays.asList();
    }
}