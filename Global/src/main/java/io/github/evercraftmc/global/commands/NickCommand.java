package io.github.evercraftmc.global.commands;

import java.util.Arrays;
import java.util.List;
import io.github.evercraftmc.core.api.commands.ECCommand;
import io.github.evercraftmc.core.api.server.player.ECConsole;
import io.github.evercraftmc.core.api.server.player.ECPlayer;
import io.github.evercraftmc.core.impl.util.ECTextFormatter;
import io.github.evercraftmc.global.GlobalModule;

public class NickCommand implements ECCommand {
    protected final GlobalModule parent;

    public NickCommand(GlobalModule parent) {
        this.parent = parent;
    }

    @Override
    public String getName() {
        return "nickname";
    }

    @Override
    public String getDescription() {
        return "Change your nickname";
    }

    @Override
    public List<String> getAlias() {
        return Arrays.asList("nick", "setNickname", "setNick");
    }

    @Override
    public String getPermission() {
        return "evercraft.global.commands.nickname";
    }

    @Override
    public void run(ECPlayer player, String[] args) {
        if (!(player instanceof ECConsole)) {
            if (args.length > 0) {
                if (args.length == 1) {
                    if (ECTextFormatter.stripColors(args[0]).length() <= 16 && args[0].length() <= 32) {
                        if (args[0].equalsIgnoreCase("reset")) {
                            this.parent.getPlugin().getData().players.get(player.getUuid().toString()).displayName = player.getName();

                            player.sendMessage(ECTextFormatter.translateColors("&aYour nickname has been reset."));
                        } else {
                            this.parent.getPlugin().getData().players.get(player.getUuid().toString()).displayName = ECTextFormatter.translateColors(args[0]);

                            player.sendMessage(ECTextFormatter.translateColors("&aSuccessfully set your nickname to &r" + args[0] + "&r&a."));
                        }
                    } else {
                        player.sendMessage(ECTextFormatter.translateColors("&cThat nickname is too long."));
                    }
                } else {
                    player.sendMessage(ECTextFormatter.translateColors("&cYour nickname cant contain spaces."));
                }
            } else {
                this.parent.getPlugin().getData().players.get(player.getUuid().toString()).displayName = player.getName();

                player.sendMessage(ECTextFormatter.translateColors("&aYour nickname has been reset."));
            }

            player.setDisplayName(ECTextFormatter.translateColors((parent.getPlugin().getData().players.get(player.getUuid().toString()).prefix != null ? parent.getPlugin().getData().players.get(player.getUuid().toString()).prefix + " " : "") + parent.getPlugin().getData().players.get(player.getUuid().toString()).displayName));
        } else {
            player.sendMessage(ECTextFormatter.translateColors("&cYou cant do that from the console."));
        }
    }

    @Override
    public List<String> tabComplete(ECPlayer player, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("reset");
        } else {
            return Arrays.asList();
        }
    }
}