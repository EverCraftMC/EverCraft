package com.kale_ko.evercraft.spigot.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.kale_ko.evercraft.spigot.SpigotMain;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
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

        SpigotMain.getInstance().getServer().sendPluginMessage(SpigotMain.getInstance(), "BungeeCord", out.toByteArray());
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("globalCommandSpy");
        out.writeUTF(SpigotMain.getInstance().getServerName());
        out.writeUTF(event.getPlayer().getUniqueId().toString());
        out.writeUTF(event.getMessage());

        SpigotMain.getInstance().getServer().sendPluginMessage(SpigotMain.getInstance(), "BungeeCord", out.toByteArray());
    }

    @Override
    public void onPluginMessageReceived(String channel, Player sender, byte[] data) {
        if (channel.equals("BungeeCord")) {
            ByteArrayDataInput in = ByteStreams.newDataInput(data);

            String subChannel = in.readUTF();
            if (subChannel.equals("GetServer")) {
                SpigotMain.getInstance().setServerName(in.readUTF());
            } else if (subChannel.equals("globalMessage")) {
                String message = in.readUTF();

                for (Player player : SpigotMain.getInstance().getServer().getOnlinePlayers()) {
                    player.sendMessage(message);
                }

                SpigotMain.getInstance().getServer().getConsoleSender().sendMessage(message);
            } else if (subChannel.equals("globalPermMessage")) {
                String message = in.readUTF();
                String perm = in.readUTF();

                for (Player player : SpigotMain.getInstance().getServer().getOnlinePlayers()) {
                    if (player.hasPermission(perm)) {
                        player.sendMessage(message);
                    }
                }

                SpigotMain.getInstance().getServer().getConsoleSender().sendMessage(message);
            } else if (subChannel.equals("globalCondMessage")) {
                String message = in.readUTF();
                String cond = in.readUTF();

                for (Player player : SpigotMain.getInstance().getServer().getOnlinePlayers()) {
                    if (SpigotMain.getInstance().getData().getBoolean("players." + player.getUniqueId() + "." + cond) != null && SpigotMain.getInstance().getData().getBoolean("players." + player.getUniqueId() + "." + cond)) {
                        player.sendMessage(message);
                    }
                }

                SpigotMain.getInstance().getServer().getConsoleSender().sendMessage(message);
            } else if (subChannel.equals("globalPermCondMessage")) {
                String message = in.readUTF();
                String perm = in.readUTF();
                String cond = in.readUTF();

                for (Player player : SpigotMain.getInstance().getServer().getOnlinePlayers()) {
                    if (player.hasPermission(perm) && SpigotMain.getInstance().getData().getBoolean("players." + player.getUniqueId() + "." + cond) != null && SpigotMain.getInstance().getData().getBoolean("players." + player.getUniqueId() + "." + cond)) {
                        player.sendMessage(message);
                    }
                }

                SpigotMain.getInstance().getServer().getConsoleSender().sendMessage(message);
            }
        }
    }
}