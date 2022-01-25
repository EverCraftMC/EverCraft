package com.kale_ko.kalesutilities.spigot.commands;

import com.kale_ko.kalesutilities.spigot.Util;
import java.util.List;
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
        return false;
    }
}