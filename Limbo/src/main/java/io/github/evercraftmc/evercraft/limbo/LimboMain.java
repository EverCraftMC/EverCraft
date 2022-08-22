package io.github.evercraftmc.evercraft.limbo;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.loohp.limbo.plugins.LimboPlugin;
import io.github.evercraftmc.evercraft.limbo.commands.LimboCommand;
import io.github.evercraftmc.evercraft.limbo.commands.staff.ReloadCommand;
import io.github.evercraftmc.evercraft.limbo.listeners.LimboListener;
import io.github.evercraftmc.evercraft.limbo.listeners.MessageListener;
import io.github.evercraftmc.evercraft.limbo.listeners.SpawnListener;
import io.github.evercraftmc.evercraft.shared.Plugin;
import io.github.evercraftmc.evercraft.shared.config.FileConfig;
import io.github.evercraftmc.evercraft.shared.util.Closable;

public class LimboMain extends LimboPlugin implements Plugin {
    private static LimboMain Instance;

    private FileConfig<LimboConfig> config;
    private FileConfig<LimboMessages> messages;

    private List<LimboCommand> commands;
    private List<LimboListener> listeners;
    private List<Closable> assets;

    private String serverName = "unknown";

    @Override
    public void onLoad() {
        LimboMain.Instance = this;
    }

    @Override
    public void onEnable() {
        System.out.println("Loading plugin..");

        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdir();
        }

        System.out.println("Loading config..");

        this.config = new FileConfig<LimboConfig>(LimboConfig.class, this.getDataFolder().getAbsolutePath() + File.separator + "config.json");
        this.config.reload();

        if (this.config.getParsed() != null) {
            this.config.save();
        }

        this.serverName = this.config.getParsed().serverName;

        System.out.println("Finished loading config");

        System.out.println("Loading messages..");

        this.messages = new FileConfig<LimboMessages>(LimboMessages.class, this.getDataFolder().getAbsolutePath() + File.separator + "messages.json");
        this.messages.reload();

        if (this.messages.getParsed() != null) {
            this.messages.save();
        }

        System.out.println("Finished loading messages");

        System.out.println("Loading commands..");

        this.commands = new ArrayList<LimboCommand>();

        this.commands.add(new ReloadCommand("evercraftreload", "Reload the plugin", Arrays.asList("ecreload"), "evercraft.commands.staff.reload").register());

        System.out.println("Finished loading commands");

        System.out.println("Loading listeners..");

        this.listeners = new ArrayList<LimboListener>();

        this.listeners.add(new MessageListener().register());
        this.listeners.add(new SpawnListener().register());

        System.out.println("Finished loading listeners");

        System.out.println("Loading other assets..");

        this.assets = new ArrayList<Closable>();

        System.out.println("Finished loading other assets..");

        System.out.println("Finished loading plugin");
    }

    @Override
    public void onDisable() {
        System.out.println("Disabling plugin..");

        System.out.println("Closing config..");

        this.config.close();
        this.messages.close();

        System.out.println("Finished closing config..");

        System.out.println("Unregistering commands..");

        LimboMain.getInstance().getServer().getPluginManager().unregsiterAllCommands(LimboMain.getInstance());

        System.out.println("Finished unregistering commands..");

        System.out.println("Unregistering listeners..");

        LimboMain.getInstance().getServer().getEventsManager().unregisterAllListeners(this);

        System.out.println("Finished unregistering listeners..");

        System.out.println("Closing assets..");

        for (Closable asset : this.assets) {
            asset.close();
        }

        System.out.println("Finished closing assets..");

        System.out.println("Finished disabling plugin");
    }

    @Override
    public void reload() {
        System.out.println("Reloading plugin..");

        this.onDisable();

        this.onEnable();

        System.out.println("Finished reloading plugin");
    }

    public static LimboMain getInstance() {
        return LimboMain.Instance;
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