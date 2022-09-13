package io.github.evercraftmc.evercraft.limbo;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import com.loohp.limbo.plugins.LimboPlugin;
import io.github.evercraftmc.evercraft.limbo.commands.LimboCommand;
import io.github.evercraftmc.evercraft.limbo.commands.staff.ReloadCommand;
import io.github.evercraftmc.evercraft.limbo.listeners.LimboListener;
import io.github.evercraftmc.evercraft.limbo.listeners.MessageListener;
import io.github.evercraftmc.evercraft.limbo.listeners.SpawnListener;
import io.github.evercraftmc.evercraft.shared.Plugin;
import io.github.evercraftmc.evercraft.shared.PluginManager;
import io.github.evercraftmc.evercraft.shared.config.FileConfig;
import io.github.evercraftmc.evercraft.shared.util.Closable;

public class LimboMain extends LimboPlugin implements Plugin {
    private static LimboMain Instance;

    private Logger logger;

    private FileConfig<LimboConfig> config;
    private FileConfig<LimboMessages> messages;

    private List<LimboCommand> commands;
    private List<LimboListener> listeners;
    private List<Closable> assets;

    private String serverName = "unknown";

    @Override
    public void onLoad() {
        LimboMain.Instance = this;

        PluginManager.register(this);
    }

    @Override
    public void onEnable() {
        this.logger = PluginManager.createLogger(this.getInfo().getName(), "[{timeC} {typeT}] [{name}] {message}");

        this.getLogger().info("Loading plugin..");

        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdir();
        }

        this.getLogger().info("Loading config..");

        this.config = new FileConfig<LimboConfig>(LimboConfig.class, this.getDataFolder().getAbsolutePath() + File.separator + "config.json");
        this.config.reload();

        if (this.config.getParsed() != null) {
            this.config.save();
        }

        this.serverName = this.config.getParsed().serverName;

        this.getLogger().info("Finished loading config");

        this.getLogger().info("Loading messages..");

        this.messages = new FileConfig<LimboMessages>(LimboMessages.class, this.getDataFolder().getAbsolutePath() + File.separator + "messages.json");
        this.messages.reload();

        if (this.messages.getParsed() != null) {
            this.messages.save();
        }

        this.getLogger().info("Finished loading messages");

        this.getLogger().info("Loading commands..");

        this.commands = new ArrayList<LimboCommand>();

        this.commands.add(new ReloadCommand("evercraftreload", "Reload the plugin", Arrays.asList("ecreload"), "evercraft.commands.staff.reload").register());

        this.getLogger().info("Finished loading commands");

        this.getLogger().info("Loading listeners..");

        this.listeners = new ArrayList<LimboListener>();

        this.listeners.add(new MessageListener().register());
        this.listeners.add(new SpawnListener().register());

        this.getLogger().info("Finished loading listeners");

        this.getLogger().info("Loading other assets..");

        this.assets = new ArrayList<Closable>();

        this.getLogger().info("Finished loading other assets..");

        this.getLogger().info("Finished loading plugin");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Disabling plugin..");

        this.getLogger().info("Closing config..");

        this.config.close();
        this.messages.close();

        this.getLogger().info("Finished closing config..");

        this.getLogger().info("Unregistering commands..");

        LimboMain.getInstance().getServer().getPluginManager().unregsiterAllCommands(LimboMain.getInstance());

        this.getLogger().info("Finished unregistering commands..");

        this.getLogger().info("Unregistering listeners..");

        LimboMain.getInstance().getServer().getEventsManager().unregisterAllListeners(this);

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

    public static LimboMain getInstance() {
        return LimboMain.Instance;
    }

    @Override
    public Logger getLogger() {
        return this.logger;
    }

    public FileConfig<LimboConfig> getPluginConfig() {
        return this.config;
    }

    public FileConfig<LimboMessages> getPluginMessages() {
        return this.messages;
    }

    public List<LimboCommand> getCommands() {
        return this.commands;
    }

    public List<LimboListener> getListeners() {
        return this.listeners;
    }

    public List<Closable> getAssets() {
        return this.assets;
    }

    public String getServerName() {
        return this.serverName;
    }

    public void setServerName(String value) {
        this.serverName = value;

        this.config.getParsed().serverName = value;
    }
}