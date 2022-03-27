package com.kale_ko.evercraft.spigot;

import java.util.List;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.kale_ko.evercraft.shared.Plugin;
import com.kale_ko.evercraft.shared.config.FileConfig;
import com.kale_ko.evercraft.shared.config.MySQLConfig;
import com.kale_ko.evercraft.shared.economy.Economy;
import com.kale_ko.evercraft.shared.util.Closable;
import com.kale_ko.evercraft.spigot.commands.SpigotCommand;
import com.kale_ko.evercraft.spigot.commands.staff.gamemode.AdventureCommand;
import com.kale_ko.evercraft.spigot.commands.staff.gamemode.CreativeCommand;
import com.kale_ko.evercraft.spigot.commands.staff.gamemode.GameModeCommand;
import com.kale_ko.evercraft.spigot.commands.staff.gamemode.SpectatorCommand;
import com.kale_ko.evercraft.spigot.commands.staff.gamemode.SurvivalCommand;
import com.kale_ko.evercraft.spigot.commands.warp.SetWarpCommand;
import com.kale_ko.evercraft.spigot.commands.warp.SpawnCommand;
import com.kale_ko.evercraft.spigot.commands.warp.WarpCommand;
import com.kale_ko.evercraft.spigot.listeners.MessageListener;
import com.kale_ko.evercraft.spigot.listeners.SpigotListener;
import org.bukkit.plugin.java.JavaPlugin;

public class SpigotMain extends JavaPlugin implements Plugin {
    private static SpigotMain Instance;

    private FileConfig config;
    private FileConfig messages;
    private MySQLConfig data;

    private FileConfig warps;

    private Economy economy;

    private List<SpigotCommand> commands;
    private List<SpigotListener> listeners;
    private List<Closable> assets;

    private String serverName;

    @Override
    public void onLoad() {
        SpigotMain.Instance = this;
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

        this.getLogger().info("Finished loading config");

        this.getLogger().info("Loading messages..");

        this.messages = new FileConfig(this.getDataFolder().getAbsolutePath() + "messages.json");

        this.messages.addDefault("error.noPerms", "&cYou need the permission {permission} to do that");
        this.messages.addDefault("error.noConsole", "&cYou can't do that from the console");
        this.messages.addDefault("error.playerNotFound", "&cCouldn't find player {player}");
        this.messages.addDefault("error.invalidArgs", "&cInvalid arguments");
        this.messages.addDefault("warp.warped", "&aSuccessfully warped to {warp}");
        this.messages.addDefault("warp.setWarp", "&aSuccessfully set warp {warp} to your location");
        this.messages.addDefault("warp.notFound", "&cWarp {warp} does not exist");
        this.messages.addDefault("staff.gamemode", "&cSuccessfully set your gamemode to {gamemode}");

        this.getLogger().info("Finished loading messages");

        this.getLogger().info("Loading player data..");

        this.data = new MySQLConfig(this.config.getString("database.host"), this.config.getInt("database.port"), this.config.getString("database.name"), "data", this.config.getString("database.username"), this.config.getString("database.password"));

        this.getLogger().info("Finished loading player data");

        this.getLogger().info("Loading other data..");

        this.warps = new FileConfig(this.getDataFolder().getAbsolutePath() + "warps.json");

        this.getLogger().info("Finished loading other data..");

        this.getLogger().info("Loading economy..");

        this.economy = new Economy(this.getData());

        this.getLogger().info("Finished loading economy");

        this.getLogger().info("Loading commands..");

        this.commands.add(new WarpCommand().register());
        this.commands.add(new SpawnCommand().register());
        this.commands.add(new SetWarpCommand().register());

        this.commands.add(new GameModeCommand().register());
        this.commands.add(new SurvivalCommand().register());
        this.commands.add(new CreativeCommand().register());
        this.commands.add(new AdventureCommand().register());
        this.commands.add(new SpectatorCommand().register());

        this.getLogger().info("Finished loading commands");

        this.getLogger().info("Loading listeners..");

        this.listeners.add(new MessageListener().register());

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new MessageListener());

        this.getLogger().info("Finished loading listeners");

        this.getLogger().info("Finished loading plugin");

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("GetServer");
        this.getServer().sendPluginMessage(this, "BungeeCord", out.toByteArray());
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

        for (SpigotCommand command : this.commands) {
            command.unregister();
        }

        this.getLogger().info("Finished unregistering commands..");

        this.getLogger().info("Unregistering listeners..");

        for (SpigotListener listener : this.listeners) {
            listener.unregister();
        }

        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this);

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

    public static SpigotMain getInstance() {
        return SpigotMain.Instance;
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

    public FileConfig getWarps() {
        return this.warps;
    }

    public Economy getEconomy() {
        return this.economy;
    }

    public String getServerName() {
        return this.serverName;
    }

    public void setServerName(String value) {
        this.serverName = value;
    }
}