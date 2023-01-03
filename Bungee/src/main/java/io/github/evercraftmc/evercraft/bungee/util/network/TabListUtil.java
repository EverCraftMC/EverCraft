package io.github.evercraftmc.evercraft.bungee.util.network;

import java.util.EnumSet;
import java.util.UUID;
import io.github.evercraftmc.evercraft.bungee.BungeeMain;
import io.github.evercraftmc.evercraft.bungee.util.formatting.ComponentFormatter;
import io.github.evercraftmc.evercraft.shared.util.StringUtils;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.protocol.Property;
import net.md_5.bungee.protocol.packet.PlayerListItem;
import net.md_5.bungee.protocol.packet.PlayerListItemRemove;
import net.md_5.bungee.protocol.packet.PlayerListItemUpdate;

public class TabListUtil {
    public static void addToList(ProxiedPlayer player) {
        for (ProxiedPlayer player2 : BungeeMain.getInstance().getProxy().getPlayers()) {
            if (player == player2) {
                continue;
            }

            PlayerListItemUpdate packet = new PlayerListItemUpdate();
            packet.setActions(EnumSet.of(PlayerListItemUpdate.Action.ADD_PLAYER, PlayerListItemUpdate.Action.UPDATE_DISPLAY_NAME, PlayerListItemUpdate.Action.UPDATE_LATENCY, PlayerListItemUpdate.Action.UPDATE_GAMEMODE));

            PlayerListItem.Item item = new PlayerListItem.Item();
            item.setUuid(player.getUniqueId());
            item.setUsername(player.getName());
            item.setDisplayName(ComponentFormatter.stringToJson(player.getDisplayName()));
            item.setPing(player.getPing());
            item.setGamemode(0);
            item.setProperties(new Property[] {});

            packet.setItems(new PlayerListItem.Item[] { item, item, item });

            player2.unsafe().sendPacket(packet);
        }
    }

    public static void addToList(ProxiedPlayer player, ServerInfo server) {
        for (ProxiedPlayer player2 : BungeeMain.getInstance().getProxy().getPlayers()) {
            if (player == player2) {
                continue;
            }

            PlayerListItemUpdate packet = new PlayerListItemUpdate();
            packet.setActions(EnumSet.of(PlayerListItemUpdate.Action.ADD_PLAYER, PlayerListItemUpdate.Action.UPDATE_DISPLAY_NAME, PlayerListItemUpdate.Action.UPDATE_LATENCY, PlayerListItemUpdate.Action.UPDATE_GAMEMODE));

            PlayerListItem.Item item = new PlayerListItem.Item();
            item.setUuid(player.getUniqueId());
            item.setUsername(player.getName());
            item.setDisplayName(ComponentFormatter.stringToJson(((player2.getServer() != null && player2.getServer().getInfo() != server) ? "[" + StringUtils.toTtitleCase(server.getName()) + "] " : "") + player.getDisplayName()));
            item.setPing(player.getPing());
            item.setGamemode(0);
            item.setProperties(new Property[] {});

            packet.setItems(new PlayerListItem.Item[] { item, item, item });

            player2.unsafe().sendPacket(packet);
        }
    }

    public static void addToList(ProxiedPlayer player, ProxiedPlayer player2) {
        if (player == player2) {
            return;
        }

        PlayerListItemUpdate packet = new PlayerListItemUpdate();
        packet.setActions(EnumSet.of(PlayerListItemUpdate.Action.ADD_PLAYER, PlayerListItemUpdate.Action.UPDATE_DISPLAY_NAME, PlayerListItemUpdate.Action.UPDATE_LATENCY, PlayerListItemUpdate.Action.UPDATE_GAMEMODE));

        PlayerListItem.Item item = new PlayerListItem.Item();
        item.setUuid(player.getUniqueId());
        item.setUsername(player.getName());
        item.setDisplayName(ComponentFormatter.stringToJson(player.getDisplayName()));
        item.setPing(player.getPing());
        item.setGamemode(0);
        item.setProperties(new Property[] {});

        packet.setItems(new PlayerListItem.Item[] { item, item, item });

        player2.unsafe().sendPacket(packet);
    }

