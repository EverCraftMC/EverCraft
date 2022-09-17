package io.github.evercraftmc.evercraft.discord;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import io.github.evercraftmc.evercraft.discord.commands.DiscordCommand;
import io.github.evercraftmc.evercraft.discord.listeners.DiscordListener;
import io.github.evercraftmc.evercraft.shared.PluginData;
import io.github.evercraftmc.evercraft.shared.PluginManager;
import io.github.evercraftmc.evercraft.shared.config.FileConfig;
import io.github.evercraftmc.evercraft.shared.config.MySQLConfig;
import io.github.evercraftmc.evercraft.shared.discord.DiscordBot;
import io.github.evercraftmc.evercraft.shared.util.Closable;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class DiscordMain implements io.github.evercraftmc.evercraft.shared.Plugin {
    private static DiscordMain Instance;

    private Logger logger;

    private FileConfig<DiscordConfig> config;
    private FileConfig<DiscordMessages> messages;
    private MySQLConfig<PluginData> data;

    private DiscordBot bot;

    private List<DiscordCommand> commands;
    private List<DiscordListener> listeners;
    private List<Closable> assets;

    @Override
    public void onLoad() {
        DiscordMain.Instance = this;

        PluginManager.register(this);

        this.logger = PluginManager.createLogger("Discord Bot", "[{timeC} {typeU}] {message}");
    }

    @Override
    public void onEnable() {
        this.getLogger().info("Loading bot..");

        if (!new File(System.getProperty("user.dir")).exists()) {
            new File(System.getProperty("user.dir")).mkdir();
        }

        this.getLogger().info("Loading config..");

        this.config = new FileConfig<DiscordConfig>(DiscordConfig.class, new File(System.getProperty("user.dir")).getAbsolutePath() + File.separator + "config.json");
        this.config.reload();

        if (this.config.getParsed() != null) {
            this.config.save();
        }

        this.messages = new FileConfig<DiscordMessages>(DiscordMessages.class, new File(System.getProperty("user.dir")).getAbsolutePath() + File.separator + "messages.json");
        this.messages.reload();

        if (this.messages.getParsed() != null) {
            this.messages.save();
        }

        this.getLogger().info("Finished loading config");

        this.getLogger().info("Loading player data..");

        this.data = new MySQLConfig<PluginData>(PluginData.class, this.config.getParsed().database.host, this.config.getParsed().database.port, this.config.getParsed().database.name, this.config.getParsed().database.tableName, "data", this.config.getParsed().database.username, this.config.getParsed().database.password);

        this.getLogger().info("Finished loading player data");

        this.getLogger().info("Loading commands..");

        this.commands = new ArrayList<DiscordCommand>();

        // this.commands.add(new TestCommand("test", "Just a test command", Arrays.asList("test2"), null).register());

        this.getLogger().info("Finished loading commands");

        this.getLogger().info("Loading listeners..");

        this.listeners = new ArrayList<DiscordListener>();

        // TODO Listeners

        this.getLogger().info("Finished loading listeners");

        this.getLogger().info("Loading other assets..");

        this.assets = new ArrayList<Closable>();

        this.getLogger().info("Finished loading other assets..");

        this.getLogger().info("Finished loading bot");

        this.getLogger().info("Starting JDA..");

        this.bot = new DiscordBot(this.config.getParsed().discord.token, this.config.getParsed().discord.guildId, new GatewayIntent[] { GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_BANS }, new CacheFlag[] { CacheFlag.ROLE_TAGS, CacheFlag.MEMBER_OVERRIDES }, MemberCachePolicy.ONLINE, this.config.getParsed().discord.statusType, this.config.getParsed().discord.status);
        this.bot.addListener((GenericEvent rawevent) -> {
            if (rawevent instanceof ReadyEvent event) {
                this.getLogger().info("JDA Logged in as \"" + event.getJDA().getSelfUser().getAsTag() + "\"");
            }

            if (rawevent instanceof MessageReceivedEvent event) {
                if (event.getMessage().getContentRaw().startsWith(this.getPluginConfig().getParsed().prefix) && !event.getAuthor().equals(event.getJDA().getSelfUser())) {
                    List<String> args = Arrays.asList(event.getMessage().getContentRaw().substring(this.getPluginConfig().getParsed().prefix.length()).split(" "));
                    String command = args.get(0);
                    args = args.subList(1, args.size());

                    Boolean found = false;

                    for (DiscordCommand discCommand : this.commands) {
                        if (discCommand.getName().equalsIgnoreCase(command)) {
                            found = true;

                            discCommand.execute(event.getMessage(), args.toArray(new String[] {}));
                        }
                    }

                    if (!found) {
                        event.getMessage().reply(this.getPluginMessages().getParsed().error.invalidArgs).queue();;
                    }
                }
            }

            // TODO Do stuff for event listeners
        });
        this.assets.add(this.bot);
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Disabling bot..");

        this.getLogger().info("Closing config..");

        this.config.close();
        this.messages.close();

        this.getLogger().info("Finished closing config..");

        this.getLogger().info("Closing player data..");

        this.data.close();

        this.getLogger().info("Finished closing player data..");

        this.getLogger().info("Unregistering commands..");

        for (DiscordCommand command : this.commands) {
            command.unregister();
        }

        this.getLogger().info("Finished unregistering commands..");

        this.getLogger().info("Unregistering listeners..");

        for (DiscordListener listener : this.listeners) {
            listener.unregister();
        }

        this.getLogger().info("Finished unregistering listeners..");

        this.getLogger().info("Closing assets..");

        for (Closable asset : this.assets) {
            asset.close();
        }

        this.getLogger().info("Finished closing assets..");

        this.getLogger().info("Finished disabling bot");
    }

    @Override
    public void reload() {
        this.getLogger().info("Reloading bot..");

        this.onDisable();

        this.onEnable();

        this.getLogger().info("Finished reloading bot");
    }

    public static DiscordMain getInstance() {
        return DiscordMain.Instance;
    }

    @Override
    public Logger getLogger() {
        return this.logger;
    }

    public FileConfig<DiscordConfig> getPluginConfig() {
        return this.config;
    }

    public FileConfig<DiscordMessages> getPluginMessages() {
        return this.messages;
    }

    public MySQLConfig<PluginData> getPluginData() {
        return this.data;
    }

    public List<DiscordCommand> getCommands() {
        return this.commands;
    }

    public List<DiscordListener> getListeners() {
        return this.listeners;
    }

    public List<Closable> getAssets() {
        return this.assets;
    }

    public DiscordBot getBot() {
        return this.bot;
    }

    public JDA getJDA() {
        return this.bot.getJDA();
    }

    public Guild getGuild() {
        return this.bot.getGuild();
    }

    public static void main(String[] args) {
        DiscordMain main = new DiscordMain();
        main.onLoad();
        main.onEnable();
    }
}
