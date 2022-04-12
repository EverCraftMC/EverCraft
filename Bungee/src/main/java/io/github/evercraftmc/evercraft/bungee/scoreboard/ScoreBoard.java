package io.github.evercraftmc.evercraft.bungee.scoreboard;

import java.util.List;
import java.util.concurrent.TimeUnit;
import io.github.evercraftmc.evercraft.bungee.BungeeMain;
import io.github.evercraftmc.evercraft.bungee.util.formatting.ComponentFormatter;
import io.github.evercraftmc.evercraft.shared.util.Closable;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.api.score.Objective;
import net.md_5.bungee.api.score.Position;
import net.md_5.bungee.api.score.Score;

public class ScoreBoard implements Closable {
    private ScheduledTask task;

    public ScoreBoard() {
        task = BungeeMain.getInstance().getProxy().getScheduler().schedule(BungeeMain.getInstance(), new Runnable() {
            public void run() {
                for (ProxiedPlayer player : BungeeMain.getInstance().getProxy().getPlayers()) {
                    if (player.getServer() != null) {
                        player.getScoreboard().setName(TextFormatter.translateColors(BungeeMain.getInstance().getPluginConfig().getString("scoreboard.title")));
                        player.getScoreboard().setPosition(Position.SIDEBAR);

                        if (player.getScoreboard().getObjective(player.getName()) == null) {
                            player.getScoreboard().addObjective(new Objective(player.getName(), "dummy", "integer"));
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

                            if (getScore(player, lines.size() - i) != null) {
                                if (!getScore(player, lines.size() - i).equals(line)) {
                                    player.getScoreboard().removeScore(getScore(player, lines.size() - i));
                                    player.getScoreboard().addScore(new Score(line, player.getName(), lines.size() - i));
                                }
                            } else {
                                player.getScoreboard().addScore(new Score(line, player.getName(), lines.size() - i));
                            }
                        }

                        player.setTabHeader(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginConfig().getString("tablist.header")
                            .replace("{player}", player.getDisplayName())
                            .replace("{balance}", BungeeMain.getInstance().getEconomy().getBalance(player.getUniqueId()) + "")
                            .replace("{ping}", player.getPing() + "")
                            .replace("{server}", player.getServer().getInfo().getName())
                            .replace("{serverOnline}", player.getServer().getInfo().getPlayers().size() + "")
                            .replace("{proxyOnline}", BungeeMain.getInstance().getProxy().getOnlineCount() + "")
                            .replace("{proxyMax}", BungeeMain.getInstance().getProxy().getConfigurationAdapter().getListeners().iterator().next().getMaxPlayers() + ""))), ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginConfig().getString("tablist.footer")
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

    public String getScore(ProxiedPlayer player, Integer value) {
        for (Score score : player.getScoreboard().getScores()) {
            if (score.getValue() == value) {
                return score.getScoreName();
            }
        }

        return null;
    }

    public void close() {
        task.cancel();
    }
}