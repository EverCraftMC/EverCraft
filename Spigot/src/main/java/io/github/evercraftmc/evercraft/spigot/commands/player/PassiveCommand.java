package io.github.evercraftmc.evercraft.spigot.commands.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import io.github.evercraftmc.evercraft.shared.util.StringUtils;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import io.github.evercraftmc.evercraft.spigot.SpigotMain;
import io.github.evercraftmc.evercraft.spigot.commands.SpigotCommand;
import io.github.evercraftmc.evercraft.spigot.util.formatting.ComponentFormatter;

public class PassiveCommand extends SpigotCommand {
    public PassiveCommand(String name, String description, List<String> aliases, String permission) {
        super(name, description, aliases, permission);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                if (SpigotMain.getInstance().getPluginData().getBoolean("players." + player.getUniqueId() + ".passive." + SpigotMain.getInstance().getServerName())) {
                    SpigotMain.getInstance().getPluginData().set("players." + player.getUniqueId() + ".passive." + SpigotMain.getInstance().getServerName(), false);

                    player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getParsed().passive.replace("{value}", "off"))));
                } else {
                    SpigotMain.getInstance().getPluginData().set("players." + player.getUniqueId() + ".passive." + SpigotMain.getInstance().getServerName(), true);

                    player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getParsed().passive.replace("{value}", "on"))));
                }
            } else {
                if (args[0].equalsIgnoreCase("on")) {
                    SpigotMain.getInstance().getPluginData().set("players." + player.getUniqueId() + ".passive." + SpigotMain.getInstance().getServerName(), true);

                    player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getParsed().passive.replace("{value}", "on"))));
                } else if (args[0].equalsIgnoreCase("off")) {
                    SpigotMain.getInstance().getPluginData().set("players." + player.getUniqueId() + ".passive." + SpigotMain.getInstance().getServerName(), false);

                    player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getParsed().passive.replace("{value}", "off"))));
                }
            }
        } else {
            sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getParsed().error.noConsole)));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<String>();

        if (args.length == 1) {
            list.add("on");
            list.add("off");
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