package io.github.evercraftmc.evercraft.bungee.scoreboard;

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
    private Map<ScoreboardObjective, Map<String, Integer>> linesMap = new HashMap<ScoreboardObjective, Map<String, Integer>>();

    public ScoreBoard() {
        task = BungeeMain.getInstance().getProxy().getScheduler().schedule(BungeeMain.getInstance(), new Runnable() {
            public void run() {
                for (ProxiedPlayer player : BungeeMain.getInstance().getProxy().getPlayers()) {
                    if (player.getServer() != null) {
                        if (!scoreboardMap.containsKey(player)) {
                            scoreboardMap.put(player, new ScoreboardObjective("main", ComponentFormatter.stringToJson(TextFormatter.translateColors(BungeeMain.getInstance().getPluginConfig().getString("scoreboard.title"))), ScoreboardObjective.HealthDisplay.INTEGER, (byte) 0));
                            linesMap.put(scoreboardMap.get(player), new HashMap<String, Integer>());

                            player.unsafe().sendPacket(scoreboardMap.get(player));

                            ScoreboardDisplay display = new ScoreboardDisplay((byte) 1, scoreboardMap.get(player).getName());
                            player.unsafe().sendPacket(display);
                        }

                        List<String> lines = BungeeMain.getInstance().getPluginConfig().getStringList("scoreboard.lines");

                        for (int i = lines.size() - 1; i > 0; i--) {
                            String line = TextFormatter.translateColors(lines.get(i)
                                .replace("{player}", player.getDisplayName())
                                .replace("{balance}", BungeeMain.getInstance().getEconomy().getBalance(player.getUniqueId()) + "")
                                .replace("{ping}", player.getPing() + "")
                                .replace("{server}", player.getServer().getInfo().getName())
                                .replace("{serverOnline}", player.getServer().getInfo().getPlayers().size() + "")
                                .replace("{proxyOnline}", BungeeMain.getInstance().getProxy().getOnlineCount() + "")
                                .replace("{proxyMax}", BungeeMain.getInstance().getProxy().getConfigurationAdapter().getListeners().iterator().next().getMaxPlayers() + ""));

                            if (getScore(scoreboardMap.get(player), i) == null) {
                                ScoreboardScore score = new ScoreboardScore(line, (byte) 0, scoreboardMap.get(player).getName(), i);
                                linesMap.get(scoreboardMap.get(player)).put(score.getItemName(), score.getValue());
                                player.unsafe().sendPacket(score);
                            } else if (!getScore(scoreboardMap.get(player), i).equals(line)) {
                                ScoreboardScore removescore = new ScoreboardScore(line, (byte) 1, scoreboardMap.get(player).getName(), i);
                                linesMap.get(scoreboardMap.get(player)).remove(removescore.getItemName(), removescore.getValue());
                                player.unsafe().sendPacket(removescore);

                                ScoreboardScore score = new ScoreboardScore(line, (byte) 0, scoreboardMap.get(player).getName(), i);
                                linesMap.get(scoreboardMap.get(player)).put(score.getItemName(), score.getValue());
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

    public String getScore(ScoreboardObjective objective, Integer value) {
        for (String line : linesMap.get(objective).keySet()) {
            if (linesMap.get(objective).get(line).equals(value)) {
                return line;
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