package com.kale_ko.evercraft.spigot.commands;

import java.util.ArrayList;
import java.util.List;
import com.kale_ko.evercraft.shared.PluginCommand;
import com.kale_ko.evercraft.shared.util.formatting.TextFormatter;
import com.kale_ko.evercraft.spigot.SpigotMain;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;

public abstract class SpigotCommand extends Command implements PluginCommand {
    public static final String name = null;
    public static final String description = "";

    public static final List<String> aliases = new ArrayList<String>();

    public static final String permission = null;

    protected SpigotCommand() {
        super(name);
        this.setLabel(name);
        this.setName(name);
        this.setDescription(description);
        this.setAliases(aliases);
        this.setPermission(permission);
        this.setPermissionMessage(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("error.noPerms").replace("{permission}", permission)));

        this.register();
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender.hasPermission(permission)) {
            this.run(sender, args);
        } else {
            sender.sendMessage(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("error.noPerms").replace("{permission}", permission)));
        }

        return true;
    }

    public void run(CommandSender sender, String[] args) { }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        return null;
    }

    public SpigotCommand register() {
        ((CraftServer) SpigotMain.getInstance().getServer()).getCommandMap().register(SpigotMain.getInstance().getName(), this);

        return this;
    }

    public void unregister() {
        SpigotMain.getInstance().getCommand(name).unregister(((CraftServer) SpigotMain.getInstance().getServer()).getCommandMap());
    }
}