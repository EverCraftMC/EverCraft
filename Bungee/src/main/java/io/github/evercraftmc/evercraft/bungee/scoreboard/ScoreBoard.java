package io.github.evercraftmc.evercraft.bungee.scoreboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import io.github.evercraftmc.evercraft.bungee.BungeeMain;
import io.github.evercraftmc.evercraft.bungee.util.formatting.ComponentFormatter;
import io.github.evercraftmc.evercraft.shared.util.Closable;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.protocol.packet.ScoreboardDisplay;
import net.md_5.bungee.protocol.packet.ScoreboardObjective;
import net.md_5.bungee.protocol.packet.ScoreboardScore;

public class ScoreBoard implements Closable {
    private ScheduledTask task;

    private Map<ProxiedPlayer, ScoreboardObjective> scoreboardMap = new HashMap<ProxiedPlayer, ScoreboardObjective>();
    private Map<ScoreboardObjective, List<ScoreboardScore>> linesMap = new HashMap<ScoreboardObjective, List<ScoreboardScore>>();

    public ScoreBoard() {
        task = BungeeMain.getInstance().getProxy().getScheduler().schedule(BungeeMain.getInstance(), new Runnable() {
            public void run() {
                for (ProxiedPlayer player : BungeeMain.getInstance().getProxy().getPlayers()) {
                    if (player.getServer() != null) {
                        if (!scoreboardMap.containsKey(player)) {
                            scoreboardMap.put(player, new ScoreboardObjective(player.getName(), TextFormatter.translateColors(BungeeMain.getInstance().getPluginConfig().getString("scoreboard.title")), ScoreboardObjective.HealthDisplay.INTEGER, (byte) 0));
                            linesMap.put(scoreboardMap.get(player), new ArrayList<ScoreboardScore>());

                            player.unsafe().sendPacket(scoreboardMap.get(player));

                            ScoreboardDisplay display = new ScoreboardDisplay((byte) 1, scoreboardMap.get(player).getName());
                            player.unsafe().sendPacket(display);
                        }

                        List<String> lines = BungeeMain.getInstance().getPluginConfig().getStringList("scoreboard.lines");

                        for (int i = 0; i < lines.size(); i++) {
                            String line = TextFormatter.translateColors(lines.get(i)
                                .replace("{player}", player.getDisplayName())
                                .replace("{balance}", BungeeMain.getInstance().getEconomy().getBalance(player.getUniqueId()) + "")
                                .replace("{ping}", player.getPing() + "")
                                .replace("{server}", player.getServer().getInfo().getName())
                                .replace("{serverOnline}", player.getServer().getInfo().getPlayers().size() + "")
                                .replace("{proxyOnline}", BungeeMain.getInstance().getProxy().getOnlineCount() + "")
                                .replace("{proxyMax}", BungeeMain.getInstance().getProxy().getConfigurationAdapter().getListeners().iterator().next().getMaxPlayers() + ""));

                            ScoreboardScore score = new ScoreboardScore(scoreboardMap.get(player).getName(), (byte) 0, line, i);
                            linesMap.get(scoreboardMap.get(player)).add(score);
                            player.unsafe().sendPacket(score);
                        }

                        player.setTabHeader(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginConfig().getString("tablist.header")
                            .replace("{player}", player.getDisplayName())
                            .replace("{balance}", BungeeMain.getInstance().getEconomy().getBalance(player.getUniqueId()) + "")
                            .replace("{ping}", player.getPing() + "")
                            .replace("{server}", player.getServer().getInfo().getName())
                            .replace("{serverOnline}", player.getServer().getInfo().getPlayers().size() + "")
                            .replace("{proxyOnline}", BungeeMain.getInstance().getProxy().getOnlineCount() + "")
                            .replace("{proxyMax}", BungeeMain.getInstance().getProxy().getConfigurationAdapter().getListeners().iterator().next().getMaxPlayers() + ""))),
                        ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginConfig().getString("tablist.footer")
                            .replace("{player}", player.getDisplayName())
                            .replace("{balance}", BungeeMain.getInstance().getEconomy().getBalance(player.getUniqueId()) + "")
                            .replace("{ping}", player.getPing() + "")
                            .replace("{server}", player.getServer().getInfo().getName())
                            .replace("{serverOnline}", player.getServer().getInfo().getPlayers().size() + "")
                            .replace("{proxyOnline}", BungeeMain.getInstance().getProxy().getOnlineCount() + "")
                            .replace("{proxyMax}", BungeeMain.getInstance().getProxy().getConfigurationAdapter().getListeners().iterator().next().getMaxPlayers() + ""))));
                    }
                }
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    public void close() {
        for (ProxiedPlayer player : BungeeMain.getInstance().getProxy().getPlayers()) {
            if (scoreboardMap.containsKey(player)) {
                player.unsafe().sendPacket(new ScoreboardObjective(scoreboardMap.get(player).getName(), scoreboardMap.get(player).getValue(), scoreboardMap.get(player).getType(), (byte) 2));

                scoreboardMap.remove(player);
            }

            player.setTabHeader(new TextComponent(), new TextComponent());
        }

        task.cancel();
    }
}