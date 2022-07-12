package io.github.evercraftmc.evercraft.bungee;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import io.github.evercraftmc.evercraft.bungee.broadcast.Broadcaster;
import io.github.evercraftmc.evercraft.bungee.commands.BungeeCommand;
import io.github.evercraftmc.evercraft.bungee.commands.economy.BalanceCommand;
import io.github.evercraftmc.evercraft.bungee.commands.economy.EconomyCommand;
import io.github.evercraftmc.evercraft.bungee.commands.info.InfoCommand;
import io.github.evercraftmc.evercraft.bungee.commands.moderation.KickCommand;
import io.github.evercraftmc.evercraft.bungee.commands.moderation.LockChatCommand;
import io.github.evercraftmc.evercraft.bungee.commands.moderation.MaintenanceCommand;
import io.github.evercraftmc.evercraft.bungee.commands.moderation.PermBanCommand;
import io.github.evercraftmc.evercraft.bungee.commands.moderation.PermMuteCommand;
import io.github.evercraftmc.evercraft.bungee.commands.moderation.TempBanCommand;
import io.github.evercraftmc.evercraft.bungee.commands.moderation.TempMuteCommand;
import io.github.evercraftmc.evercraft.bungee.commands.moderation.UnBanCommand;
import io.github.evercraftmc.evercraft.bungee.commands.moderation.UnMuteCommand;
import io.github.evercraftmc.evercraft.bungee.commands.player.MessageCommand;
import io.github.evercraftmc.evercraft.bungee.commands.player.NickNameCommand;
import io.github.evercraftmc.evercraft.bungee.commands.player.ReplyCommand;
import io.github.evercraftmc.evercraft.bungee.commands.player.SeenCommand;
import io.github.evercraftmc.evercraft.bungee.commands.player.SpigotCommandCommand;
import io.github.evercraftmc.evercraft.bungee.commands.staff.CommandSpyCommand;
import io.github.evercraftmc.evercraft.bungee.commands.staff.ReloadCommand;
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
import io.github.evercraftmc.evercraft.bungee.util.network.TabListUtil;
import io.github.evercraftmc.evercraft.bungee.util.player.BungeePlayerResolver;
import io.github.evercraftmc.evercraft.shared.config.FileConfig;
import io.github.evercraftmc.evercraft.shared.config.MySQLConfig;
import io.github.evercraftmc.evercraft.shared.discord.DiscordBot;
import io.github.evercraftmc.evercraft.shared.economy.Economy;
import io.github.evercraftmc.evercraft.shared.util.Closable;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import net.dv8tion.jda.api.entities.Activity.ActivityType;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeMain extends Plugin implements io.github.evercraftmc.evercraft.shared.Plugin {
    private static BungeeMain Instance;

    private FileConfig config;
    private FileConfig messages;
    private MySQLConfig data;

    private Economy economy;

    private DiscordBot bot;

    private List<BungeeCommand> commands;
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

        this.config.addDefault("server.default", "hub");
        this.config.addDefault("server.fallback", "fallback");
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
        this.config.addDefault("broadcaster.interval", 600);
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
        this.messages.addDefault("reload.reloading", "&aReloading plugin..");
        this.messages.addDefault("reload.reloaded", "&aSuccessfully reloaded");
        this.messages.addDefault("globalMessage", "&f[{server}] &r{message}");
        this.messages.addDefault("chat.default", "&f{player} &r&f> {message}");
        this.messages.addDefault("chat.dm", "&f{player1} &r&f-> {player2} &r&f> {message}");
        this.messages.addDefault("chat.noReplyTo", "&cYou do not have anyone to reply to");
        this.messages.addDefault("chat.staff", "&d&l[Staffchat] &r&f{player} &r&f> {message}");
        this.messages.addDefault("chat.discord", "&b&l[Discord] &r&f{player} &r&f> {message}");
        this.messages.addDefault("chat.commandSpy", "&d&l[Commandspy] &r&f{player} &r&fran {message}");
        this.messages.addDefault("server.disconnected-no-com", "&cThe server you where on is temporarily down, you have been placed in the fallback server.");
        this.messages.addDefault("server.disconnected-error", "&cYour connection to the server encountered an error, you have been placed in the fallback server. {error}");
        this.messages.addDefault("info.about", "&f");
        this.messages.addDefault("info.discord", "&f");
        this.messages.addDefault("info.vote", "&f");
        this.messages.addDefault("info.staff", "&f");
        this.messages.addDefault("welcome.firstJoin", "&b&lWelcome {player} &r&l&bto the server!");
        this.messages.addDefault("welcome.join", "&e{player} &r&ejoined the server");
        this.messages.addDefault("welcome.move", "&c{player} &r&ehas moved to {server}");
        this.messages.addDefault("welcome.quit", "&e{player} &r&eleft the server");
        this.messages.addDefault("nickname", "&aSuccessfully changed your nickname to {nickname}");
        this.messages.addDefault("lastSeen.lastSeen", "&a{player} was last seen {lastSeen} ago");
        this.messages.addDefault("lastSeen.online", "&a{player} is online right now!");
        this.messages.addDefault("economy.yourBalance", "&aYou balance is currently {balance}");
        this.messages.addDefault("economy.otherBalance", "&a{player}&r&a's balance is currently {balance}");
        this.messages.addDefault("economy.economy", "&aSuccessfully set {player}&r&a's balance to {balance}");
        this.messages.addDefault("vote", "&aThanks so much for voting {player}&r&a! /vote");
        this.messages.addDefault("warp.hub", "&aSuccessfully went to the hub");
        this.messages.addDefault("warp.server", "&aSuccessfully went to {server}");
        this.messages.addDefault("warp.alreadyConnected", "&cYou are already in the hub");
        this.messages.addDefault("moderation.kick.noReason", "&cYou where kicked by {moderator}");
        this.messages.addDefault("moderation.kick.reason", "&cYou where kicked by {moderator} &r&cfor {reason}");
        this.messages.addDefault("moderation.kick.broadcast.noReason", "&a{player}&r&a was kicked by {moderator}");
        this.messages.addDefault("moderation.kick.broadcast.reason", "&a{player}&r&a was kicked by {moderator} &r&afor {reason}");
        this.messages.addDefault("moderation.kick.cantKickSelf", "&cYou can't kick yourself (If you are absolutely sure you want to kick yourself add --confirm to the end of the command");
        this.messages.addDefault("moderation.ban.noReason", "&cYou where banned by {moderator}");
        this.messages.addDefault("moderation.ban.reason", "&cYou where banned by {moderator} &r&cfor {time} {reason}");
        this.messages.addDefault("moderation.ban.broadcast.noReason", "&a{player}&r&a was banned by {moderator}");
        this.messages.addDefault("moderation.ban.broadcast.reason", "&a{player}&r&a was banned by {moderator} &r&afor {reason}");
        this.messages.addDefault("moderation.ban.alreadyBanned", "&c{player}&r&c is already banned");
        this.messages.addDefault("moderation.ban.cantBanSelf", "&cYou can't ban yourself (If you are absolutely sure you want to ban yourself (You can't unban yourself) add --confirm to the end of the command");
        this.messages.addDefault("moderation.unban.noReason", "&cYou where unbanned by {moderator}");
        this.messages.addDefault("moderation.unban.reason", "&cYou where unbanned by {moderator} &r&cfor {reason}");
        this.messages.addDefault("moderation.unban.broadcast.noReason", "&a{player}&r&a was unbanned by {moderator}");
        this.messages.addDefault("moderation.unban.broadcast.reason", "&a{player}&r&a was unbanned by {moderator} &r&afor {reason}");
        this.messages.addDefault("moderation.unban.notBanned", "&c{player}&r&c is not banned");
        this.messages.addDefault("moderation.unban.cantUnbanSelf", "&cYou can't unban yourself");
        this.messages.addDefault("moderation.mute.noReason", "&cYou where muted by {moderator}");
        this.messages.addDefault("moderation.mute.reason", "&cYou where muted by {moderator} &r&cfor {time} {reason}");
        this.messages.addDefault("moderation.mute.broadcast.noReason", "&a{player}&r&a was muted by {moderator} for {time}");
        this.messages.addDefault("moderation.mute.broadcast.reason", "&a{player}&r&a was muted by {moderator} &r&afor {time} {reason}");
        this.messages.addDefault("moderation.mute.alreadyMuted", "&c{player}&r&c is already muted");
        this.messages.addDefault("moderation.mute.cantMuteSelf", "&cYou can't mute yourself (If you are absolutely sure you want to mute yourself (You can't unmute yourself) add --confirm to the end of the command");
        this.messages.addDefault("moderation.unmute.noReason", "&cYou where unmuted by {moderator}");
        this.messages.addDefault("moderation.unmute.reason", "&cYou where unmuted by {moderator} &r&cfor {reason}");
        this.messages.addDefault("moderation.unmute.broadcast.noReason", "&a{player}&r&a was unmuted by {moderator}");
        this.messages.addDefault("moderation.unmute.broadcast.reason", "&a{player}&r&a was unmuted by {moderator} &r&afor {reason}");
        this.messages.addDefault("moderation.unmute.notMuted", "&c{player}&r&c is not muted");
        this.messages.addDefault("moderation.unmute.cantUnmuteSelf", "&cYou can't unmute yourself");
        this.messages.addDefault("moderation.chatLock.toggle", "&aSuccessfully toggled chat lock mode {value}");
        this.messages.addDefault("moderation.chatLock.chat", "&cChat lock is currently enabled");
        this.messages.addDefault("moderation.maintenance.toggle", "&aSuccessfully toggled maintenance mode {value}");
        this.messages.addDefault("moderation.maintenance.kick", "&cSorry but the server is currently in maintenance mode, please come back later");
        this.messages.addDefault("moderation.maintenance.motd", "              &cCurrently under maintenance");
        this.messages.addDefault("moderation.chat.warning", "&a{player}&r&a was warned by CONSOLE for Inappropriate language");
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

        this.commands = new ArrayList<BungeeCommand>();

        this.commands.add(new InfoCommand("about", "Get the server about", Arrays.asList("info"), "evercraft.commands.info.about").register());
        this.commands.add(new InfoCommand("rules", "Get the server rules", Arrays.asList(), "evercraft.commands.info.rules").register());
        this.commands.add(new InfoCommand("discord", "Get the server discord link", Arrays.asList("community"), "evercraft.commands.info.discord").register());
        this.commands.add(new InfoCommand("vote", "Get the server vote link", Arrays.asList(), "evercraft.commands.info.vote").register());
        this.commands.add(new InfoCommand("staff", "Get the server staff", Arrays.asList(), "evercraft.commands.info.staff").register());

        this.commands.add(new NickNameCommand("nickname", "Change your nickname", Arrays.asList("nick"), "evercraft.commands.player.nickname").register());

        this.commands.add(new SeenCommand("lastSeen", "Check when someone was last online", Arrays.asList("seen"), "evercraft.commands.player.seen").register());

        this.commands.add(new MessageCommand("message", "Message someone", Arrays.asList("msg", "tell"), "evercraft.commands.player.message").register());
        this.commands.add(new ReplyCommand("reply", "Reply to someone", Arrays.asList("r"), "evercraft.commands.player.message").register());

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
        this.commands.add(new LockChatCommand("lockchat", "Toggle chat lock", Arrays.asList("lockchat"), "evercraft.commands.moderation.chatlock").register());
        this.commands.add(new MaintenanceCommand("maintenance", "Toggle maintenance mode", Arrays.asList(), "evercraft.commands.moderation.maintenance").register());

        this.commands.add(new StaffChatCommand("staffchat", "Send a message to the staffchat", Arrays.asList("sc"), "evercraft.commands.staff.staffchat").register());
        this.commands.add(new CommandSpyCommand("commandspy", "Toggle your commandspy", Arrays.asList("cs"), "evercraft.commands.staff.commandspy").register());

        this.commands.add(new SpigotCommandCommand("spigotcommand", "Run a command on the spigot server", Arrays.asList(), null).register());
        this.commands.add(new SudoCommand("sudo", "Run a command or send a message as another player", Arrays.asList(), "evercraft.commands.staff.sudo").register());

        this.commands.add(new ReloadCommand("evercraftbungeereload", "Reload the plugin", Arrays.asList("ecbreload"), "evercraft.commands.staff.reload").register());

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

        for (ProxiedPlayer player : this.getProxy().getPlayers()) {
            player.setDisplayName(TextFormatter.translateColors(BungeePlayerResolver.getDisplayName(data, player.getUniqueId())));
            TabListUtil.updatePlayerName(player);
        }

        this.getLogger().info("Finished loading other assets..");

        this.getLogger().info("Finished loading plugin");

        this.getLogger().info("Starting Discord bot..");

        this.bot = new DiscordBot(this.config.getString("discord.token"), this.config.getString("discord.guildID"), new GatewayIntent[] { GatewayIntent.GUILD_MESSAGES }, new CacheFlag[] {}, MemberCachePolicy.NONE, ActivityType.valueOf(this.config.getString("discord.statusType")), this.config.getString("discord.status"));
        this.bot.addListener((GenericEvent rawevent) -> {
            if (rawevent instanceof MessageReceivedEvent event) {
                if (event.getAuthor() != this.bot.getJDA().getSelfUser()) {
                    if (event.getMessage().getChannel().getId().equals(this.getPluginConfig().getString("discord.channelId"))) {
                        for (ProxiedPlayer player : this.getProxy().getPlayers()) {
                            player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(this.getPluginMessages().getString("chat.discord").replace("{player}", event.getMessage().getMember().getEffectiveName()).replace("{message}", event.getMessage().getContentDisplay()))));
                        }
                    } else if (event.getMessage().getChannel().getId().equals(this.getPluginConfig().getString("discord.staffChannelId"))) {
                        for (ProxiedPlayer player : this.getProxy().getPlayers()) {
                            if (player.hasPermission("evercraft.commands.staff.staffchat")) {
                                player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(BungeeMain.getInstance().getPluginMessages().getString("chat.staff").replace("{player}", event.getMessage().getMember().getEffectiveName()).replace("{message}", event.getMessage().getContentDisplay()))));
                            }
                        }
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

        for (BungeeCommand command : this.commands) {
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

        for (ProxiedPlayer player : this.getProxy().getPlayers()) {
            player.setDisplayName(null);
            TabListUtil.updatePlayerName(player);
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

    public List<BungeeCommand> getCommands() {
        return this.commands;
    }

    public List<BungeeListener> getListeners() {
        return this.listeners;
    }

    public List<Closable> getAssets() {
        return this.assets;
    }

    public DiscordBot getDiscordBot() {
        return this.bot;
    }
}