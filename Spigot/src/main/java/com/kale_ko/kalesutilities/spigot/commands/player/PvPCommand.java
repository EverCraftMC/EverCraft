package com.kale_ko.kalesutilities.spigot.commands.player;

import java.util.List;
import com.kale_ko.kalesutilities.spigot.SpigotPlugin;
import com.kale_ko.kalesutilities.spigot.Util;
import com.kale_ko.kalesutilities.spigot.commands.SpigotCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PvPCommand extends SpigotCommand {
    public PvPCommand(String name, String description, List<String> aliases, String usage, String permission) {
        super(name, description, aliases, usage, permission);
    }

    @Override
    public void run(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player player) {
            if (Util.hasMetadata(player, "enablePvP")) {
                if (Util.getMetadata(player, "enablePvP").asBoolean()) {
                    Util.setMetadata(player, "enablePvP", false);

                    Util.sendMessage(player, SpigotPlugin.Instance.config.getString("messages.togglepvp").replace("{value}", "off"));
                } else {
                    Util.setMetadata(player, "enablePvP", true);

                    Util.sendMessage(player, SpigotPlugin.Instance.config.getString("messages.togglepvp").replace("{value}", "on"));
                }
            } else {
                Util.setMetadata(player, "enablePvP", false);

                Util.sendMessage(player, SpigotPlugin.Instance.config.getString("messages.togglepvp").replace("{value}", "off"));
            }
        } else {
            Util.sendMessage(sender, SpigotPlugin.Instance.config.getString("messages.noconsole"));
        }
    }
}