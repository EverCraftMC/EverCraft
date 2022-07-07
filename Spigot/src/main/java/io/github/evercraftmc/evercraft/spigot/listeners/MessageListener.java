package io.github.evercraftmc.evercraft.spigot.listeners;

import java.util.UUID;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import io.github.evercraftmc.evercraft.shared.util.StringUtils;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import io.github.evercraftmc.evercraft.spigot.SpigotMain;
import io.github.evercraftmc.evercraft.spigot.util.formatting.ComponentFormatter;
import org.bukkit.GameRule;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.luckperms.api.LuckPermsProvider;

public class MessageListener extends SpigotListener implements PluginMessageListener {
    @EventHandler
    public void onChatMessage(AsyncChatEvent event) {
        event.setCancelled(true);

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("globalChat");
        out.writeUTF(SpigotMain.getInstance().getServerName());
        out.writeUTF(event.getPlayer().getUniqueId().toString());
        out.writeUTF(ComponentFormatter.componentToString(event.message()));

        event.getPlayer().sendPluginMessage(SpigotMain.getInstance(), "BungeeCord", out.toByteArray());
    }

    @EventHandler
    public void onDeathMessage(PlayerDeathEvent event) {
        if (event.getPlayer().getWorld().getGameRuleValue(GameRule.SHOW_DEATH_MESSAGES)) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("globalPlayerMessage");
            out.writeUTF(SpigotMain.getInstance().getServerName());
            out.writeUTF(event.getEntity().getUniqueId().toString());
            out.writeUTF(ComponentFormatter.componentToString(event.deathMessage()).replace(event.getEntity().getName(), "{player}"));

            event.getEntity().sendPluginMessage(SpigotMain.getInstance(), "BungeeCord", out.toByteArray());

            event.deathMessage(Component.empty());
        }
    }

    @EventHandler
    public void onAdvancementMessage(PlayerAdvancementDoneEvent event) {
        if (event.getPlayer().getWorld().getGameRuleValue(GameRule.ANNOUNCE_ADVANCEMENTS) && event.message() != null) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("globalPlayerMessage");
            out.writeUTF(SpigotMain.getInstance().getServerName());
            out.writeUTF(event.getPlayer().getUniqueId().toString());
            out.writeUTF(ComponentFormatter.componentToString(event.message()).replace(event.getPlayer().getName(), "{player}"));

            event.getPlayer().sendPluginMessage(SpigotMain.getInstance(), "BungeeCord", out.toByteArray());

            event.message(Component.empty());
        }
    }

    @EventHandler
    public void onGamemodeChange(PlayerGameModeChangeEvent event) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("updateGamemode");
        out.writeUTF(event.getPlayer().getUniqueId().toString());

        event.getPlayer().sendPluginMessage(SpigotMain.getInstance(), "BungeeCord", out.toByteArray());
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("globalCommandSpy");
        out.writeUTF(SpigotMain.getInstance().getServerName());
        out.writeUTF(event.getPlayer().getUniqueId().toString());
        out.writeUTF(event.getMessage());

        event.getPlayer().sendPluginMessage(SpigotMain.getInstance(), "BungeeCord", out.toByteArray());
    }

    @Override
    public void onPluginMessageReceived(String channel, Player sender, byte[] data) {
        if (channel.equals("BungeeCord")) {
            ByteArrayDataInput in = ByteStreams.newDataInput(data);

            String subChannel = in.readUTF();
            if (subChannel.equals("GetServer")) {
                SpigotMain.getInstance().setServerName(StringUtils.toTtitleCase(in.readUTF()));
            } else if (subChannel.equals("crossCommand")) {
                Player player = SpigotMain.getInstance().getServer().getPlayer(UUID.fromString(in.readUTF()));

                String command = in.readUTF();

                SpigotMain.getInstance().getServer().dispatchCommand(player, command);
            } else if (subChannel.equals("updateName")) {
                Player player = SpigotMain.getInstance().getServer().getPlayer(UUID.fromString(in.readUTF()));

                player.displayName(ComponentFormatter.stringToComponent(TextFormatter.translateColors(LuckPermsProvider.get().getUserManager().getUser(player.getUniqueId()).getCachedData().getMetaData().getPrefix() + SpigotMain.getInstance().getData().getString("players." + player.getUniqueId() + ".nickname"))));
            } else if (subChannel.equals("playSound")) {
                Player player = SpigotMain.getInstance().getServer().getPlayer(UUID.fromString(in.readUTF()));

                player.playSound(player, Sound.valueOf(in.readUTF()), SoundCategory.valueOf(in.readUTF()), in.readFloat(), in.readFloat());
            }
        }
    }
}