package io.github.evercraftmc.evercraft.bungee.commands.link;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import io.github.evercraftmc.evercraft.bungee.BungeeMain;
import io.github.evercraftmc.evercraft.bungee.commands.BungeeCommand;
import io.github.evercraftmc.evercraft.bungee.util.formatting.ComponentFormatter;
import io.github.evercraftmc.evercraft.shared.PluginData;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class LinkCommand extends BungeeCommand {
    public LinkCommand(String name, String description, List<String> aliases, String permission) {
        super(name, description, aliases, permission);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer player) {
            Random random = new Random();
            StringBuilder sb = new StringBuilder();
            while (sb.length() < 8) {
                sb.append(Integer.toHexString(random.nextInt()));
            }
            String code = sb.toString().substring(0, 8).toUpperCase();

            PluginData.Linking linking = new PluginData.Linking();
            linking.account = player.getUniqueId().toString();
            linking.expires = Instant.now().plus(5, ChronoUnit.MINUTES).getEpochSecond();
            BungeeMain.getInstance().getPluginData().getParsed().linking.put(code, linking);
            BungeeMain.getInstance().getPluginData().save();

            sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().linking.replace("{code}", code))));
        } else {
            sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getParsed().error.noConsole)));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return Arrays.asList();
    }
}