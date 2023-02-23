package io.github.evercraftmc.evercraft.limbo.commands.staff;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.loohp.limbo.commands.CommandSender;
import io.github.evercraftmc.evercraft.limbo.LimboMain;
import io.github.evercraftmc.evercraft.limbo.commands.LimboCommand;
import io.github.evercraftmc.evercraft.limbo.util.formatting.ComponentFormatter;
import io.github.evercraftmc.evercraft.shared.util.StringUtils;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import io.github.kale_ko.bjsl.BJSL;
import io.github.kale_ko.bjsl.elements.ParsedElement;

public class DebugCommand extends LimboCommand {
    public DebugCommand(String name, String description, List<String> aliases, String permission) {
        super(name, description, aliases, permission);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (args.length > 1) {
            ParsedElement json = null;
            if (args[0].equalsIgnoreCase("config")) {
                json = BJSL.parseJson(BJSL.stringifyJson(LimboMain.getInstance().getPluginConfig().get()));
            } else if (args[0].equalsIgnoreCase("messages")) {
                json = BJSL.parseJson(BJSL.stringifyJson(LimboMain.getInstance().getPluginMessages().get()));
            }

            if (json != null) {
                String[] path = args[1].split("\\.");

                for (String part : path) {
                    if (json.isObject()) {
                        json = json.asObject().get(part);
                    } else if (json.isArray()) {
                        json = json.asArray().get(Integer.parseInt(part));
                    } else {
                        break;
                    }

                    if (json == null || json.isPrimitive()) {
                        break;
                    }
                }

                String string = BJSL.stringifyJson(json);

                sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.removeColors(args[1] + " in " + args[0] + " has the value of \n" + string + "")));
            } else {
                sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(LimboMain.getInstance().getPluginMessages().get().error.invalidArgs)));
            }
        } else {
            sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(LimboMain.getInstance().getPluginMessages().get().error.invalidArgs)));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<String>();

        if (args.length == 1) {
            list.add("config");
            list.add("messages");
            list.add("data");
        } else {
            return Arrays.asList();
        }

        if (args.length > 0) {
            return StringUtils.matchPartial(args[args.length - 1], list);
        } else {
            return list;
        }
    }
}