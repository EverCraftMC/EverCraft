package io.github.evercraftmc.evercraft.bungee;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.github.evercraftmc.evercraft.bungee.commands.BungeeCommand;
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
import io.github.evercraftmc.evercraft.bungee.commands.player.ReplyCommand;
import io.github.evercraftmc.evercraft.bungee.commands.player.SeenCommand;
import io.github.evercraftmc.evercraft.bungee.commands.player.SpigotCommandCommand;
import io.github.evercraftmc.evercraft.bungee.commands.staff.CommandSpyCommand;
import io.github.evercraftmc.evercraft.bungee.commands.staff.DebugCommand;
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
import io.github.evercraftmc.evercraft.shared.PluginData;
import io.github.evercraftmc.evercraft.shared.PluginManager;
import io.github.evercraftmc.evercraft.shared.discord.DiscordBot;
import io.github.evercraftmc.evercraft.shared.util.Closable;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import io.github.kale_ko.ejcl.file.bjsl.JsonFileConfig;
import io.github.kale_ko.ejcl.mysql.StructuredMySQLConfig;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeMain extends Plugin implements io.github.evercraftmc.evercraft.shared.Plugin {
    private static BungeeMain Instance;

    private JsonFileConfig<BungeeConfig> config;
    private JsonFileConfig<BungeeMessages> messages;
    private StructuredMySQLConfig<PluginData> data;

    private DiscordBot bot;

    private List<BungeeCommand> commands;
    private List<BungeeListener> listeners;
    private List<Closable> assets;

    public Integer serverMaxPlayers = 100;
    public String serverMotd = "";
    public Map<String, String> serverMotds = new HashMap<String, String>();

    @Override
    public void onLoad() {
        BungeeMain.Instance = this;

        PluginManager.register(this);
    }

    @Override
    public void onEnable() {
        this.getLogger().info("Loading plugin..");

        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdir();
        }

        this.getLogger().info("Loading config..");

        this.config = new JsonFileConfig<BungeeConfig>(BungeeConfig.class, this.getDataFolder().toPath().resolve("config.json").toFile());
        try {
            this.config.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.messages = new JsonFileConfig<BungeeMessages>(BungeeMessages.class, this.getDataFolder().toPath().resolve("messages.json").toFile());
        try {
            this.messages.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.getLogger().info("Finished loading config");

        this.getLogger().info("Loading player data..");

        this.data = new StructuredMySQLConfig<PluginData>(PluginData.class, this.config.get().database.host, this.config.get().database.port, this.config.get().database.name, this.config.get().database.tableName, this.config.get().database.username, this.config.get().database.password);
        try {
            this.data.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.getLogger().info("Finished loading player data");

        this.getLogger().info("Loading commands..");

        this.commands = new ArrayList<BungeeCommand>();

        this.commands.add(new InfoCommand("about", "Get the server about", Arrays.asList("info"), "evercraft.commands.info.about").register());
        this.commands.add(new InfoCommand("rules", "Get the server rules", Arrays.asList(), "evercraft.commands.info.rules").register());
        this.commands.add(new InfoCommand("discord", "Get the server discord link", Arrays.asList("community"), "evercraft.commands.info.discord").register());
        this.commands.add(new InfoCommand("vote", "Get the server vote link", Arrays.asList(), "evercraft.commands.info.vote").register());
        this.commands.add(new InfoCommand("staff", "Get the server staff", Arrays.asList(), "evercraft.commands.info.staff").register());

        this.commands.add(new SeenCommand("lastSeen", "Check when someone was last online", Arrays.asList("seen"), "evercraft.commands.player.seen").register());

        this.commands.add(new MessageCommand("message", "Message someone", Arrays.asList("msg", "tell"), "evercraft.commands.player.message").register());
        this.commands.add(new ReplyCommand("reply", "Reply to someone", Arrays.asList("r"), "evercraft.commands.player.message").register());

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

        this.commands.add(new ReloadCommand("evercraftbungeereload", "Reload the plugin", Arrays.asList("ecbreload"), "evercraft.commands.staff.reload").register());
        this.commands.add(new DebugCommand("evercraftbungeedebug", "Debug the plugin", Arrays.asList("ecbdebug"), "evercraft.commands.staff.debug").register());

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

        this.serverMaxPlayers = this.getProxy().getConfigurationAdapter().getListeners().iterator().next().getMaxPlayers();
        this.serverMotd = this.getProxy().getConfigurationAdapter().getListeners().iterator().next().getMotd();
        for (ServerInfo server : this.getProxy().getServersCopy().values()) {
            this.serverMotds.put(server.getName().toLowerCase(), server.getMotd());
        }

        this.getLogger().info("Finished loading other assets..");

        this.getLogger().info("Finished loading plugin");

        this.getLogger().info("Starting Discord bot..");

        this.bot = new DiscordBot(this.config.get().discord.token, this.config.get().discord.guildId, new GatewayIntent[] { GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT }, new CacheFlag[] {}, MemberCachePolicy.NONE, this.config.get().discord.statusType, this.config.get().discord.status);
        this.bot.addListener((GenericEvent rawevent) -> {
            if (rawevent instanceof MessageReceivedEvent event) {
                if (event.getAuthor() != this.bot.getJDA().getSelfUser()) {
                    if (event.getMessage().getChannel().getId().equals(this.config.get().discord.channelId)) {
                        for (ProxiedPlayer player : this.getProxy().getPlayers()) {
                            player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(this.messages.get().chat.discord.replace("{player}", event.getMessage().getMember().getEffectiveName()).replace("{message}", event.getMessage().getContentDisplay()))));
                        }
                    } else if (event.getMessage().getChannel().getId().equals(this.config.get().discord.staffChannelId)) {
                        for (ProxiedPlayer player : this.getProxy().getPlayers()) {
                            if (player.hasPermission("evercraft.commands.staff.staffchat")) {
                                player.sendMessage(ComponentFormatter.stringToComponent(TextFormatter.translateColors(this.messages.get().chat.staff.replace("{player}", event.getMessage().getMember().getEffectiveName()).replace("{message}", event.getMessage().getContentDisplay()))));
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

        try {
            this.config.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            this.messages.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.getLogger().info("Finished closing config..");

        this.getLogger().info("Closing player data..");

        try {
            this.data.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    public JsonFileConfig<BungeeConfig> getPluginConfig() {
        return this.config;
    }

    public JsonFileConfig<BungeeMessages> getPluginMessages() {
        return this.messages;
    }

    public StructuredMySQLConfig<PluginData> getPluginData() {
        return this.data;
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