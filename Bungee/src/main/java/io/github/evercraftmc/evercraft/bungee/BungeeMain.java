package io.github.evercraftmc.evercraft.bungee;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import io.github.evercraftmc.evercraft.bungee.broadcast.Broadcaster;
import io.github.evercraftmc.evercraft.bungee.commands.economy.BalanceCommand;
import io.github.evercraftmc.evercraft.bungee.commands.economy.EconomyCommand;
import io.github.evercraftmc.evercraft.bungee.commands.info.InfoCommand;
import io.github.evercraftmc.evercraft.bungee.commands.moderation.KickCommand;
import io.github.evercraftmc.evercraft.bungee.commands.moderation.MaintenanceCommand;
import io.github.evercraftmc.evercraft.bungee.commands.moderation.PermBanCommand;
import io.github.evercraftmc.evercraft.bungee.commands.moderation.PermMuteCommand;
import io.github.evercraftmc.evercraft.bungee.commands.moderation.TempBanCommand;
import io.github.evercraftmc.evercraft.bungee.commands.moderation.TempMuteCommand;
import io.github.evercraftmc.evercraft.bungee.commands.moderation.UnBanCommand;
import io.github.evercraftmc.evercraft.bungee.commands.moderation.UnMuteCommand;
import io.github.evercraftmc.evercraft.bungee.commands.player.NickNameCommand;
import io.github.evercraftmc.evercraft.bungee.commands.player.SpigotCommandCommand;
import io.github.evercraftmc.evercraft.bungee.commands.staff.CommandSpyCommand;
import io.github.evercraftmc.evercraft.bungee.commands.staff.StaffChatCommand;
import io.github.evercraftmc.evercraft.bungee.commands.staff.SudoCommand;
import io.github.evercraftmc.evercraft.bungee.commands.warp.HubCommand;
import io.github.evercraftmc.evercraft.bungee.commands.warp.ServerCommand;
import io.github.evercraftmc.evercraft.bungee.listeners.BungeeListener;
import io.github.evercraftmc.evercraft.bungee.listeners.MessageListener;
import io.github.evercraftmc.evercraft.bungee.listeners.PingListener;
import io.github.evercraftmc.evercraft.bungee.listeners.VoteListener;
import io.github.evercraftmc.evercraft.bungee.listeners.JoinListener;
import io.github.evercraftmc.evercraft.bungee.scoreboard.ScoreBoard;
import io.github.evercraftmc.evercraft.bungee.util.formatting.ComponentFormatter;
import io.github.evercraftmc.evercraft.shared.PluginCommand;
import io.github.evercraftmc.evercraft.shared.config.FileConfig;
import io.github.evercraftmc.evercraft.shared.config.MySQLConfig;
import io.github.evercraftmc.evercraft.shared.discord.DiscordBot;
import io.github.evercraftmc.evercraft.shared.economy.Economy;
import io.github.evercraftmc.evercraft.shared.util.Closable;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Activity.ActivityType;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeMain extends Plugin implements io.github.evercraftmc.evercraft.shared.Plugin {
    private static BungeeMain Instance;

    private FileConfig config;
    private FileConfig messages;
    private MySQLConfig data;

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

        this.config = new FileConfig(this.getDataFolder().getAbsolutePath() + File.separator + "config.json");
        this.config.reload();

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

        this.config.copyDefaults();

        this.getLogger().info("Finished loading config");

        this.getLogger().info("Loading messages..");

        this.messages = new FileConfig(this.getDataFolder().getAbsolutePath() + File.separator + "messages.json");
        this.messages.reload();

        this.messages.addDefault("error.noPerms", "&cYou need the permission {permission} to do that");
        this.messages.addDefault("error.noConsole", "&cYou can't do that from the console");
        this.messages.addDefault("error.playerNotFound", "&cCouldn't find player {player}");
        this.messages.addDefault("error.invalidArgs", "&cInvalid arguments");
        this.messages.addDefault("globalMessage", "&f[{server}] &r{message}");
        this.messages.addDefault("chat.default", "&f{player} &r&f> {message}");
        this.messages.addDefault("chat.staff", "&d&l[Staffchat] &r&f{player} &r&f> {message}");
        this.messages.addDefault("chat.discord", "&b&l[Discord] &r&f{player} &r&f> {message}");
        this.messages.addDefault("chat.commandSpy", "&d&l[Commandspy] &r&f{player} &r&fran {message}");
        this.messages.addDefault("info.about", "&f");
        this.messages.addDefault("info.discord", "&f");
        this.messages.addDefault("info.vote", "&f");
        this.messages.addDefault("info.staff", "&f");
        this.messages.addDefault("welcome.firstJoin", "&b&lWelcome {player} &r&l&bto the server!");
        this.messages.addDefault("welcome.join", "&e{player} &r&ejoined the server");
        this.messages.addDefault("welcome.move", "&c{player} &r&ehas moved to {server}");
        this.messages.addDefault("welcome.quit", "&e{player} &r&eleft the server");
        this.messages.addDefault("nickname", "&aSuccessfully changed your nickname to {nickname}");
        this.messages.addDefault("economy.yourBalance", "&aYou balance is currently {balance}");
        this.messages.addDefault("economy.otherBalance", "&a{player}'s balance is currently {balance}");
        this.messages.addDefault("economy.economy", "&aSuccessfully set {player}&r&a's balance to {balance}");
        this.messages.addDefault("vote", "&aThanks so much for voting {player}&r&a! /vote");
        this.messages.addDefault("warp.hub", "&aSuccessfully went to the hub");
        this.messages.addDefault("warp.server", "&aSuccessfully went to {server}");
        this.messages.addDefault("warp.alreadyConnected", "&cYou are already in the hub");
        this.messages.addDefault("moderation.kick.noreason", "&cYou where kicked by {moderator}");
        this.messages.addDefault("moderation.kick.reason", "&cYou where kicked by {moderator} &r&cfor {reason}");
        this.messages.addDefault("moderation.kick.brodcast.noreason", "&a{player}&r&a was kicked by {moderator}");
        this.messages.addDefault("moderation.kick.brodcast.reason", "&a{player}&r&a was kicked by {moderator} &r&afor {reason}");
        this.messages.addDefault("moderation.ban.noreason", "&cYou where banned by {moderator}");
        this.messages.addDefault("moderation.ban.reason", "&cYou where banned by {moderator} &r&cfor {time} {reason}");
        this.messages.addDefault("moderation.ban.brodcast.noreason", "&a{player}&r&a was banned by {moderator}");
        this.messages.addDefault("moderation.ban.brodcast.reason", "&a{player}&r&a was banned by {moderator} &r&afor {reason}");
        this.messages.addDefault("moderation.ban.alreadybanned", "&c{player}&r&c is already banned");
        this.messages.addDefault("moderation.unban.noreason", "&cYou where unbanned by {moderator}");
        this.messages.addDefault("moderation.unban.reason", "&cYou where unbanned by {moderator} &r&cfor {reason}");
        this.messages.addDefault("moderation.unban.brodcast.noreason", "&a{player} was unbanned by {moderator}");
        this.messages.addDefault("moderation.unban.brodcast.reason", "&a{player} was unbanned by {moderator} &r&afor {reason}");
        this.messages.addDefault("moderation.unban.notbanned", "&c{player}&r&c is not banned");
        this.messages.addDefault("moderation.mute.noreason", "&cYou where muted by {moderator}");
        this.messages.addDefault("moderation.mute.reason", "&cYou where muted by {moderator} &r&cfor {time} {reason}");
        this.messages.addDefault("moderation.mute.brodcast.noreason", "&a{player} was muted by {moderator} for {time}");
        this.messages.addDefault("moderation.mute.brodcast.reason", "&a{player} was muted by {moderator} &r&afor {time} {reason}");
        this.messages.addDefault("moderation.mute.alreadymuted", "&c{player}&r&c is already muted");
        this.messages.addDefault("moderation.unmute.noreason", "&cYou where unmuted by {moderator}");
        this.messages.addDefault("moderation.unmute.reason", "&cYou where unmuted by {moderator} &r&cfor {reason}");
        this.messages.addDefault("moderation.unmute.brodcast.noreason", "&a{player} was unmuted by {moderator}");
        this.messages.addDefault("moderation.unmute.brodcast.reason", "&a{player} was unmuted by {moderator} &r&afor {reason}");
        this.messages.addDefault("moderation.unmute.notmuted", "&c{player}&r&c is not muted");
        this.messages.addDefault("moderation.maintenance.toggle", "&aSuccessfully toggled maintenance mode {value}");
        this.messages.addDefault("moderation.maintenance.kick", "&cSorry but the server is currently in maintenance mode, please come back later");
        this.messages.addDefault("moderation.maintenance.motd", "              &cCurrently under maintenance");
        this.messages.addDefault("moderation.chat.warning1", "&c&lHey don't say that :(");
        this.messages.addDefault("moderation.chat.warning2", "&c&lSeriously don't say that");
        this.messages.addDefault("moderation.chat.warning3", "&c&lThis is your last warning, do not say that");
        this.messages.addDefault("commandspy", "&aSuccessfully toggled your commandspy {value}");
        this.messages.addDefault("sudo.message", "&aSuccessfully said {message} as {player}");
        this.messages.addDefault("sudo.command", "&aSuccessfully ran {command} as {player}");

        this.messages.copyDefaults();

        this.getLogger().info("Finished loading messages");

        this.getLogger().info("Loading player data..");

        this.data = new MySQLConfig(this.config.getString("database.host"), this.config.getInteger("database.port"), this.config.getString("database.name"), "data", this.config.getString("database.username"), this.config.getString("database.password"));

        this.getLogger().info("Finished loading player data");

        this.getLogger().info("Loading economy..");

        this.economy = new Economy(this.getData());

        this.getLogger().info("Finished loading economy");

        this.getLogger().info("Loading commands..");

        this.commands = new ArrayList<PluginCommand>();

        this.commands.add(new InfoCommand("about", "Get the server about", Arrays.asList("info"), "evercraft.commands.info.about").register());
        this.commands.add(new InfoCommand("rules", "Get the server rules", Arrays.asList(), "evercraft.commands.info.rules").register());
        this.commands.add(new InfoCommand("discord", "Get the server discord link", Arrays.asList("community"), "evercraft.commands.info.discord").register());
        this.commands.add(new InfoCommand("vote", "Get the server vote link", Arrays.asList(), "evercraft.commands.info.vote").register());
        this.commands.add(new InfoCommand("staff", "Get the server staff", Arrays.asList(), "evercraft.commands.info.staff").register());

        this.commands.add(new NickNameCommand("nickname", "Change your nickname", Arrays.asList("nick"), "evercraft.commands.player.nickname").register());

        this.commands.add(new BalanceCommand("balance", "Check your balance", Arrays.asList("bal"), "evercraft.commands.economy.balance").register());
        this.commands.add(new EconomyCommand("economy", "Modify someones balance", Arrays.asList("eco"), "evercraft.commands.economy.economy").register());

        this.commands.add(new HubCommand("hub", "Go to the hub", Arrays.asList("lobby"), "evercraft.commands.warp.hub").register());

        for (String server : this.getProxy().getServersCopy().keySet()) {
            this.commands.add(new ServerCommand(server, "Go to the " + server + " server", Arrays.asList(), "evercraft.commands.warp.server").register());
        }

        this.commands.add(new KickCommand("kick", "Kick a player from the server", Arrays.asList(), "evercraft.commands.moderation.kick").register());
        this.commands.add(new TempBanCommand("tempban", "Temporarily ban a player from the server", Arrays.asList("ban"), "evercraft.commands.moderation.tempban").register());
        this.commands.add(new PermBanCommand("permban", "Ban a player from the server", Arrays.asList(), "evercraft.commands.moderation.permban").register());
        this.commands.add(new UnBanCommand("unban", "Unban a player from the server", Arrays.asList(), "evercraft.commands.moderation.unban").register());
        this.commands.add(new TempMuteCommand("tempmute", "Temporarily mute a player on the server", Arrays.asList("mute"), "evercraft.commands.moderation.tempmute").register());
        this.commands.add(new PermMuteCommand("permmute", "Mute a player on the server", Arrays.asList(), "evercraft.commands.moderation.permmute").register());
        this.commands.add(new UnMuteCommand("unmute", "Unmute a player on the server", Arrays.asList(), "evercraft.commands.moderation.unmute").register());
        this.commands.add(new MaintenanceCommand("maintenance", "Toggle maintainance mode", Arrays.asList(), "evercraft.commands.moderation.maintenance").register());

        this.commands.add(new StaffChatCommand("staffchat", "Send a message to the staffchat", Arrays.asList("sc"), "evercraft.commands.staff.staffchat").register());
        this.commands.add(new CommandSpyCommand("commandspy", "Toggle your commandspy", Arrays.asList("cs"), "evercraft.commands.staff.commandspy").register());

        this.commands.add(new SpigotCommandCommand("spigotcommand", "Run a command on the spigot server", Arrays.asList(), null).register());
        this.commands.add(new SudoCommand("sudo", "Run a command or send a message as another player", Arrays.asList(), "evercraft.commands.staff.sudo").register());

        this.getLogger().info("Finished loading commands");

        this.getLogger().info("Loading listeners..");

        this.listeners = new ArrayList<BungeeListener>();

        this.listeners.add(new JoinListener().register());
        this.listeners.add(new PingListener().register());

        this.listeners.add(new MessageListener().register());

        this.listeners.add(new VoteListener().register());

        this.getLogger().info("Finished loading listeners");

        this.getLogger().info("Loading other assets..");

        this.assets = new ArrayList<Closable>();

        this.assets.add(new ScoreBoard());
        this.assets.add(new Broadcaster());

        this.getLogger().info("Finished loading other assets..");

        this.getLogger().info("Finished loading plugin");

        this.getLogger().info("Starting Discord bot..");

        this.bot = new DiscordBot(this.config.getString("discord.token"), this.config.getString("discord.guildID"), ActivityType.valueOf(this.config.getString("discord.statusType")), this.config.getString("discord.status"));
        this.bot.setMessageCallback((Message message) -> {
            if (message.getChannel().getId().equals(this.getPluginConfig().getString("discord.channelId"))) {
                for (ProxiedPlayer player : this.getProxy().getPlayers()) {
                    player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(this.getPluginMessages().getString("chat.discord").replace("{player}", message.getMember().getEffectiveName()).replace("{message}", message.getContentDisplay()))));
                }
            } else if (message.getChannel().getId().equals(this.getPluginConfig().getString("discord.staffChannelId"))) {
                for (ProxiedPlayer player : this.getProxy().getPlayers()) {
                    if (player.hasPermission("evercraft.commands.staff.staffchat")) {
                        player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("chat.staff").replace("{player}", message.getMember().getEffectiveName()).replace("{message}", message.getContentDisplay()))));
                    }
                }
            }
        });
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

        data.close();

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

    public MySQLConfig getData() {
        return this.data;
    }

    public Economy getEconomy() {
        return this.economy;
    }

    public DiscordBot getDiscordBot() {
        return this.bot;
    }
}