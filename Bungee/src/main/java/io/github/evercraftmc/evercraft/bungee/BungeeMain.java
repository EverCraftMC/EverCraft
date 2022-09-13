package io.github.evercraftmc.evercraft.bungee;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
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
import io.github.evercraftmc.evercraft.bungee.commands.player.FriendCommand;
import io.github.evercraftmc.evercraft.bungee.commands.player.MessageCommand;
import io.github.evercraftmc.evercraft.bungee.commands.player.NickNameCommand;
import io.github.evercraftmc.evercraft.bungee.commands.player.ReplyCommand;
import io.github.evercraftmc.evercraft.bungee.commands.player.SeenCommand;
import io.github.evercraftmc.evercraft.bungee.commands.player.SettingsCommand;
import io.github.evercraftmc.evercraft.bungee.commands.player.SpigotCommandCommand;
import io.github.evercraftmc.evercraft.bungee.commands.staff.CommandSpyCommand;
import io.github.evercraftmc.evercraft.bungee.commands.staff.ImpersonateCommand;
import io.github.evercraftmc.evercraft.bungee.commands.staff.PlayerInfoCommand;
import io.github.evercraftmc.evercraft.bungee.commands.staff.ReloadCommand;
import io.github.evercraftmc.evercraft.bungee.commands.staff.StaffChatCommand;
import io.github.evercraftmc.evercraft.bungee.commands.staff.SudoCommand;
import io.github.evercraftmc.evercraft.bungee.commands.warp.HubCommand;
import io.github.evercraftmc.evercraft.bungee.commands.warp.ServerCommand;
import io.github.evercraftmc.evercraft.bungee.listeners.BungeeListener;
import io.github.evercraftmc.evercraft.bungee.listeners.JoinListener;
import io.github.evercraftmc.evercraft.bungee.listeners.MessageListener;
import io.github.evercraftmc.evercraft.bungee.listeners.PingListener;
import io.github.evercraftmc.evercraft.bungee.listeners.VoteListener;
import io.github.evercraftmc.evercraft.bungee.scoreboard.ScoreBoard;
import io.github.evercraftmc.evercraft.bungee.util.formatting.ComponentFormatter;
import io.github.evercraftmc.evercraft.bungee.util.network.TabListUtil;
import io.github.evercraftmc.evercraft.bungee.util.player.BungeePlayerResolver;
import io.github.evercraftmc.evercraft.shared.PluginData;
import io.github.evercraftmc.evercraft.shared.PluginManager;
import io.github.evercraftmc.evercraft.shared.config.FileConfig;
import io.github.evercraftmc.evercraft.shared.config.MySQLConfig;
import io.github.evercraftmc.evercraft.shared.discord.DiscordBot;
import io.github.evercraftmc.evercraft.shared.economy.Economy;
import io.github.evercraftmc.evercraft.shared.util.Closable;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeMain extends Plugin implements io.github.evercraftmc.evercraft.shared.Plugin {
    private static BungeeMain Instance;

    private Logger logger;

    private FileConfig<BungeeConfig> config;
    private FileConfig<BungeeMessages> messages;
    private MySQLConfig<PluginData> data;

    private Economy economy;

    private DiscordBot bot;

    private List<BungeeCommand> commands;
    private List<BungeeListener> listeners;
    private List<Closable> assets;

    @Override
    public void onLoad() {
        BungeeMain.Instance = this;

        PluginManager.register(this);
    }

    @Override
    public void onEnable() {
        this.logger = PluginManager.createLogger(this.getDescription().getName(), "[{timeC} {typeU}]: [{name}] {message}");

        this.getLogger().info("Loading plugin..");

        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdir();
        }

        this.getLogger().info("Loading config..");

        this.config = new FileConfig<BungeeConfig>(BungeeConfig.class, this.getDataFolder().getAbsolutePath() + File.separator + "config.json");
        this.config.reload();

        if (this.config.getParsed() != null) {
            this.config.save();
        }

        this.messages = new FileConfig<BungeeMessages>(BungeeMessages.class, this.getDataFolder().getAbsolutePath() + File.separator + "messages.json");
        this.messages.reload();

        if (this.messages.getParsed() != null) {
            this.messages.save();
        }

        this.getLogger().info("Finished loading config");

        this.getLogger().info("Loading player data..");

        this.data = new MySQLConfig<PluginData>(PluginData.class, this.config.getParsed().database.host, this.config.getParsed().database.port, this.config.getParsed().database.name, this.config.getParsed().database.tableName, "data", this.config.getParsed().database.username, this.config.getParsed().database.password);

        this.getLogger().info("Finished loading player data");

        this.getLogger().info("Loading economy..");

        this.economy = new Economy(this.data);

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

        this.commands.add(new FriendCommand("friend", "Manage your friends", Arrays.asList("friends", "f"), "evercraft.commands.player.friend").register());
        this.commands.add(new SettingsCommand("settings", "Manage your settings", Arrays.asList("setting", "set", "s"), "evercraft.commands.player.settings").register());

        this.commands.add(new HubCommand("hub", "Go to the hub", Arrays.asList("lobby"), "evercraft.commands.warp.hub").register());

        for (String server : this.getProxy().getServersCopy().keySet()) {
            this.commands.add(new ServerCommand(server, "Go to the " + server + " server", Arrays.asList(), "evercraft.commands.warp.server").register());
        }

        this.commands.add(new PlayerInfoCommand("playerinfo", "Get information about a player", Arrays.asList(), "evercraft.commands.staff.playerinfo").register());

        this.commands.add(new KickCommand("kick", "Kick a player from the server", Arrays.asList(), "evercraft.commands.moderation.kick").register());
        this.commands.add(new TempBanCommand("tempban", "Temporarily ban a player from the server", Arrays.asList("ban"), "evercraft.commands.moderation.tempban").register());
        this.commands.add(new PermBanCommand("permban", "Ban a player from the server", Arrays.asList(), "evercraft.commands.moderation.permban").register());
        this.commands.add(new UnBanCommand("unban", "Unban a player from the server", Arrays.asList(), "evercraft.commands.moderation.unban").register());
        this.commands.add(new TempMuteCommand("tempmute", "Temporarily mute a player on the server", Arrays.asList("mute"), "evercraft.commands.moderation.tempmute").register());
        this.commands.add(new PermMuteCommand("permmute", "Mute a player on the server", Arrays.asList(), "evercraft.commands.moderation.permmute").register());
        this.commands.add(new UnMuteCommand("unmute", "Unmute a player on the server", Arrays.asList(), "evercraft.commands.moderation.unmute").register());
        this.commands.add(new LockChatCommand("lockchat", "Toggle chat lock", Arrays.asList(), "evercraft.commands.moderation.chatlock").register());
        this.commands.add(new MaintenanceCommand("maintenance", "Toggle maintenance mode", Arrays.asList("broken"), "evercraft.commands.moderation.maintenance").register());

        this.commands.add(new StaffChatCommand("staffchat", "Send a message to the staffchat", Arrays.asList("sc"), "evercraft.commands.staff.staffchat").register());
        this.commands.add(new CommandSpyCommand("commandspy", "Toggle your commandspy", Arrays.asList("cs"), "evercraft.commands.staff.commandspy").register());

        this.commands.add(new SpigotCommandCommand("spigotcommand", "Run a command on the spigot server", Arrays.asList(), null).register());
        this.commands.add(new SudoCommand("sudo", "Run a command or send a message as another player", Arrays.asList(), "evercraft.commands.staff.sudo").register());
        this.commands.add(new ImpersonateCommand("impersonate", "Send a message as another player (Even an offline one)", Arrays.asList("imp"), "evercraft.commands.staff.impersonate").register());

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

        for (ProxiedPlayer player : this.getProxy().getPlayers()) {
            player.setDisplayName(TextFormatter.translateColors(BungeePlayerResolver.getDisplayName(data, player.getUniqueId())));
            TabListUtil.updatePlayerName(player);
        }

        this.getLogger().info("Finished loading other assets..");

        this.getLogger().info("Finished loading plugin");

        this.getLogger().info("Starting Discord bot..");

        this.bot = new DiscordBot(this.config.getParsed().discord.token, this.config.getParsed().discord.guildId, new GatewayIntent[] { GatewayIntent.GUILD_MESSAGES }, new CacheFlag[] {}, MemberCachePolicy.NONE, this.config.getParsed().discord.statusType, this.config.getParsed().discord.status);
        this.bot.addListener((GenericEvent rawevent) -> {
            if (rawevent instanceof MessageReceivedEvent event) {
                if (event.getAuthor() != this.bot.getJDA().getSelfUser()) {
                    if (event.getMessage().getChannel().getId().equals(this.config.getParsed().discord.channelId)) {
                        for (ProxiedPlayer player : this.getProxy().getPlayers()) {
                            player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(this.messages.getParsed().chat.discord.replace("{player}", event.getMessage().getMember().getEffectiveName()).replace("{message}", event.getMessage().getContentDisplay()))));
                        }
                    } else if (event.getMessage().getChannel().getId().equals(this.config.getParsed().discord.staffChannelId)) {
                        for (ProxiedPlayer player : this.getProxy().getPlayers()) {
                            if (player.hasPermission("evercraft.commands.staff.staffchat")) {
                                player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(this.messages.getParsed().chat.staff.replace("{player}", event.getMessage().getMember().getEffectiveName()).replace("{message}", event.getMessage().getContentDisplay()))));
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

        this.config.close();
        this.messages.close();

        this.getLogger().info("Finished closing config..");

        this.getLogger().info("Closing player data..");

        this.data.close();

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
            player.setDisplayName(player.getName());
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

    @Override
    public Logger getLogger() {
        return this.logger;
    }

    public FileConfig<BungeeConfig> getPluginConfig() {
        return this.config;
    }

    public FileConfig<BungeeMessages> getPluginMessages() {
        return this.messages;
    }

    public MySQLConfig<PluginData> getPluginData() {
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