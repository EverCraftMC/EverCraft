package com.kale_ko.evercraft.spigot;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.kale_ko.evercraft.shared.Plugin;
import com.kale_ko.evercraft.shared.config.FileConfig;
import com.kale_ko.evercraft.shared.config.MySQLConfig;
import com.kale_ko.evercraft.shared.economy.Economy;
import com.kale_ko.evercraft.spigot.listeners.MessageListener;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;

public class SpigotMain extends JavaPlugin implements Plugin {
    private static SpigotMain Instance;

    private FileConfig config;
    private FileConfig messages;
    private MySQLConfig players;

    private Economy economy;

    private LuckPerms luckPerms;

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

        // this.config.addDefault("database.host", "localhost");
        // this.config.addDefault("database.port", 3306);
        // this.config.addDefault("database.name", "evercraft");
        // this.config.addDefault("database.username", "root");
        // this.config.addDefault("database.password", "");

        this.getLogger().info("Finished loading config");

        this.getLogger().info("Loading messages..");

        this.messages = new FileConfig("messages.json");

        this.messages.addDefault("error.noPerms", "&cYou need the permission {permission} to do that");
        this.messages.addDefault("error.playerNotFound", "&cCouldn't find player {player}");
        this.messages.addDefault("error.invalidArgs", "&cInvalid arguments");

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

        // this.getLogger().info("Loading commands..");

        // this.getLogger().info("Finished loading commands");

        this.getLogger().info("Loading listeners..");

        this.getServer().getPluginManager().registerEvents(new MessageListener(), this);

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

        players.close();

        this.getLogger().info("Finished closing player data..");

        this.getLogger().info("Unloading listeners..");

        HandlerList.unregisterAll(this);

        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this);

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

    public static SpigotMain getInstance() {
        return SpigotMain.Instance;
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

    public String getServerName() {
        return this.serverName;
    }

    public void setServerName(String value) {
        this.serverName = value;
    }
}