package com.kale_ko.evercraft.spigot.commands;

import java.util.List;
import com.kale_ko.evercraft.spigot.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class SpigotCommand extends Command {
    public SpigotCommand(String name, String description, List<String> aliases, String usage, String permission) {
        super(name);
        this.setName(name);
        this.setLabel(name);
        this.setDescription(description);
        this.setAliases(aliases);
        this.setUsage(usage);
        this.setPermission(permission);
        this.setPermissionMessage(Util.getNoPermissionMessage(permission));
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (Util.hasPermission(sender, getPermission())) {
            run(sender, commandLabel, args);
        } else {
            Util.sendMessage(sender, "&cYou do not have permission to perform that command.");
        }

        return true;
    }

    public void run(CommandSender sender, String commandLabel, String[] args) { }
}