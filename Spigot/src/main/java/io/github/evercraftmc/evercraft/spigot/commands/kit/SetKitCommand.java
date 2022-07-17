package io.github.evercraftmc.evercraft.spigot.commands.kit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import io.github.evercraftmc.evercraft.spigot.SpigotMain;
import io.github.evercraftmc.evercraft.spigot.commands.SpigotCommand;
import io.github.evercraftmc.evercraft.spigot.util.formatting.ComponentFormatter;
import io.github.evercraftmc.evercraft.spigot.util.types.SerializableItemStack;

public class SetKitCommand extends SpigotCommand {
    public SetKitCommand(String name, String description, List<String> aliases, String permission) {
        super(name, description, aliases, permission);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            if (args.length >= 1) {
                List<SerializableItemStack> items = new ArrayList<SerializableItemStack>();

                for (ItemStack item : player.getInventory().getContents()) {
                    if (item != null) {
                        items.add(SerializableItemStack.fromBukkitItemStack(item));
                    }
                }

                if (SpigotMain.getInstance().getKits().getParsed().kits.containsKey(args[0])) {
                    SpigotMain.getInstance().getKits().getParsed().kits.remove(args[0]);
                }

                SpigotMain.getInstance().getKits().getParsed().kits.put(args[0], items);
                SpigotMain.getInstance().getKits().save();

                player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getParsed().kit.setKit.replace("{kit}", args[0]))));
            } else {
                sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getParsed().error.invalidArgs)));
            }
        } else {
            sender.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotMain.getInstance().getPluginMessages().getParsed().error.noConsole)));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return Arrays.asList();
    }
}