package io.github.evercraftmc.evercraft.limbo.commands;

import java.util.Arrays;
import java.util.List;
import io.github.evercraftmc.evercraft.shared.PluginCommand;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import io.github.evercraftmc.evercraft.limbo.LimboMain;
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

    @Override
    public void execute(CommandSender sender, String[] args) {
        Boolean isCommand = args[0].equalsIgnoreCase(name);

        for (String alias : aliases) {
            if (args[0].equalsIgnoreCase(alias)) {
                isCommand = true;
            }
        }

        if (isCommand && this.testPermission(sender)) {
            List<String> args0 = Arrays.asList(args);
            args0.remove(0);
            this.run(sender, args0.toArray(new String[] {}));
        }
    }

    public boolean testPermission(CommandSender sender) {
        if (this.testPermissionSilent(sender)) {
            return true;
        } else {
            sender.sendMessage(TextFormatter.translateColors(LimboMain.getInstance().getPluginMessages().getString("error.noPerms").replace("{permission}", this.permission)));

            return false;
        }
    }

    public boolean testPermissionSilent(CommandSender sender) {
        return this.permission == null || sender.hasPermission(this.permission);
    }

    public abstract void run(CommandSender sender, String[] args);

    public LimboCommand register() {
        LimboMain.getInstance().getServer().getPluginManager().registerCommands(LimboMain.getInstance(), this);

        return this;
    }

    public void unregister() {}
}