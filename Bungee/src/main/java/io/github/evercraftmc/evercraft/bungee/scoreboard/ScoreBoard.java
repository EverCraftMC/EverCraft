package io.github.evercraftmc.evercraft.bungee.scoreboard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import io.github.evercraftmc.evercraft.bungee.BungeeMain;
import io.github.evercraftmc.evercraft.bungee.util.formatting.ComponentFormatter;
import io.github.evercraftmc.evercraft.shared.util.Closable;
import io.github.evercraftmc.evercraft.shared.util.StringUtils;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.protocol.packet.ScoreboardDisplay;
import net.md_5.bungee.protocol.packet.ScoreboardObjective;
import net.md_5.bungee.protocol.packet.ScoreboardScore;

public class ScoreBoard implements Closable {
    private static ScoreBoard Instance;

    private Map<ProxiedPlayer, ScoreboardObjective> scoreboardMap = new HashMap<ProxiedPlayer, ScoreboardObjective>();
    private Map<ProxiedPlayer, Map<String, Integer>> linesMap = new HashMap<ProxiedPlayer, Map<String, Integer>>();

    private ScheduledTask task;

    public ScoreBoard() {
        ScoreBoard.Instance = this;

        task = BungeeMain.getInstance().getProxy().getScheduler().schedule(BungeeMain.getInstance(), new Runnable() {
            public void run() {
                for (ProxiedPlayer player : BungeeMain.getInstance().getProxy().getPlayers()) {
                    if (player.getServer() != null) {
                        if (!scoreboardMap.containsKey(player)) {
                            scoreboardMap.put(player, new ScoreboardObjective(player.getName(), ComponentFormatter.stringToJson(TextFormatter.translateColors(BungeeMain.getInstance().getPluginConfig().getString("scoreboard.title"))), ScoreboardObjective.HealthDisplay.INTEGER, (byte) 0));
                            linesMap.put(player, new HashMap<String, Integer>());

                            player.unsafe().sendPacket(scoreboardMap.get(player));

                            ScoreboardDisplay display = new ScoreboardDisplay((byte) 1, scoreboardMap.get(player).getName());
                            player.unsafe().sendPacket(display);
                        }

                        List<String> lines = BungeeMain.getInstance().getPluginConfig().getStringList("scoreboard.lines");

                        for (int i = lines.size() - 1; i >= 0; i--) {
                            String line = TextFormatter.translateColors(lines.get(i)
                                .replace("{player}", player.getDisplayName())
                                .replace("{balance}", BungeeMain.getInstance().getEconomy().getBalance(player.getUniqueId()) + "")
                                .replace("{ping}", player.getPing() + "")
                                .replace("{server}", StringUtils.toTtitleCase(player.getServer().getInfo().getName()))
                                .replace("{serverOnline}", player.getServer().getInfo().getPlayers().size() + "")
                                .replace("{proxyOnline}", BungeeMain.getInstance().getProxy().getOnlineCount() + "")
                                .replace("{proxyMax}", BungeeMain.getInstance().getProxy().getConfigurationAdapter().getListeners().iterator().next().getMaxPlayers() + ""));

                            if (getScore(player, lines.size() - i) == null) {
                                ScoreboardScore score = new ScoreboardScore(line, (byte) 0, scoreboardMap.get(player).getName(), lines.size() - i);
                                linesMap.get(player).put(score.getItemName(), score.getValue());
                                player.unsafe().sendPacket(score);
                            } else if (!getScore(player, lines.size() - i).equals(line)) {
                                ScoreboardScore removescore = new ScoreboardScore(getScore(player, lines.size() - i), (byte) 1, scoreboardMap.get(player).getName(), lines.size() - i);
                                linesMap.get(player).remove(removescore.getItemName());
                                player.unsafe().sendPacket(removescore);

                                ScoreboardScore score = new ScoreboardScore(line, (byte) 0, scoreboardMap.get(player).getName(), lines.size() - i);
                                linesMap.get(player).put(score.getItemName(), score.getValue());
                                player.unsafe().sendPacket(score);
                            }
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

    public static ScoreBoard getInstance() {
        return ScoreBoard.Instance;
    }

    public Map<ProxiedPlayer, ScoreboardObjective> getScoreboardMap() {
        return this.scoreboardMap;
    }

    public Map<ProxiedPlayer, Map<String, Integer>> getLinesMap() {
        return this.linesMap;
    }

    private String getScore(ProxiedPlayer player, Integer value) {
        for (Map.Entry<String, Integer> line : linesMap.get(player).entrySet()) {
            if (line.getValue().equals(value)) {
                return line.getKey();
            }
        }

        return null;
    }

    public void close() {
        for (ProxiedPlayer player : BungeeMain.getInstance().getProxy().getPlayers()) {
            if (scoreboardMap.containsKey(player)) {
                player.unsafe().sendPacket(new ScoreboardObjective(scoreboardMap.get(player).getName(), null, null, (byte) 2));

                scoreboardMap.remove(player);
            }

            player.setTabHeader(new TextComponent(), new TextComponent());
        }

        task.cancel();
    }
}