package com.kale_ko.evercraft.bungee.commands.staff;

import java.util.Arrays;
import java.util.List;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.kale_ko.evercraft.bungee.BungeeMain;
import com.kale_ko.evercraft.bungee.commands.BungeeCommand;
import com.kale_ko.evercraft.shared.util.formatting.TextFormatter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class StaffChatCommand extends BungeeCommand {
    public static final String name = "staffchat";
    public static final String description = "Message the other staff";

    public static final List<String> aliases = Arrays.asList("sc");

    public static final String permission = "evercraft.commands.staff.staffchat";

    @Override
    public void run(CommandSender sender, String[] args) {
        String senderName;
        if (sender instanceof ProxiedPlayer player) {
            senderName = player.getDisplayName();
        } else {
            senderName = "CONSOLE";
        }

        StringBuilder message = new StringBuilder();

        for (String arg : args) {
            message.append(arg + " ");
        }

        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("globalPermMessage");
        out.writeUTF(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("chat.staff").replace("{player}", senderName).replace("{message}", message.substring(0, message.length() - 1))));
        out.writeUTF(permission);

        for (ServerInfo server : BungeeMain.getInstance().getProxy().getServers().values()) {
            server.sendData("BungeeCord", out.toByteArray());
        }

        BungeeMain.getInstance().getDiscordBot().getGuild().getTextChannelById(BungeeMain.getInstance().getPluginConfig().getString("discord.staffChannelId")).sendMessage(TextFormatter.discordFormat(BungeeMain.getInstance().getPluginMessages().getString("chat.staff").replace("{player}", senderName).replace("{message}", message.substring(0, message.length() - 1)))).queue();
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return Arrays.asList();
    }
}