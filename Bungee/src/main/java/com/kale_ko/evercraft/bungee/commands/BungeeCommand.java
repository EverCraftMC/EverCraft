package com.kale_ko.evercraft.bungee.commands;

import java.util.List;
import com.kale_ko.evercraft.bungee.Util;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class BungeeCommand extends Command {
    public BungeeCommand(String name, List<String> aliases, String permission) {
        super(name, permission, aliases.toArray(new String[] {}));
        this.setPermissionMessage(Util.getNoPermissionMessage(permission));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

    }
}