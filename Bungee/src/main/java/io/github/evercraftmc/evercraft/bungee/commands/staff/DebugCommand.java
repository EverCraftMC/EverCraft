package io.github.evercraftmc.evercraft.bungee.commands.staff;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import io.github.evercraftmc.evercraft.bungee.BungeeMain;
import io.github.evercraftmc.evercraft.bungee.commands.BungeeCommand;
import io.github.evercraftmc.evercraft.bungee.util.formatting.ComponentFormatter;
import io.github.evercraftmc.evercraft.bungee.util.player.BungeePlayerResolver;
import io.github.evercraftmc.evercraft.shared.util.StringUtils;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import net.md_5.bungee.api.CommandSender;

public class DebugCommand extends BungeeCommand {
    private static Gson gson;

    static {
        DebugCommand.gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().serializeSpecialFloatingPointValues().create();
    }

    public DebugCommand(String name, String description, List<String> aliases, String permission) {
        super(name, description, aliases, permission);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (args.length > 1) {
            JsonElement json = null;
            if (args[0].equalsIgnoreCase("config")) {
                json = BungeeMain.getInstance().getPluginConfig().getRaw();
            } else if (args[0].equalsIgnoreCase("messages")) {
                json = BungeeMain.getInstance().getPluginMessages().getRaw();
            } else if (args[0].equalsIgnoreCase("data")) {
                json = BungeeMain.getInstance().getPluginData().getRaw();
            }

            if (json != null) {
                String[] path = args[1].split("\\.");

                for (String part : path) {
                    if (part.startsWith("{") && part.endsWith("}")) {
                        UUID uuid = BungeePlayerResolver.getUUIDFromName(BungeeMain.getInstance().getPluginData(), part.substring(1, part.length() - 1));
                        if (uuid != null) {
                            part = uuid.toString();
                        } else {
                            json = null;

                            break;
                        }
                    }

                    if (json.isJsonObject()) {
                        json = json.getAsJsonObject().get(part);
                    } else if (json.isJsonArray()) {
                        json = json.getAsJsonArray().get(Integer.parseInt(part));
                    } else {
                        break;
                    }

                    if (json == null || json.isJsonNull() || json.isJsonPrimitive()) {
                        break;
                    }
                }

                String string = null;

                if (json == null || json.isJsonNull()) {
                    string = "null";
                } else if (json.isJsonObject() || json.isJsonArray()) {
                    string = gson.toJson(json);
                } else {
                    string = json.getAsString();
                }

                sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.removeColors(args[1] + " in " + args[0] + " has the value of \n" + string + "")));
            } else {
                sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().error.invalidArgs)));
            }
        } else {
            sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().error.invalidArgs)));
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