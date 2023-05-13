package io.github.evercraftmc.core.impl.bungee.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.github.evercraftmc.core.api.commands.ECCommand;
import io.github.evercraftmc.core.api.server.ECCommandManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.TabExecutor;

public class ECBungeeCommandManager implements ECCommandManager {
    protected class CommandInter extends Command implements TabExecutor {
        protected final ECBungeeCommandManager parent = ECBungeeCommandManager.this;

        protected ECCommand command;

        public CommandInter(ECCommand command, boolean distinguishServer) {
            super((distinguishServer ? "b" : "") + command.getName().toLowerCase(), command.getPermission(), CommandInter.alias(command.getAlias(), distinguishServer).toArray(new String[] {}));

            this.command = command;
        }

        public void execute(CommandSender sender, String[] args) {
            if (sender instanceof ProxiedPlayer bungeePlayer) {
                if (sender.hasPermission(this.getPermission())) {
                    this.command.run(parent.server.getOnlinePlayer(bungeePlayer.getUniqueId()), args);
                }
            } else {
                this.command.run(parent.server.getConsole(), args);
            }
        }

        public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
            if (sender instanceof ProxiedPlayer bungeePlayer) {
                if (sender.hasPermission(this.getPermission())) {
                    return this.command.tabComplete(parent.server.getOnlinePlayer(bungeePlayer.getUniqueId()), args);
                } else {
                    return Arrays.asList();
                }
            } else {
                return this.command.tabComplete(parent.server.getConsole(), args);
            }
        }

        private static List<String> alias(List<String> uAliases, boolean distinguishServer) {
            ArrayList<String> aliases = new ArrayList<String>();

            for (String alias : uAliases) {
                aliases.add((distinguishServer ? "b" : "") + alias.toLowerCase());
            }

            return aliases;
        }
    }

    protected ECBungeeServer server;

    protected Map<String, ECCommand> commands = new HashMap<String, ECCommand>();
    protected Map<String, CommandInter> interCommands = new HashMap<String, CommandInter>();

    public ECBungeeCommandManager(ECBungeeServer server) {
        this.server = server;
    }

    public ECBungeeServer getServer() {
        return this.server;
    }

    @Override
    public ECCommand get(String name) {
        return this.commands.get(name.toLowerCase());
    }

    @Override
    public ECCommand register(ECCommand command) {
        return this.register(command, true);
    }

    @Override
    public ECCommand register(ECCommand command, boolean distinguishServer) {
        String name = command.getName();
        if (distinguishServer) {
            name = "b" + name;
        }

        if (!this.commands.containsKey(name)) {
            CommandInter interCommand = new CommandInter(command, distinguishServer);

            this.commands.put(name, command);
            this.interCommands.put(name, interCommand);

            this.server.getHandle().getPluginManager().registerCommand((Plugin) this.server.getPlugin().getHandle(), (Command) interCommand);

            return command;
        } else {
            throw new RuntimeException("Command /" + command.getName() + " is already registered");
        }
    }

    @Override
    public ECCommand unregister(ECCommand command) {
        if (this.commands.containsKey(command.getName().toLowerCase())) {
            this.server.getHandle().getPluginManager().unregisterCommand((Command) this.interCommands.get(command.getName().toLowerCase()));

            this.commands.remove(command.getName().toLowerCase());
            this.interCommands.remove(command.getName().toLowerCase());

            return command;
        } else if (this.commands.containsKey("b" + command.getName().toLowerCase())) {
            this.server.getHandle().getPluginManager().unregisterCommand((Command) this.interCommands.get("b" + command.getName().toLowerCase()));

            this.commands.remove("b" + command.getName().toLowerCase());
            this.interCommands.remove("b" + command.getName().toLowerCase());

            return command;
        } else {
            throw new RuntimeException("Command /" + command.getName() + " is not registered");
        }
    }

    @Override
    public void unregisterAll() {
        for (ECCommand command : this.commands.values()) {
            this.unregister(command);
        }
    }
}