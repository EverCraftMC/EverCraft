package com.kale_ko.evercraft.spigot.scoreboard;

import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.kale_ko.evercraft.shared.networking.ServerPing;
import com.kale_ko.evercraft.shared.networking.ServerPinger;
import com.kale_ko.evercraft.spigot.SpigotPlugin;
import com.kale_ko.evercraft.spigot.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class ScoreBoard {
    public Map<Player, Scoreboard> scoreBoardMap = new HashMap<Player, Scoreboard>();

    private BukkitTask task;

    public ScoreBoard() {
        task = Bukkit.getScheduler().runTaskTimer(SpigotPlugin.Instance, new Runnable() {
            public void run() {
                update();
            }
        }, 0, 100);
    }

    public void update() {
        for (Player player : SpigotPlugin.Instance.getServer().getOnlinePlayers()) {
            if (!scoreBoardMap.containsKey(player)) {
                Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
                sb.registerNewObjective(player.getName(), "dummy", Util.formatMessage(SpigotPlugin.Instance.config.getString("config.scoreboardTitle")));
                scoreBoardMap.put(player, sb);
            }

            Scoreboard sb = scoreBoardMap.get(player);
            Objective obj = sb.getObjective(player.getName());

            Bukkit.getScheduler().runTaskAsynchronously(SpigotPlugin.Instance, new Runnable() {
                public void run() {
                    ServerPing ping = new ServerPinger(SpigotPlugin.Instance.config.getString("config.proxyHost"), SpigotPlugin.Instance.config.getInt("config.proxyPort"), SpigotPlugin.Instance.config.getInt("config.requestTimeout")).ping();

                    if (ping == null) {
                        ping = new ServerPing();
                    }

                    List<String> lines = SpigotPlugin.Instance.config.getStringList("config.scoreboardLines");
                    int i = 0;

                    for (String line : lines) {
                        line = Util.formatMessage(line
                                .replace("%username%", Util.getPlayerName(player))
                                .replace("%health%", "" + Math.round(player.getHealth() * 100) / 100)
                                .replace("%balance%", "" + Util.formatCurrencyMin(SpigotPlugin.Instance.eco.getBalance(player.getName())))
                                .replace("%ping%", "" + player.getPing())
                                .replace("%onlineplayers%", "" + SpigotPlugin.Instance.getServer().getOnlinePlayers().size())
                                .replace("%maxplayers%", "" + SpigotPlugin.Instance.getServer().getMaxPlayers())
                                .replace("%onlineproxyplayers%", "" + ping.getPlayers().getOnline())
                                .replace("%maxproxyplayers%", "" + ping.getPlayers().getMax())
                                .replace("%uptime%", Util.formatDurationLetters(ManagementFactory.getRuntimeMXBean().getUptime(), true, true)));

                        if (!line.equals(getScore(obj, lines.size() - i))) {
                            if (getScore(obj, lines.size() - i) != null) {
                                sb.resetScores(getScore(obj, lines.size() - i));
                            }
                            obj.getScore(line).setScore(lines.size() - i);
                        }

                        i++;
                    }

                    obj.setDisplaySlot(DisplaySlot.SIDEBAR);
                    player.setScoreboard(sb);
                    player.setPlayerListHeaderFooter(
                            Util.formatMessage(SpigotPlugin.Instance.config.getString("config.tablistHeader")
                                    .replace("%username%", Util.getPlayerName(player))
                                    .replace("%health%", "" + Math.round(player.getHealth() * 100) / 100)
                                    .replace("%balance%", "" + Util.formatCurrencyMin(0)) // SpigotPlugin.Instance.eco.getBalance(player)
                                    .replace("%ping%", "" + player.getPing())
                                    .replace("%onlineplayers%", "" + SpigotPlugin.Instance.getServer().getOnlinePlayers().size())
                                    .replace("%maxplayers%", "" + SpigotPlugin.Instance.getServer().getMaxPlayers())
                                    .replace("%onlineproxyplayers%", "" + ping.getPlayers().getOnline())
                                    .replace("%maxproxyplayers%", "" + ping.getPlayers().getMax())
                                    .replace("%uptime%", Util.formatDurationLetters(ManagementFactory.getRuntimeMXBean().getUptime(), true, true))),
                            Util.formatMessage(SpigotPlugin.Instance.config.getString("config.tablistFooter")
                                    .replace("%username%", Util.getPlayerName(player))
                                    .replace("%health%", "" + Math.round(player.getHealth() * 100) / 100)
                                    .replace("%balance%", "" + Util.formatCurrencyMin(0)) // SpigotPlugin.Instance.eco.getBalance(player)
                                    .replace("%ping%", "" + player.getPing())
                                    .replace("%onlineplayers%", "" + SpigotPlugin.Instance.getServer().getOnlinePlayers().size()))
                                    .replace("%maxplayers%", "" + SpigotPlugin.Instance.getServer().getMaxPlayers())
                                    .replace("%onlineproxyplayers%", "" + ping.getPlayers().getOnline())
                                    .replace("%maxproxyplayers%", "" + ping.getPlayers().getMax())
                                    .replace("%uptime%", Util.formatDurationLetters(ManagementFactory.getRuntimeMXBean().getUptime(), true, true)));

                    for (Map.Entry<Integer, String> entry : SpigotPlugin.Instance.luckperms.getPlayerAdapter(Player.class).getUser(player).getCachedData().getMetaData().getPrefixes().entrySet()) {
                        if (entry.getValue().equals(SpigotPlugin.Instance.luckperms.getPlayerAdapter(Player.class).getUser(player).getCachedData().getMetaData().getPrefix())) {
                            Team team = sb.getTeam("sort" + (9 - entry.getKey()));

                            if (team == null) {
                                team = sb.registerNewTeam("sort" + (9 - entry.getKey()));
                            }

                            team.addEntry(player.getName());
                        }
                    }
                }
            });
        }
    }

    private String getScore(Objective objective, Integer score) {
        for (String entry : objective.getScoreboard().getEntries()) {
            if (objective.getScore(entry).getScore() == score) {
                return entry;
            }
        }

        return null;
    }

    public void close() {
        task.cancel();
    }
}