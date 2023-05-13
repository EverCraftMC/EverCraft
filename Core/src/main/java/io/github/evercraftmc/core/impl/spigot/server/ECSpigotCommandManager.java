package io.github.evercraftmc.core.impl.spigot.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import io.github.evercraftmc.core.api.commands.ECCommand;
import io.github.evercraftmc.core.api.server.ECCommandManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ECSpigotCommandManager implements ECCommandManager {
    protected class CommandInter extends Command {
        protected final ECSpigotCommandManager parent = ECSpigotCommandManager.this;

        protected ECCommand command;

        public CommandInter(ECCommand command) {
            super(command.getName());

            this.setName(command.getName().toLowerCase());
            this.setDescription(command.getDescription());
            this.setAliases(CommandInter.lower(command.getAlias()));
            this.setPermission(command.getPermission().toLowerCase());

            this.command = command;
        }

        public boolean execute(CommandSender sender, String commandLabel, String[] args) {
            if (sender instanceof Player spigotPlayer) {
                if (sender.hasPermission(this.getPermission())) {
                    this.command.run(parent.server.getOnlinePlayer(spigotPlayer.getUniqueId()), args);
                }
            } else {
                this.command.run(parent.server.getConsole(), args);
            }

            return true;
        }

        public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
            if (sender instanceof ProxiedPlayer spigotPlayer) {
                if (sender.hasPermission(this.getPermission())) {
                    return this.command.tabComplete(parent.server.getOnlinePlayer(spigotPlayer.getUniqueId()), args);
                } else {
                    return Arrays.asList(new String[0]);
                }
            } else {
                return this.command.tabComplete(parent.server.getConsole(), args);
            }
        }

        private static List<String> lower(List<String> uAliases) {
            ArrayList<String> aliases = new ArrayList<String>();

            for (String alias : uAliases) {
                aliases.add(alias.toLowerCase());
            }

            return aliases;
        }
    }

    protected ECSpigotServer server;

    protected Map<String, ECCommand> commands = new HashMap<String, ECCommand>();
    protected Map<String, CommandInter> interCommands = new HashMap<String, CommandInter>();

    public ECSpigotCommandManager(ECSpigotServer server) {
        this.server = server;
    }

    public ECSpigotServer getServer() {
        return this.server;
    }

    @Override
    public ECCommand get(String name) {
        return this.commands.get(name.toLowerCase());
    }

    @Override
    public ECCommand register(ECCommand command) {
        if (!this.commands.containsKey(command.getName().toLowerCase())) {
            CommandInter interCommand = new CommandInter(command);

            this.commands.put(command.getName().toLowerCase(), command);
            this.interCommands.put(command.getName().toLowerCase(), interCommand);

            this.server.getHandle().getCommandMap().register("evercraft", interCommand);
            this.interCommands.get(command.getName().toLowerCase()).register(this.server.getHandle().getCommandMap());

            return command;
        } else {
            throw new RuntimeException("Command /" + command.getName() + " is already registered");
        }
    }

    @Override
    public ECCommand unregister(ECCommand command) {
        if (this.commands.containsKey(command.getName().toLowerCase())) {
            this.interCommands.get(command.getName().toLowerCase()).unregister(this.server.getHandle().getCommandMap());

            this.commands.remove(command.getName().toLowerCase());
            this.interCommands.remove(command.getName().toLowerCase());

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