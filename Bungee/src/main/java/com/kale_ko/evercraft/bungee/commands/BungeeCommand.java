package com.kale_ko.evercraft.bungee.commands;

import java.util.Arrays;
import java.util.List;
import com.kale_ko.evercraft.bungee.BungeeMain;
import com.kale_ko.evercraft.shared.PluginCommand;
import com.kale_ko.evercraft.shared.util.formatting.TextFormatter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public abstract class BungeeCommand extends Command implements PluginCommand, TabExecutor {
    protected BungeeCommand(String name, String description, List<String> aliases, String permission) {
        super(name, permission, aliases.toArray(new String[] {}));
        if (permission != null) {
            this.setPermissionMessage(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("error.noPerms").replace("{permission}", permission)));
        }
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (this.hasPermission(sender)) {
            this.run(sender, args);
        } else {
            sender.sendMessage(TextComponent.fromLegacyText(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("error.noPerms").replace("{permission}", this.getPermission()))));
        }
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return this.getPermission() == null || sender.hasPermission(this.getPermission());
    }

    public void run(CommandSender sender, String[] args) { }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        return tabComplete(sender, args);
    }

    public List<String> tabComplete(CommandSender sender, String[] args) {
        return Arrays.asList();
    }

    public BungeeCommand register() {
        BungeeMain.getInstance().getProxy().getPluginManager().registerCommand(BungeeMain.getInstance(), this);

        return this;
    }

    public void unregister() {
        BungeeMain.getInstance().getProxy().getPluginManager().unregisterCommand(this);
    }
}