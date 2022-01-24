package com.kale_ko.kalesutilities.bungee;

import java.util.logging.Logger;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

public class Main extends Plugin {
    public static Main Instance;

    public final Logger Console = getLogger();

    public BungeeConfig config;
    public BungeeConfig players;

    @Override
    public void onEnable() {
        Main.Instance = this;

        Console.info("Loading config..");

        config = BungeeConfig.load("config.yml");
        players = BungeeConfig.load("players.yml");

        Console.info("Finished loading config");

        Console.info("Loading commands..");

        Console.info("Finished loading commands");

        Console.info("Loading event listeners..");

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