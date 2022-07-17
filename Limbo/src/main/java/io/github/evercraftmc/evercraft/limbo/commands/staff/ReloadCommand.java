package io.github.evercraftmc.evercraft.limbo.commands.staff;

import java.util.Arrays;
import java.util.List;
import com.loohp.limbo.commands.CommandSender;
import io.github.evercraftmc.evercraft.limbo.LimboMain;
import io.github.evercraftmc.evercraft.limbo.commands.LimboCommand;
import io.github.evercraftmc.evercraft.limbo.util.formatting.ComponentFormatter;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;

public class ReloadCommand extends LimboCommand {
    public ReloadCommand(String name, String description, List<String> aliases, String permission) {
        super(name, description, aliases, permission);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(LimboMain.getInstance().getPluginMessages().getParsed().reload.reloading)));

        LimboMain.getInstance().reload();

        sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(LimboMain.getInstance().getPluginMessages().getParsed().reload.reloaded)));
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return Arrays.asList();
    }
}