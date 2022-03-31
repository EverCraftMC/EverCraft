package com.kale_ko.evercraft.spigot.commands;

import java.util.List;
import com.kale_ko.evercraft.shared.PluginCommand;
import com.kale_ko.evercraft.shared.util.formatting.TextFormatter;
import com.kale_ko.evercraft.spigot.SpigotMain;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;

public abstract class SpigotCommand extends Command implements PluginCommand {
    protected SpigotCommand(String name, String description, List<String> aliases, String permission) {
        super(name);
        this.setLabel(name);
        this.setName(name);
        this.setDescription(description);
        this.setAliases(aliases);
        this.setPermission(permission);
        if (permission != null) {
            this.setPermissionMessage(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("error.noPerms").replace("{permission}", permission)));
        }

        this.register();
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (this.testPermission(sender)) {
            this.run(sender, args);
        }

        return true;
    }

    @Override
    public boolean testPermission(CommandSender sender) {
        if (this.testPermissionSilent(sender)) {
            return true;
        } else {
            sender.sendMessage(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getString("error.noPerms").replace("{permission}", this.getPermission())));

            return false;
        }
    }

    @Override
    public boolean testPermissionSilent(CommandSender sender) {
        return this.getPermission() == null || sender.hasPermission(this.getPermission()) || sender.isOp();
    }

    public void run(CommandSender sender, String[] args) { }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        return tabComplete(sender, args);
    }

    public List<String> tabComplete(CommandSender sender, String[] args) {
        return null;
    }

    public SpigotCommand register() {
        ((CraftServer) SpigotMain.getInstance().getServer()).getCommandMap().register(SpigotMain.getInstance().getName(), this);

        return this;
    }

    public void unregister() {
        SpigotMain.getInstance().getCommand(this.getName()).unregister(((CraftServer) SpigotMain.getInstance().getServer()).getCommandMap());
    }
}