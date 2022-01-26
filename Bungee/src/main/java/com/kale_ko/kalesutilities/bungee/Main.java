package com.kale_ko.kalesutilities.bungee;

import java.util.Arrays;
import java.util.logging.Logger;
import com.kale_ko.kalesutilities.bungee.commands.info.KalesUtilitiesBungeeCommand;
import com.kale_ko.kalesutilities.bungee.commands.server.HubCommand;
import com.kale_ko.kalesutilities.bungee.listeners.PlayerJoinListener;
import com.kale_ko.kalesutilities.shared.mysql.MySQLConfig;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

public class Main extends Plugin {
    public static Main Instance;

    public final Logger Console = getLogger();

    public BungeeConfig config;
    public MySQLConfig players;

    @Override
    public void onEnable() {
        Main.Instance = this;

        Console.info("Loading config..");

        config = BungeeConfig.load("config.yml");

        config.addDefault("config.mainServer", "hub");
        config.addDefault("database.url", "localhost");
        config.addDefault("database.port", "3306");
        config.addDefault("database.database", "minecraft");
        config.addDefault("database.username", "root");
        config.addDefault("database.password", "");
        config.addDefault("database.tablePrefix", "kalesutilities_");
        config.addDefault("messages.invalidCommand", "{command} is not a command");
        config.addDefault("messages.noperms", "You need the permission {permission} to run that command");
        config.addDefault("messages.noconsole", "You can't use that command from the console");
        config.addDefault("messages.allreadyconnected", "You are already connected to that server");
        config.addDefault("messages.playernotfound", "{player} can't be found");
        config.addDefault("messages.usage", "Usage: {usage}");
        config.addDefault("messages.help", "\n{commandList}");
        config.addDefault("messages.list", "\n{playerList}");
        config.addDefault("messages.reload", "Config Reloaded");

        config.copyDefaults();

        Console.info("Finished loading config");

        Console.info("Loading data..");

        players = MySQLConfig.load(config.getString("database.url"), config.getInt("database.port"), config.getString("database.database"), config.getString("database.tablePrefix") + "players", config.getString("database.username"), config.getString("database.password"));

        Console.info("Finished loading data");

        Console.info("Loading commands..");

        getProxy().getPluginManager().registerCommand(this, new KalesUtilitiesBungeeCommand("kalesutilitiesbungee", Arrays.asList("kub", "ksb"), "kalesutilities.commands.info.kalesutilities"));
        getProxy().getPluginManager().registerCommand(this, new HubCommand("hub", Arrays.asList("lobby"), "kalesutilities.commands.server.hub"));

        Console.info("Finished loading commands");

        Console.info("Loading event listeners..");

        getProxy().getPluginManager().registerListener(this, new PlayerJoinListener());

        Console.info("Finished loading event listeners");

        Console.info("Updating player names..");

        for (ProxiedPlayer player : getProxy().getPlayers()) {
            Util.updatePlayerName(player);
        }

        Console.info("Finished updating player names");

        Console.info("Finished enabling");
    }

    @Override
    public void onDisable() {
        Console.info("Closing data..");

        this.players.close();

        Console.info("Finished closing data");

        Console.info("Removing commands..");

        getProxy().getPluginManager().unregisterCommands(this);

        Console.info("Finished removing commands");

        Console.info("Removing event listeners..");

        getProxy().getPluginManager().unregisterListeners(Main.Instance);

        Console.info("Finished removing event listeners");

        Console.info("Reseting player names..");

        for (ProxiedPlayer player : getProxy().getPlayers()) {
            Util.resetPlayerName(player);
        }

        Console.info("Finished reseting player names");

        Console.info("Finished disabling");
    }

    public void reload() {
        Console.info("Reloading..");

        Console.info("Disabling..");

        this.onDisable();

        Console.info("Enabling..");

        this.onEnable();

        Console.info("Finished reloading");
    }
}