    public static void addToList(ProxiedPlayer player, ProxiedPlayer player2, ServerInfo server) {
        if (player == player2) {
            return;
        }

        PlayerListItemUpdate packet = new PlayerListItemUpdate();
        packet.setActions(EnumSet.of(PlayerListItemUpdate.Action.ADD_PLAYER, PlayerListItemUpdate.Action.UPDATE_DISPLAY_NAME, PlayerListItemUpdate.Action.UPDATE_LATENCY, PlayerListItemUpdate.Action.UPDATE_GAMEMODE));

        PlayerListItem.Item item = new PlayerListItem.Item();
        item.setUuid(player.getUniqueId());
        item.setUsername(player.getName());
        item.setDisplayName(ComponentFormatter.stringToJson(((player2.getServer() != null && player2.getServer().getInfo() != server) ? "[" + StringUtils.toTtitleCase(server.getName()) + "] " : "") + player.getDisplayName()));
        item.setPing(player.getPing());
        item.setGamemode(0);
        item.setProperties(new Property[] {});

        packet.setItems(new PlayerListItem.Item[] { item, item, item });

        player2.unsafe().sendPacket(packet);
    }

    public static void removeFromList(ProxiedPlayer player) {
        for (ProxiedPlayer player2 : BungeeMain.getInstance().getProxy().getPlayers()) {
            if (player == player2) {
                continue;
            }

            PlayerListItemRemove packet = new PlayerListItemRemove();
            packet.setUuids(new UUID[] { player.getUniqueId() });

            player2.unsafe().sendPacket(packet);
        }
    }

    public static void removeFromList(ProxiedPlayer player, ProxiedPlayer player2) {
        if (player == player2) {
            return;
        }

        PlayerListItemRemove packet = new PlayerListItemRemove();
        packet.setUuids(new UUID[] { player.getUniqueId() });

        player2.unsafe().sendPacket(packet);
    }

    public static void updatePlayer(ProxiedPlayer player) {
        updatePlayerName(player);
        updatePlayerPing(player);
    }

    public static void updatePlayer(ProxiedPlayer player, ServerInfo server) {
        updatePlayerName(player, server);
        updatePlayerPing(player);
        updatePlayerGamemode(player);
    }

    public static void updatePlayer(ProxiedPlayer player, ProxiedPlayer player2) {
        updatePlayerName(player, player2);
        updatePlayerPing(player, player2);
        updatePlayerGamemode(player, player2);
    }

    public static void updatePlayer(ProxiedPlayer player, ProxiedPlayer player2, ServerInfo server) {
        updatePlayerName(player, player2, server);
        updatePlayerPing(player, player2);
        updatePlayerGamemode(player, player2);
    }

    public static void updatePlayerName(ProxiedPlayer player) {
        for (ProxiedPlayer player2 : BungeeMain.getInstance().getProxy().getPlayers()) {
            PlayerListItemUpdate packet = new PlayerListItemUpdate();
            packet.setActions(EnumSet.of(PlayerListItemUpdate.Action.UPDATE_DISPLAY_NAME));

            PlayerListItem.Item item = new PlayerListItem.Item();
            item.setUuid(player.getUniqueId());
            item.setDisplayName(ComponentFormatter.stringToJson(player.getDisplayName()));

            packet.setItems(new PlayerListItem.Item[] { item });

            player2.unsafe().sendPacket(packet);
        }
    }

    public static void updatePlayerName(ProxiedPlayer player, ServerInfo server) {
        for (ProxiedPlayer player2 : BungeeMain.getInstance().getProxy().getPlayers()) {
            PlayerListItemUpdate packet = new PlayerListItemUpdate();
            packet.setActions(EnumSet.of(PlayerListItemUpdate.Action.UPDATE_DISPLAY_NAME));

            PlayerListItem.Item item = new PlayerListItem.Item();
            item.setUuid(player.getUniqueId());
            item.setDisplayName(ComponentFormatter.stringToJson(((player2.getServer() != null && player2.getServer().getInfo() != server) ? "[" + StringUtils.toTtitleCase(server.getName()) + "] " : "") + player.getDisplayName()));

            packet.setItems(new PlayerListItem.Item[] { item });

            player2.unsafe().sendPacket(packet);
        }
    }

