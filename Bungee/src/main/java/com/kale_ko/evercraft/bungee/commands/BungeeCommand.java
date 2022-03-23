package com.kale_ko.evercraft.bungee.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import com.kale_ko.evercraft.bungee.BungeeMain;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public abstract class BungeeCommand extends Command implements TabExecutor {
    public static final String name = null;
    public static final String description = "";

    public static final List<String> aliases = new ArrayList<String>();

    public static final String permission = null;

    public static final List<Map<String[], String[]>> tabComplete = new ArrayList<Map<String[], String[]>>();

    protected BungeeCommand() {
        super(name, permission, aliases.toArray(new String[] {}));
        this.setPermissionMessage(BungeeMain.getInstance().getPluginMessages().getString("error.noPerms").replace("{permission}", permission));

        this.register();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission(permission)) {
            this.run(sender, args);
        }
    }

    public void run(CommandSender sender, String[] args) { }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        return StringUtil.copyPartialMatches(args[args.length - 1], Arrays.asList(tabComplete.get(args.length - 1).get(tabComplete.subList(0, args.length - 2).toArray(new String[] {}))));
    }

    public class StringUtil {
        public static List<String> copyPartialMatches(String token, List<String> originals) {
            List<String> results = new ArrayList<String>();

            for (String string : originals) {
                if (startsWithIgnoreCase(string, token)) {
                    results.add(string);
                }
            }

            return results;
        }

        public static boolean startsWithIgnoreCase(String string, String prefix) {
            if (string.length() < prefix.length()) {
                return false;
            }

            return string.regionMatches(true, 0, prefix, 0, prefix.length());
        }
    }

    public void register() {
        BungeeMain.getInstance().getProxy().getPluginManager().registerCommand(BungeeMain.getInstance(), this);
    }
}