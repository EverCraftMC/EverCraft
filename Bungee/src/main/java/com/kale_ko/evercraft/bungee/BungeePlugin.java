package com.kale_ko.evercraft.bungee;

import com.kale_ko.evercraft.bungee.broadcast.Broadcast;
import com.kale_ko.evercraft.bungee.commands.info.EverCraftBungeeCommand;
import com.kale_ko.evercraft.bungee.commands.server.HubCommand;
import com.kale_ko.evercraft.bungee.listeners.GlobalMesageListener;
import com.kale_ko.evercraft.bungee.listeners.PlayerJoinListener;
import com.kale_ko.evercraft.bungee.listeners.WelcomeListener;
import com.kale_ko.evercraft.shared.discord.DiscordBot;
import com.kale_ko.evercraft.shared.mysql.MySQLConfig;
import com.kale_ko.evercraft.shared.util.ParamRunnable;
import java.util.Arrays;
import java.util.logging.Logger;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BungeePlugin extends net.md_5.bungee.api.plugin.Plugin implements com.kale_ko.evercraft.shared.Plugin {
    public static BungeePlugin Instance;

    public final Logger Console = getLogger();

    public BungeeConfig config;
    public MySQLConfig players;

    public LuckPerms luckperms;

    private Broadcast broadcast;

    public DiscordBot bot;

    @Override
    public void onEnable() {
        BungeePlugin.Instance = this;

        Console.info("Loading config..");

        config = BungeeConfig.load("config.yml");

        config.addDefault("config.prefix", "&6&l[EverCraft]&r");
        config.addDefault("config.serverName", "");
        config.addDefault("config.mainServer", "hub");
        config.addDefault("config.broadcastIntervial", 900);
        config.addDefault("config.broadcastMessages", Arrays.asList("&3&lMake sure to join our Discord with /discord!"));
        config.addDefault("database.url", "localhost");
        config.addDefault("database.port", "3306");
        config.addDefault("database.database", "minecraft");
        config.addDefault("database.username", "root");
        config.addDefault("database.password", "");
        config.addDefault("database.tablePrefix", "kalesutilities_");
        config.addDefault("discord.token", "");
        config.addDefault("discord.serverID", "");
        config.addDefault("discord.channelID", "");
        config.addDefault("messages.invalidCommand", "{command} is not a command");
        config.addDefault("messages.noperms", "You need the permission {permission} to run that command");
        config.addDefault("messages.noconsole", "You can't use that command from the console");
        config.addDefault("messages.allreadyconnected", "You are already connected to that server");
        config.addDefault("messages.playernotfound", "{player} can't be found");
        config.addDefault("messages.usage", "Usage: {usage}");
        config.addDefault("messages.joinMessage", "&e{player} &ehas joined the server!");
        config.addDefault("messages.moveMessage", "&e{player} &ehas moved to {server}");
        config.addDefault("messages.quitMessage", "&e{player} &ehas left the server");
        config.addDefault("messages.help", "\n{commandList}");
        config.addDefault("messages.list", "\n{playerList}");
        config.addDefault("messages.reload", "Config Reloaded");

        config.copyDefaults();

        Console.info("Finished loading config");

        Console.info("Loading data..");

        players = MySQLConfig.load(config.getString("database.url"), config.getInt("database.port"), config.getString("database.database"), config.getString("database.tablePrefix") + "players", config.getString("database.username"), config.getString("database.password"));

        Console.info("Finished loading data");

        Console.info("Loading luckperms integration..");

        luckperms = LuckPermsProvider.get();

        Console.info("Finished loading luckperms integration..");

        Console.info("Loading commands..");

        getProxy().getPluginManager().registerCommand(this, new EverCraftBungeeCommand("evercraftbungee", Arrays.asList("kub", "ksb"), "evercraft.commands.info.evercraft"));
        getProxy().getPluginManager().registerCommand(this, new HubCommand("hub", Arrays.asList("lobby"), "evercraft.commands.server.hub"));

        Console.info("Finished loading commands");

        Console.info("Loading event listeners..");

        getProxy().getPluginManager().registerListener(this, new PlayerJoinListener());
        getProxy().getPluginManager().registerListener(this, new GlobalMesageListener());
        getProxy().getPluginManager().registerListener(this, new WelcomeListener());

        Console.info("Finished loading event listeners");

        Console.info("Loading broadcast..");

        broadcast = new Broadcast(config.getLong("config.broadcastIntervial"));

        Console.info("Finished loading broadcast..");

        Console.info("Updating player names..");

        for (ProxiedPlayer player : getProxy().getPlayers()) {
            Util.updatePlayerName(player);
        }

        Console.info("Finished updating player names");

        Console.info("Finished enabling");

        Console.info("Starting Discord bot");

        bot = new DiscordBot(config.getString("discord.token"), config.getString("discord.serverID"), config.getString("discord.channelID"), new ParamRunnable() {
            @Override
            public void run() { }

            @Override
            public void init(Object... params) { }
        });
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

        getProxy().getPluginManager().unregisterListeners(BungeePlugin.Instance);

        Console.info("Finished removing event listeners");

        Console.info("Removing broadcast..");

        broadcast.close();

        Console.info("Finished removing broadcast..");

        Console.info("Reseting player names..");

        for (ProxiedPlayer player : getProxy().getPlayers()) {
            Util.resetPlayerName(player);
        }

        Console.info("Finished reseting player names");

        Console.info("Finished disabling");
    }

    @Override
    public void reload() {
        Console.info("Reloading..");

        Console.info("Disabling..");

        this.onDisable();

        Console.info("Enabling..");

        this.onEnable();

        Console.info("Finished reloading");
    }
}