    public static void updatePlayerName(ProxiedPlayer player, ProxiedPlayer player2) {
        PlayerListItemUpdate packet = new PlayerListItemUpdate();
        packet.setActions(EnumSet.of(PlayerListItemUpdate.Action.UPDATE_DISPLAY_NAME));

        PlayerListItem.Item item = new PlayerListItem.Item();
        item.setUuid(player.getUniqueId());
        item.setDisplayName(ComponentFormatter.stringToJson(player.getDisplayName()));

        packet.setItems(new PlayerListItem.Item[] { item });

        player2.unsafe().sendPacket(packet);
    }

    public static void updatePlayerName(ProxiedPlayer player, ProxiedPlayer player2, ServerInfo server) {
        PlayerListItemUpdate packet = new PlayerListItemUpdate();
        packet.setActions(EnumSet.of(PlayerListItemUpdate.Action.UPDATE_DISPLAY_NAME));

        PlayerListItem.Item item = new PlayerListItem.Item();
        item.setUuid(player.getUniqueId());
        item.setDisplayName(ComponentFormatter.stringToJson(((player2.getServer() != null && player2.getServer().getInfo() != server) ? "[" + StringUtils.toTtitleCase(server.getName()) + "] " : "") + player.getDisplayName()));

        packet.setItems(new PlayerListItem.Item[] { item });

        player2.unsafe().sendPacket(packet);
    }

    public static void updatePlayerPing(ProxiedPlayer player) {
        for (ProxiedPlayer player2 : BungeeMain.getInstance().getProxy().getPlayers()) {
            PlayerListItemUpdate packet = new PlayerListItemUpdate();
            packet.setActions(EnumSet.of(PlayerListItemUpdate.Action.UPDATE_LATENCY));

            PlayerListItem.Item item = new PlayerListItem.Item();
            item.setUuid(player.getUniqueId());
            item.setPing(player.getPing());

            packet.setItems(new PlayerListItem.Item[] { item });

            player2.unsafe().sendPacket(packet);
        }
    }

    public static void updatePlayerPing(ProxiedPlayer player, ProxiedPlayer player2) {
        PlayerListItemUpdate packet = new PlayerListItemUpdate();
        packet.setActions(EnumSet.of(PlayerListItemUpdate.Action.UPDATE_LATENCY));

        PlayerListItem.Item item = new PlayerListItem.Item();
        item.setUuid(player.getUniqueId());
        item.setPing(player.getPing());

        packet.setItems(new PlayerListItem.Item[] { item });

        player2.unsafe().sendPacket(packet);
    }

    public static void updatePlayerGamemode(ProxiedPlayer player) {
        for (ProxiedPlayer player2 : BungeeMain.getInstance().getProxy().getPlayers()) {
            if (player == player2) {
                continue;
            }

            PlayerListItemUpdate packet = new PlayerListItemUpdate();
            packet.setActions(EnumSet.of(PlayerListItemUpdate.Action.UPDATE_GAMEMODE));

            PlayerListItem.Item item = new PlayerListItem.Item();
            item.setUuid(player.getUniqueId());
            item.setGamemode(0);

            packet.setItems(new PlayerListItem.Item[] { item });

            player2.unsafe().sendPacket(packet);
        }
    }

    public static void updatePlayerGamemode(ProxiedPlayer player, ProxiedPlayer player2) {
        if (player == player2) {
            return;
        }

        PlayerListItemUpdate packet = new PlayerListItemUpdate();
        packet.setActions(EnumSet.of(PlayerListItemUpdate.Action.UPDATE_GAMEMODE));

        PlayerListItem.Item item = new PlayerListItem.Item();
        item.setUuid(player.getUniqueId());
        item.setGamemode(0);

        packet.setItems(new PlayerListItem.Item[] { item });

        player2.unsafe().sendPacket(packet);
    }
}