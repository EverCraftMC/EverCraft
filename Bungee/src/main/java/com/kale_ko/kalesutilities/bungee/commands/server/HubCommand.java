package com.kale_ko.kalesutilities.bungee.commands.server;

import java.util.List;
import com.kale_ko.kalesutilities.bungee.Main;
import com.kale_ko.kalesutilities.bungee.Util;
import com.kale_ko.kalesutilities.bungee.commands.BungeeCommand;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class HubCommand extends BungeeCommand {
    public HubCommand(String name, List<String> aliases, String permission) {
        super(name, aliases, permission);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer player) {
            player.connect(Main.Instance.getProxy().getServerInfo(Main.Instance.config.getString("config.mainServer")));
        } else {
            Util.sendMessage(sender, Main.Instance.config.getString("messages.noconsole"));
        }
    }
}