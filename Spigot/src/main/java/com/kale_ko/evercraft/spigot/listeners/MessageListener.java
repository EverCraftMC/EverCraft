package com.kale_ko.evercraft.spigot.listeners;

import java.util.UUID;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.kale_ko.evercraft.spigot.SpigotMain;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class MessageListener extends SpigotListener implements PluginMessageListener {
    @EventHandler
    public void onChatMessage(AsyncPlayerChatEvent event) {
        event.setCancelled(true);

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("globalChat");
        out.writeUTF(SpigotMain.getInstance().getServerName());
        out.writeUTF(event.getPlayer().getUniqueId().toString());
        out.writeUTF(event.getMessage());

        event.getPlayer().sendPluginMessage(SpigotMain.getInstance(), "BungeeCord", out.toByteArray());
    }

    @EventHandler
    public void onDeathMessage(PlayerDeathEvent event) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("globalDeathMessage");
        out.writeUTF(SpigotMain.getInstance().getServerName());
        out.writeUTF(event.getEntity().getUniqueId().toString());
        out.writeUTF(event.getDeathMessage().replace(event.getEntity().getName(), "{player}"));

        event.getEntity().sendPluginMessage(SpigotMain.getInstance(), "BungeeCord", out.toByteArray());

        event.setDeathMessage("");
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
                SpigotMain.getInstance().setServerName(in.readUTF());
            } else if (subChannel.equals("crossCommand")) {
                Player player = SpigotMain.getInstance().getServer().getPlayer(UUID.fromString(in.readUTF()));

                String command = in.readUTF();

                SpigotMain.getInstance().getServer().dispatchCommand(player, command);
            }
        }
    }
}