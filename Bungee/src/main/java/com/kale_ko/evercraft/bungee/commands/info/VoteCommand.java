package com.kale_ko.evercraft.bungee.commands.info;

import java.util.Arrays;
import java.util.List;
import com.kale_ko.evercraft.bungee.BungeeMain;
import com.kale_ko.evercraft.bungee.commands.BungeeCommand;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

public class VoteCommand extends BungeeCommand {
    public static final String name = "vote";
    public static final String description = "Get the voting links";

    public static final List<String> aliases = Arrays.asList();

    public static final String permission = "evercraft.commands.info.vote";

    @Override
    public void run(CommandSender sender, String[] args) {
        sender.sendMessage(TextComponent.fromLegacyText(BungeeMain.getInstance().getPluginMessages().getString("info.vote")));
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return Arrays.asList();
    }
}