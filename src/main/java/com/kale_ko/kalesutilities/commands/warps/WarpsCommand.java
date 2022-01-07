package com.kale_ko.kalesutilities.commands.warps;

import com.kale_ko.kalesutilities.Main;
import com.kale_ko.kalesutilities.Util;
import java.util.Set;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class WarpsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (Util.hasPermission(sender, "kalesutilities.warps")) {
            StringBuilder warps = new StringBuilder();

            Set<String> keys = Main.Instance.warps.getConfig().getKeys(false);
            for (String key : keys) {
                warps.append(key + "\n");
            }

            Util.sendMessage(sender, Main.Instance.config.getConfig().getString("messages.warps").replace("{warpList}", warps.toString()));
        } else {
            Util.sendMessage(sender, Main.Instance.config.getConfig().getString("messages.noperms").replace("{permission}", "kalesutilities.warps"));
        }

        return true;
    }
}