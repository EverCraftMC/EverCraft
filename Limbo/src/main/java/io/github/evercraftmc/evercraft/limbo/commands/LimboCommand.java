package io.github.evercraftmc.evercraft.limbo.commands;

import java.util.Arrays;
import java.util.List;
import io.github.evercraftmc.evercraft.shared.PluginCommand;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import io.github.evercraftmc.evercraft.limbo.LimboMain;
import io.github.evercraftmc.evercraft.limbo.util.formatting.ComponentFormatter;
import com.loohp.limbo.commands.CommandExecutor;
import com.loohp.limbo.commands.CommandSender;
import com.loohp.limbo.commands.TabCompletor;

public abstract class LimboCommand implements CommandExecutor, TabCompletor, PluginCommand {
    private String name;
    private String description;
    private List<String> aliases;
    private String permission;

    protected LimboCommand(String name, String description, List<String> aliases, String permission) {
        this.name = name;
        this.description = description;
        this.aliases = aliases;
        this.permission = permission;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public List<String> getAliases() {
        return this.aliases;
    }

    public String getPermission() {
        return this.permission;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Boolean isCommand = args[0].equalsIgnoreCase(this.getName());

        for (String alias : this.getAliases()) {
            if (args[0].equalsIgnoreCase(alias)) {
                isCommand = true;
            }
        }

        if (isCommand && this.testPermission(sender)) {
            this.run(sender, Arrays.copyOfRange(args, 1, args.length));
        }
    }

    public boolean testPermission(CommandSender sender) {
        if (this.testPermissionSilent(sender)) {
            return true;
        } else {
            sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(LimboMain.getInstance().getPluginMessages().getString("error.noPerms").replace("{permission}", this.getPermission()))));

            return false;
        }
    }

    public boolean testPermissionSilent(CommandSender sender) {
        return this.getPermission() == null || sender.hasPermission(this.getPermission());
    }

    public abstract void run(CommandSender sender, String[] args);

    public LimboCommand register() {
        LimboMain.getInstance().getServer().getPluginManager().registerCommands(LimboMain.getInstance(), this);

        return this;
    }

    public void unregister() {}
}