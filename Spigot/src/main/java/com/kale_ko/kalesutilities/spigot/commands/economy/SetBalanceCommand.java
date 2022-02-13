package com.kale_ko.kalesutilities.spigot.commands.economy;

import java.util.List;
import com.kale_ko.kalesutilities.spigot.SpigotPlugin;
import com.kale_ko.kalesutilities.spigot.commands.SpigotCommand;
import org.bukkit.command.CommandSender;

public class SetBalanceCommand extends SpigotCommand {
    public SetBalanceCommand(String name, String description, List<String> aliases, String usage, String permission) {
        super(name, description, aliases, usage, permission);
    }

    @Override
    public void run(CommandSender sender, String label, String[] args) {
        SpigotPlugin.Instance.players.set(args[0] + ".balance", Float.parseFloat(args[0]));
    }
}