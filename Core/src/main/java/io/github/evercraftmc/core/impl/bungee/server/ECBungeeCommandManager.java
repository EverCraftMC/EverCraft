package io.github.evercraftmc.core.impl.bungee.server;

import io.github.evercraftmc.core.api.commands.ECCommand;
import io.github.evercraftmc.core.api.events.ECHandler;
import io.github.evercraftmc.core.api.events.ECListener;
import io.github.evercraftmc.core.api.events.messaging.MessageEvent;
import io.github.evercraftmc.core.api.server.ECCommandManager;
import io.github.evercraftmc.core.api.server.player.ECPlayer;
import io.github.evercraftmc.core.impl.ECEnvironmentType;
import io.github.evercraftmc.core.impl.bungee.server.util.ECBungeeComponentFormatter;
import io.github.evercraftmc.core.impl.util.ECTextFormatter;
import io.github.evercraftmc.core.messaging.ECMessage;
import io.github.evercraftmc.core.messaging.ECMessageType;
import io.github.evercraftmc.core.messaging.ECRecipient;
import java.io.*;
import java.util.*;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.TabExecutor;

public class ECBungeeCommandManager implements ECCommandManager {
    protected class CommandInter extends Command implements TabExecutor {
        protected final ECBungeeCommandManager parent = ECBungeeCommandManager.this;

        protected ECCommand command;
        protected boolean forwardToOther;

        public CommandInter(ECCommand command, boolean distinguishServer, boolean forwardToOther) {
            super((distinguishServer ? "b" : "") + command.getName().toLowerCase(), command.getPermission(), CommandInter.alias(command.getName(), command.getAlias(), distinguishServer).toArray(new String[] { }));

            this.command = command;
            this.forwardToOther = forwardToOther;
        }

        @Override
        public void execute(CommandSender sender, String[] args) {
            if (sender instanceof ProxiedPlayer bungeePlayer) {
                if (sender.hasPermission(this.getPermission())) {
                    this.command.run(parent.server.getOnlinePlayer(bungeePlayer.getUniqueId()), args, true);

                    if (this.forwardToOther) {
                        try {
                            ByteArrayOutputStream commandMessageData = new ByteArrayOutputStream();
                            DataOutputStream commandMessage = new DataOutputStream(commandMessageData);
                            commandMessage.writeInt(ECMessageType.GLOBAL_COMMAND);
                            commandMessage.writeUTF(bungeePlayer.getUniqueId().toString());
                            commandMessage.writeUTF(this.getName());
                            commandMessage.writeInt(args.length);
                            for (String arg : args) {
                                commandMessage.writeUTF(arg);
                            }
                            commandMessage.close();

                            parent.server.getPlugin().getMessager().send(ECRecipient.fromEnvironmentType(ECEnvironmentType.BACKEND), commandMessageData.toByteArray());
                        } catch (IOException e) {
                            parent.server.getPlugin().getLogger().error("[Messager] Failed to send message", e);
                        }
                    }
                } else {
                    sender.sendMessage(ECBungeeComponentFormatter.stringToComponent(ECTextFormatter.translateColors("&cYou do not have permission to run that command")));
                }
            } else {
                this.command.run(parent.server.getConsole(), args, true);
            }
        }

        @Override
        public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
            if (sender instanceof ProxiedPlayer bungeePlayer) {
                if (sender.hasPermission(this.getPermission())) {
                    return this.command.tabComplete(parent.server.getOnlinePlayer(bungeePlayer.getUniqueId()), args);
                } else {
                    return List.of();
                }
            } else {
                return this.command.tabComplete(parent.server.getConsole(), args);
            }
        }

        private static List<String> alias(String uName, List<String> uAliases, boolean distinguishServer) {
            ArrayList<String> aliases = new ArrayList<>();

            aliases.add("evercraft:" + (distinguishServer ? "b" : "") + uName.toLowerCase());

            for (String alias : uAliases) {
                aliases.add((distinguishServer ? "b" : "") + alias.toLowerCase());
                aliases.add("evercraft:" + (distinguishServer ? "b" : "") + alias.toLowerCase());
            }

            return aliases;
        }
    }

    protected ECBungeeServer server;

    protected Map<String, ECCommand> commands = new HashMap<>();
    protected Map<String, CommandInter> interCommands = new HashMap<>();

    public ECBungeeCommandManager(ECBungeeServer server) {
        this.server = server;

        this.server.getEventManager().register(new ECListener() {
            private final ECBungeeCommandManager parent = ECBungeeCommandManager.this;

            @ECHandler
            public void onMessage(MessageEvent event) {
                ECMessage message = event.getMessage();

                if (!message.getSender().matches(parent.server) && message.getRecipient().matches(parent.server)) {
                    try {
                        ByteArrayInputStream commandMessageData = new ByteArrayInputStream(message.getData());
                        DataInputStream commandMessage = new DataInputStream(commandMessageData);

                        int type = commandMessage.readInt();
                        if (type == ECMessageType.GLOBAL_COMMAND) {
                            UUID uuid = UUID.fromString(commandMessage.readUTF());
                            String command = commandMessage.readUTF();
                            List<String> args = new ArrayList<>();
                            int argC = commandMessage.readInt();
                            for (int i = 0; i < argC; i++) {
                                args.add(commandMessage.readUTF());
                            }

                            ECPlayer player = parent.server.getOnlinePlayer(uuid);

                            parent.server.getCommandManager().get(command).run(player, args.toArray(new String[] { }), false);
                        }

                        commandMessage.close();
                    } catch (IOException e) {
                        parent.server.getPlugin().getLogger().error("[Messager] Failed to read message", e);
                    }
                }
            }
        });
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
        return this.register(command, false);
    }

    @Override
    public ECCommand register(ECCommand command, boolean distinguishServer) {
        return this.register(command, distinguishServer, !distinguishServer);
    }

    @Override
    public ECCommand register(ECCommand command, boolean distinguishServer, boolean forwardToOther) {
        if (!this.commands.containsKey(command.getName().toLowerCase())) {
            CommandInter interCommand = new CommandInter(command, distinguishServer, forwardToOther);

            this.commands.put(command.getName().toLowerCase(), command);
            this.interCommands.put(command.getName().toLowerCase(), interCommand);

            this.server.getHandle().getPluginManager().registerCommand((Plugin) this.server.getPlugin().getHandle(), interCommand);

            return command;
        } else {
            throw new RuntimeException("Command /" + command.getName() + " is already registered");
        }
    }

    @Override
    public ECCommand unregister(ECCommand command) {
        if (this.commands.containsKey(command.getName().toLowerCase())) {
            this.server.getHandle().getPluginManager().unregisterCommand(this.interCommands.get(command.getName().toLowerCase()));

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