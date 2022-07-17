package io.github.evercraftmc.evercraft.bungee.commands.staff;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import io.github.evercraftmc.evercraft.bungee.BungeeMain;
import io.github.evercraftmc.evercraft.bungee.commands.BungeeCommand;
import io.github.evercraftmc.evercraft.bungee.util.formatting.ComponentFormatter;
import io.github.evercraftmc.evercraft.bungee.util.player.BungeePlayerResolver;
import io.github.evercraftmc.evercraft.shared.util.StringUtils;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import io.github.evercraftmc.evercraft.shared.util.player.SimplePlayer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ImpersonateCommand extends BungeeCommand {
    public ImpersonateCommand(String name, String description, List<String> aliases, String permission) {
        super(name, description, aliases, permission);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (args.length > 0) {
            SimplePlayer player = BungeePlayerResolver.getPlayer(BungeeMain.getInstance().getPluginData(), args[0]);

            if (player != null) {
                Boolean shouldFakeJoinLeave = true;

                StringBuilder message = new StringBuilder();

                for (Integer i = 1; i < args.length; i++) {
                    if (args[i].equalsIgnoreCase("-noJoin")) {
                        shouldFakeJoinLeave = false;
                    } else {
                        message.append(args[i] + " ");
                    }
                }

                final Boolean fakeJoinLeave = shouldFakeJoinLeave;

                if (fakeJoinLeave) {
                    for (ProxiedPlayer player2 : BungeeMain.getInstance().getProxy().getPlayers()) {
                        player2.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().welcome.join.replace("{player}", player.getDisplayName()))));
                    }
                }

                BungeeMain.getInstance().getProxy().getScheduler().schedule(BungeeMain.getInstance(), new Runnable() {
                    public void run() {
                        for (ProxiedPlayer player2 : BungeeMain.getInstance().getProxy().getPlayers()) {
                            player2.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().chat.main.replace("{player}", player.getDisplayName()).replace("{message}", message))));
                        }

                        BungeeMain.getInstance().getProxy().getScheduler().schedule(BungeeMain.getInstance(), new Runnable() {
                            public void run() {
                                if (fakeJoinLeave) {
                                    for (ProxiedPlayer player2 : BungeeMain.getInstance().getProxy().getPlayers()) {
                                        player2.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().welcome.quit.replace("{player}", player.getDisplayName()))));
                                    }
                                }
                            }
                        }, 200, TimeUnit.MILLISECONDS);
                    }
                }, 200, TimeUnit.MILLISECONDS);
            } else {
                sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().error.playerNotFound.replace("{player}", args[0]))));
            }
        } else {
            sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().error.invalidArgs)));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<String>();

        if (args.length > 1) {
            list.add("-noJoin");
        }

        if (args.length > 0) {
            return StringUtils.matchPartial(args[args.length - 1], list);
        } else {
            return list;
        }
    }

}