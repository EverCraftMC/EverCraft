package com.kale_ko.evercraft.bungee;

import java.util.Arrays;
import java.util.List;
import com.kale_ko.evercraft.bungee.broadcast.Broadcaster;
import com.kale_ko.evercraft.bungee.commands.economy.BalanceCommand;
import com.kale_ko.evercraft.bungee.commands.economy.EconomyCommand;
import com.kale_ko.evercraft.bungee.commands.info.AboutCommand;
import com.kale_ko.evercraft.bungee.commands.info.DiscordCommand;
import com.kale_ko.evercraft.bungee.commands.info.StaffCommand;
import com.kale_ko.evercraft.bungee.commands.info.VoteCommand;
import com.kale_ko.evercraft.bungee.commands.moderation.KickCommand;
import com.kale_ko.evercraft.bungee.commands.moderation.PermBanCommand;
import com.kale_ko.evercraft.bungee.commands.moderation.PermMuteCommand;
import com.kale_ko.evercraft.bungee.commands.player.NickNameCommand;
import com.kale_ko.evercraft.bungee.commands.staff.CommandSpyCommand;
import com.kale_ko.evercraft.bungee.commands.staff.StaffChatCommand;
import com.kale_ko.evercraft.bungee.commands.warp.HubCommand;
import com.kale_ko.evercraft.bungee.commands.warp.ServerCommand;
import com.kale_ko.evercraft.bungee.listeners.BungeeListener;
import com.kale_ko.evercraft.bungee.listeners.MessageListener;
import com.kale_ko.evercraft.bungee.listeners.PingListener;
import com.kale_ko.evercraft.bungee.listeners.VoteListener;
import com.kale_ko.evercraft.bungee.listeners.JoinListener;
import com.kale_ko.evercraft.bungee.scoreboard.ScoreBoard;
import com.kale_ko.evercraft.shared.PluginCommand;
import com.kale_ko.evercraft.shared.config.FileConfig;
import com.kale_ko.evercraft.shared.config.MySQLConfig;
import com.kale_ko.evercraft.shared.discord.DiscordBot;
import com.kale_ko.evercraft.shared.economy.Economy;
import com.kale_ko.evercraft.shared.util.Closable;
import net.dv8tion.jda.api.entities.Activity.ActivityType;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeMain extends Plugin implements com.kale_ko.evercraft.shared.Plugin {
    private static BungeeMain Instance;

    private FileConfig config;
    private FileConfig messages;
    private MySQLConfig players;

    private Economy economy;

    private DiscordBot bot;

    private List<PluginCommand> commands;
    private List<BungeeListener> listeners;
    private List<Closable> assets;

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
        this.config.addDefault("discord.channelId", "");
        this.config.addDefault("discord.staffChannelId", "");
        this.config.addDefault("discord.statusType", "PLAYING");
        this.config.addDefault("discord.status", "on play.evercraft.ga");
        this.config.addDefault("scoreboard.title", "&3&lEverCraft");
        this.config.addDefault("scoreboard.lines", Arrays.asList());
        this.config.addDefault("tablist.header", "");
        this.config.addDefault("tablist.footer", "");
        this.config.addDefault("broadcaster.intervail", 600);
        this.config.addDefault("broadcaster.messages", Arrays.asList());

        this.getLogger().info("Finished loading config");

        this.getLogger().info("Loading messages..");

        this.messages = new FileConfig("messages.json");

        this.messages.addDefault("error.noPerms", "&cYou need the permission {permission} to do that");
        this.messages.addDefault("error.playerNotFound", "&cCouldn't find player {player}");
        this.messages.addDefault("error.invalidArgs", "&cInvalid arguments");
        this.messages.addDefault("globalMessage", "&f[{server}] &r{message}");
        this.messages.addDefault("chat.default", "&f{player} &f> {message}");
        this.messages.addDefault("chat.staff", "&6&l[Staffchat] &r&f{player} &f> {message}");
        this.messages.addDefault("chat.discord", "&b&l[Discord] &r&f{player} &f> {message}");
        this.messages.addDefault("info.about", "&f");
        this.messages.addDefault("info.discord", "&f");
        this.messages.addDefault("info.vote", "&f");
        this.messages.addDefault("info.staff", "&f");
        this.messages.addDefault("welcome.firstJoin", "&fWelcome {player} to the server!");
        this.messages.addDefault("welcome.join", "&f{player} joined the server");
        this.messages.addDefault("welcome.quit", "&f{player} left the server");
        this.messages.addDefault("nickname", "&aSuccessfully changed your nickname to {nickname}");
        this.messages.addDefault("economy.yourBalance", "&aYou balance is currently {balance}");
        this.messages.addDefault("economy.otherBalance", "&a{player}'s balance is currently {balance}");
        this.messages.addDefault("economy.economy", "&aSuccessfully set {player}'s balance to {balance}");
        this.messages.addDefault("vote", "&aThanks so much for voting {player}! /vote");
        this.messages.addDefault("warp.hub", "&aSuccessfully went to the hub");
        this.messages.addDefault("warp.alreadyConnected", "&cYou are already in the hub");
        this.messages.addDefault("moderation.kick.noreason", "&cYou where kicked by {moderator}");
        this.messages.addDefault("moderation.kick.reason", "&cYou where kicked by {moderator} &r&cfor {reason}");
        this.messages.addDefault("moderation.kick.brodcast.noreason", "&f{player} was kicked by {moderator}");
        this.messages.addDefault("moderation.kick.brodcast.reason", "&f{player} was kicked by {moderator} &r&cfor {reason}");
        this.messages.addDefault("moderation.ban.noreason", "&cYou where banned by {moderator}");
        this.messages.addDefault("moderation.ban.reason", "&cYou where banned by {moderator} &r&cfor {time} {reason}");
        this.messages.addDefault("moderation.ban.brodcast.noreason", "&f{player} was banned by {moderator} for {time}");
        this.messages.addDefault("moderation.ban.brodcast.reason", "&f{player} was banned by {moderator} &r&cfor {time} {reason}");
        this.messages.addDefault("moderation.mute.noreason", "&cYou where muted by {moderator}");
        this.messages.addDefault("moderation.mute.reason", "&cYou where muted by {moderator} &r&cfor {time} {reason}");
        this.messages.addDefault("moderation.mute.brodcast.noreason", "&f{player} was muted by {moderator} for {time}");
        this.messages.addDefault("moderation.mute.brodcast.reason", "&f{player} was muted by {moderator} &r&cfor {time} {reason}");
        this.messages.addDefault("moderation.maintenance.toggle", "&aSuccessfully toggled maintenance mode {value}");
        this.messages.addDefault("moderation.maintenance.kick", "&cSorry but maintenance mode is currently enable, please come back later");
        this.messages.addDefault("commandspy", "&cSuccessfully toggled your commandspy {value}");

        this.getLogger().info("Finished loading messages");

        this.getLogger().info("Loading player data..");

        this.players = new MySQLConfig(this.config.getString("database.host"), this.config.getInt("database.port"), this.config.getString("database.name"), "players", this.config.getString("database.username"), this.config.getString("database.password"));

        this.getLogger().info("Finished loading player data");

        this.getLogger().info("Loading economy..");

        this.economy = new Economy(this.getPlayerData());

        this.getLogger().info("Finished loading economy");

        this.getLogger().info("Loading commands..");

        this.commands.add(new AboutCommand().register());
        this.commands.add(new VoteCommand().register());
        this.commands.add(new DiscordCommand().register());
        this.commands.add(new StaffCommand().register());

        this.commands.add(new NickNameCommand().register());

        this.commands.add(new BalanceCommand().register());
        this.commands.add(new EconomyCommand().register());

        this.commands.add(new HubCommand().register());

        for (String server : this.getProxy().getServers().keySet()) {
            this.commands.add(new ServerCommand(server).register());
        }

        this.commands.add(new KickCommand().register());
        this.commands.add(new PermBanCommand().register());
        this.commands.add(new PermMuteCommand().register());

        this.commands.add(new StaffChatCommand().register());
        this.commands.add(new CommandSpyCommand().register());

        this.getLogger().info("Finished loading commands");

        this.getLogger().info("Loading listeners..");

        this.listeners.add(new JoinListener().register());
        this.listeners.add(new PingListener().register());

        this.listeners.add(new MessageListener().register());

        this.listeners.add(new VoteListener().register());

        this.getLogger().info("Finished loading listeners");

        this.getLogger().info("Loading other assets..");

        this.assets.add(new ScoreBoard());
        this.assets.add(new Broadcaster());

        this.getLogger().info("Finished loading other assets..");

        this.getLogger().info("Finished loading plugin");

        this.getLogger().info("Starting Discord bot..");

        this.bot = new DiscordBot(this.config.getString("discord.token"), this.config.getString("discord.guildID"), ActivityType.valueOf(this.config.getString("discord.statusType")), this.config.getString("discord.status"));
        this.assets.add(this.bot);
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

        this.getLogger().info("Unregistering commands..");

        for (PluginCommand command : this.commands) {
            command.unregister();
        }

        this.getLogger().info("Finished unregistering commands..");

        this.getLogger().info("Unregistering listeners..");

        for (BungeeListener listener : this.listeners) {
            listener.unregister();
        }

        this.getLogger().info("Finished unregistering listeners..");

        this.getLogger().info("Closing assets..");

        for (Closable asset : this.assets) {
            asset.close();
        }

        this.getLogger().info("Finished closing assets..");

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

    public DiscordBot getDiscordBot() {
        return this.bot;
    }
}