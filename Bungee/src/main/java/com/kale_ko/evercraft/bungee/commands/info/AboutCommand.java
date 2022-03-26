package com.kale_ko.evercraft.bungee.commands.info;

import java.util.Arrays;
import java.util.List;
import com.kale_ko.evercraft.bungee.BungeeMain;
import com.kale_ko.evercraft.bungee.commands.BungeeCommand;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

public class AboutCommand extends BungeeCommand {
    public static final String name = "about";
    public static final String description = "Get info about the server";

    public static final List<String> aliases = Arrays.asList("info");

    public static final String permission = "evercraft.commands.info.about";

    @Override
    public void run(CommandSender sender, String[] args) {
        sender.sendMessage(TextComponent.fromLegacyText(BungeeMain.getInstance().getPluginMessages().getString("info.about")));
    }
}