package com.kale_ko.evercraft.spigot;

import com.kale_ko.evercraft.shared.Plugin;
import com.kale_ko.evercraft.shared.config.FileConfig;
import com.kale_ko.evercraft.shared.config.MySQLConfig;
import org.bukkit.plugin.java.JavaPlugin;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;

public class SpigotMain extends JavaPlugin implements Plugin {
    private FileConfig config;
    private FileConfig messages;
    private MySQLConfig players;

    private LuckPerms luckPerms;

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

        this.messages = new FileConfig("messages.json");

        this.getLogger().info("Finished loading messages");

        this.getLogger().info("Loading player data..");

        this.players = new MySQLConfig(this.config.getString("database.host"), this.config.getInt("database.port"), this.config.getString("database.name"), "players", this.config.getString("database.username"), this.config.getString("database.password"));

        this.getLogger().info("Finished loading player data");

        this.getLogger().info("Loading LuckPerms integration..");

        this.luckPerms = LuckPermsProvider.get();

        this.getLogger().info("Finished loading LuckPerms integration");

        this.getLogger().info("Finished loading plugin");
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

        this.getLogger().info("Finished disabling plugin");
    }

    @Override
    public void reload() {
        this.getLogger().info("Reloading plugin..");

        this.onDisable();

        this.onEnable();

        this.getLogger().info("Finished reloading plugin");
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

    public LuckPerms getLuckPerms() {
        return this.luckPerms;
    }
}