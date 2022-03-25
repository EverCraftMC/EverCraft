package com.kale_ko.evercraft.bungee;

import com.kale_ko.evercraft.bungee.commands.economy.BalanceCommand;
import com.kale_ko.evercraft.bungee.commands.economy.EconomyCommand;
import com.kale_ko.evercraft.bungee.commands.player.NickNameCommand;
import com.kale_ko.evercraft.bungee.commands.staff.StaffChatCommand;
import com.kale_ko.evercraft.bungee.listeners.MessageListener;
import com.kale_ko.evercraft.shared.config.FileConfig;
import com.kale_ko.evercraft.shared.config.MySQLConfig;
import com.kale_ko.evercraft.shared.discord.DiscordBot;
import com.kale_ko.evercraft.shared.economy.Economy;
import net.dv8tion.jda.api.entities.Activity.ActivityType;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeMain extends Plugin implements com.kale_ko.evercraft.shared.Plugin {
    private static BungeeMain Instance;

    private FileConfig config;
    private FileConfig messages;
    private MySQLConfig players;

    private Economy economy;

    private LuckPerms luckPerms;

    private DiscordBot bot;

    @Override
    public void onLoad() {
        BungeeMain.Instance = this;
    }

    @Override
    public void onEnable() {
        this.getLogger().info("Loading plugin..");

        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdir();
        }

        this.getLogger().info("Loading config..");

        this.config = new FileConfig(this.getDataFolder().getAbsolutePath() + "config.json");

        this.config.addDefault("database.host", "localhost");
        this.config.addDefault("database.port", 3306);
        this.config.addDefault("database.name", "evercraft");
        this.config.addDefault("database.username", "root");
        this.config.addDefault("database.password", "");
        this.config.addDefault("discord.token", "");
        this.config.addDefault("discord.guildID", "");
        this.config.addDefault("discord.statusType", "PLAYING");
        this.config.addDefault("discord.status", "");

        this.getLogger().info("Finished loading config");

        this.getLogger().info("Loading messages..");

        this.messages = new FileConfig("messages.json");

        this.messages.addDefault("error.noPerms", "&cYou need the permission {permission} to do that");
        this.messages.addDefault("error.playerNotFound", "&cCouldn't find player {player}");
        this.messages.addDefault("error.invalidArgs", "&cInvalid arguments");
        this.messages.addDefault("economy.balance", "&aYou balance is currently {balance}");
        this.messages.addDefault("economy.economy", "&aSuccessfully set {player}'s balance to {balance}");

        this.getLogger().info("Finished loading messages");

        this.getLogger().info("Loading player data..");

        this.players = new MySQLConfig(this.config.getString("database.host"), this.config.getInt("database.port"), this.config.getString("database.name"), "players", this.config.getString("database.username"), this.config.getString("database.password"));

        this.getLogger().info("Finished loading player data");

        this.getLogger().info("Loading economy..");

        this.economy = new Economy(this.getPlayerData());

        this.getLogger().info("Finished loading economy");

        this.getLogger().info("Loading LuckPerms integration..");

        this.luckPerms = LuckPermsProvider.get();

        this.getLogger().info("Finished loading LuckPerms integration");

        this.getLogger().info("Loading commands..");

        new NickNameCommand().register();

        new BalanceCommand().register();
        new EconomyCommand().register();

        new StaffChatCommand().register();

        this.getLogger().info("Finished loading commands");

        this.getLogger().info("Loading listeners..");

        this.getProxy().getPluginManager().registerListener(this, new MessageListener());

        this.getLogger().info("Finished loading listeners");

        this.getLogger().info("Finished loading plugin");

        this.getLogger().info("Starting Discord bot..");

        this.bot = new DiscordBot(this.config.getString("discord.token"), this.config.getString("discord.guildID"), ActivityType.valueOf(this.config.getString("discord.statusType")), this.config.getString("discord.status"));
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Disabling plugin..");

        this.getLogger().info("Closing config..");

        config.close();

        this.getLogger().info("Finished closing config..");

        this.getLogger().info("Closing messages..");

        messages.close();

        this.getLogger().info("Finished closing messages..");

        this.getLogger().info("Closing player data..");

        players.close();

        this.getLogger().info("Finished closing player data..");

        this.getLogger().info("Unloading listeners..");

        this.getProxy().getPluginManager().unregisterListeners(this);

        this.getLogger().info("Finished unloading listeners");

        this.getLogger().info("Finished disabling plugin");
    }

    @Override
    public void reload() {
        this.getLogger().info("Reloading plugin..");

        this.onDisable();

        this.onEnable();

        this.getLogger().info("Finished reloading plugin");
    }

    public static BungeeMain getInstance() {
        return BungeeMain.Instance;
    }

    public FileConfig getPluginConfig() {
        return this.config;
    }

    public FileConfig getPluginMessages() {
        return this.messages;
    }

    public MySQLConfig getPlayerData() {
        return this.players;
    }

    public Economy getEconomy() {
        return this.economy;
    }

    public LuckPerms getLuckPerms() {
        return this.luckPerms;
    }

    public DiscordBot getDiscordBot() {
        return this.bot;
    }
}