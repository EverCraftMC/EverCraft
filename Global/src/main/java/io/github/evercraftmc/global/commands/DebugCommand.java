package io.github.evercraftmc.global.commands;

import io.github.evercraftmc.core.ECPlayerData;
import io.github.evercraftmc.core.api.commands.ECCommand;
import io.github.evercraftmc.core.api.server.player.ECPlayer;
import io.github.evercraftmc.core.impl.util.ECTextFormatter;
import io.github.evercraftmc.global.GlobalModule;
import io.github.kale_ko.bjsl.BJSL;
import io.github.kale_ko.bjsl.elements.ParsedElement;
import io.github.kale_ko.bjsl.elements.ParsedObject;
import io.github.kale_ko.ejcl.PathResolver;
import java.util.List;
import java.util.regex.Pattern;

public class DebugCommand implements ECCommand {
    protected final GlobalModule parent;

    public DebugCommand(GlobalModule parent) {
        this.parent = parent;
    }

    @Override
    public String getName() {
        return "debug";
    }

    @Override
    public String getDescription() {
        return "Get/set data";
    }

    @Override
    public List<String> getAlias() {
        return List.of();
    }

    @Override
    public String getPermission() {
        return "evercraft.global.commands.debug";
    }

    @Override
    public void run(ECPlayer player, String[] args, boolean sendFeedback) {
        if (args.length > 1) {
            if (args[0].equalsIgnoreCase("get")) {
                StringBuilder pathBuilder = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    pathBuilder.append(args[i]).append(" ");
                }
                String path = pathBuilder.toString().trim();
                for (ECPlayer player2 : parent.getPlugin().getServer().getPlayers()) {
                    path = Pattern.compile("\\{" + player2.getName() + "}", Pattern.CASE_INSENSITIVE).matcher(path).replaceAll(player2.getUuid().toString());
                }

                ParsedObject dataElement = BJSL.elementify(parent.getPlugin().getPlayerData()).asObject();
                ParsedElement resolvedElement = PathResolver.resolveElement(dataElement, path);

                player.sendMessage(ECTextFormatter.translateColors("&aGot " + path + ":&r\n") + (resolvedElement != null ? BJSL.stringifyJson(resolvedElement) : "undefined"));
            } else if (args[0].equalsIgnoreCase("set")) {
                StringBuilder pathBuilder = new StringBuilder();
                for (int i = 1; i < args.length - 1; i++) {
                    pathBuilder.append(args[i]).append(" ");
                }
                String path = pathBuilder.toString().trim();
                for (ECPlayer player2 : parent.getPlugin().getServer().getPlayers()) {
                    path = Pattern.compile("\\{" + player2.getName() + "}", Pattern.CASE_INSENSITIVE).matcher(path).replaceAll(player2.getUuid().toString());
                }
                String value = args[args.length - 1];

                ParsedObject dataElement = BJSL.elementify(parent.getPlugin().getPlayerData()).asObject();
                ParsedElement valueElement = BJSL.parseJson(value);
                PathResolver.updateElement(dataElement, path, valueElement, true);
                parent.getPlugin().setPlayerData(BJSL.parse(dataElement, ECPlayerData.class));
                parent.getPlugin().saveData();

                player.sendMessage(ECTextFormatter.translateColors("&aSet " + path + ":&r\n") + BJSL.stringifyJson(valueElement));
            } else if (sendFeedback) {
                player.sendMessage(ECTextFormatter.translateColors("&cExpected \"get\" or \"set\"."));
            }
        } else if (sendFeedback) {
            player.sendMessage(ECTextFormatter.translateColors("&cYou must pass a command and path."));
        }
    }

    @Override
    public List<String> tabComplete(ECPlayer player, String[] args) {
        if (args.length == 1) {
            return List.of("get", "set");
        } else {
            return List.of();
        }
    }
}