package com.kale_ko.kalesutilities.spigot.commands.economy;

import java.util.Collection;
import java.util.List;
import com.kale_ko.kalesutilities.spigot.SpigotPlugin;
import com.kale_ko.kalesutilities.spigot.Util;
import com.kale_ko.kalesutilities.spigot.commands.SpigotCommand;
import org.bukkit.command.CommandSender;

public class BalanceTopCommand extends SpigotCommand {
    public BalanceTopCommand(String name, String description, List<String> aliases, String usage, String permission) {
        super(name, description, aliases, usage, permission);
    }

    @Override
    public void run(CommandSender sender, String label, String[] args) {
        Float total = 0f;
        StringBuilder top = new StringBuilder();

        Collection<String> keys = SpigotPlugin.Instance.players.getKeys();
        for (int i = 0; i < 8; i++) {
            String topPlayer = null;

            for (String key : keys) {
                if (key.split("\\.").length == 1) {
                    String player = key.split("\\.")[0];

                    total += SpigotPlugin.Instance.eco.getBalance(player);

                    if (topPlayer == null || SpigotPlugin.Instance.eco.getBalance(player) > SpigotPlugin.Instance.eco.getBalance(topPlayer)) {
                        topPlayer = player;
                    }
                }
            }

            top.append("&7" + topPlayer + " &a- " + Util.formatCurrencyMin(SpigotPlugin.Instance.eco.getBalance(topPlayer)) + "\n");
            keys.remove("players." + topPlayer);
        }

        Util.sendMessage(sender, "&a&lBalance Top\n&aServer Total: " + Util.formatCurrencyMin(total) + "\n" + top.substring(0, top.length() - 2));
    }
}