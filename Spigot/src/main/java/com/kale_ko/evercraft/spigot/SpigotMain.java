package com.kale_ko.evercraft.spigot;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.kale_ko.evercraft.shared.Plugin;
import com.kale_ko.evercraft.shared.config.FileConfig;
import com.kale_ko.evercraft.shared.config.MySQLConfig;
import com.kale_ko.evercraft.shared.economy.Economy;
import com.kale_ko.evercraft.shared.util.Closable;
import com.kale_ko.evercraft.spigot.commands.SpigotCommand;
import com.kale_ko.evercraft.spigot.commands.kit.KitCommand;
import com.kale_ko.evercraft.spigot.commands.player.BungeeCommandCommand;
import com.kale_ko.evercraft.spigot.commands.staff.gamemode.AdventureCommand;
import com.kale_ko.evercraft.spigot.commands.staff.gamemode.CreativeCommand;
import com.kale_ko.evercraft.spigot.commands.staff.gamemode.GameModeCommand;
import com.kale_ko.evercraft.spigot.commands.staff.gamemode.SpectatorCommand;
import com.kale_ko.evercraft.spigot.commands.staff.gamemode.SurvivalCommand;
import com.kale_ko.evercraft.spigot.commands.warp.DelWarpCommand;
import com.kale_ko.evercraft.spigot.commands.warp.SetWarpCommand;
import com.kale_ko.evercraft.spigot.commands.warp.SpawnCommand;
import com.kale_ko.evercraft.spigot.commands.warp.WarpCommand;
import com.kale_ko.evercraft.spigot.listeners.JoinListener;
import com.kale_ko.evercraft.spigot.listeners.MessageListener;
import com.kale_ko.evercraft.spigot.listeners.SpigotListener;
import org.bukkit.plugin.java.JavaPlugin;

public class SpigotMain extends JavaPlugin implements Plugin {
    private static SpigotMain Instance;

    private FileConfig config;
    private FileConfig messages;
    private MySQLConfig data;

    private FileConfig warps;
    private FileConfig kits;

    private Economy economy;

    private List<SpigotCommand> commands;
    private List<SpigotListener> listeners;
    private List<Closable> assets;

    private String serverName = "unknown";

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

        this.config = new FileConfig(this.getDataFolder().getAbsolutePath() + File.separator + "config.json");
        this.config.reload();

        this.config.addDefault("database.host", "localhost");
        this.config.addDefault("database.port", 3306);
        this.config.addDefault("database.name", "evercraft");
        this.config.addDefault("database.username", "root");
        this.config.addDefault("database.password", "");

        this.config.copyDefaults();

        this.getLogger().info("Finished loading config");

        this.getLogger().info("Loading messages..");

        this.messages = new FileConfig(this.getDataFolder().getAbsolutePath() + File.separator + "messages.json");
        this.messages.reload();

        this.messages.addDefault("error.noPerms", "&cYou need the permission {permission} to do that");
        this.messages.addDefault("error.noConsole", "&cYou can't do that from the console");
        this.messages.addDefault("error.playerNotFound", "&cCouldn't find player {player}");
        this.messages.addDefault("error.invalidArgs", "&cInvalid arguments");
        this.messages.addDefault("warp.warped", "&aSuccessfully warped to {warp}");
        this.messages.addDefault("warp.setWarp", "&aSuccessfully set warp {warp} to your location");
        this.messages.addDefault("warp.delWarp", "&aSuccessfully deleted warp {warp}");
        this.messages.addDefault("warp.notFound", "&cWarp {warp} does not exist");
        this.messages.addDefault("kit.kit", "&aSuccessfully got kit {kit}");
        this.messages.addDefault("kit.notFound", "&cKit {kit} does not exist");
        this.messages.addDefault("staff.gamemode", "&aSuccessfully set your gamemode to {gamemode}");

        this.messages.copyDefaults();

        this.getLogger().info("Finished loading messages");

        this.getLogger().info("Loading player data..");

        this.data = new MySQLConfig(this.config.getString("database.host"), this.config.getInteger("database.port"), this.config.getString("database.name"), "data", this.config.getString("database.username"), this.config.getString("database.password"));

        this.getLogger().info("Finished loading player data");

        this.getLogger().info("Loading other data..");

        this.warps = new FileConfig(this.getDataFolder().getAbsolutePath() + File.separator + "warps.json");
        this.warps.reload();

        this.kits = new FileConfig(this.getDataFolder().getAbsolutePath() + File.separator + "kits.json");
        this.kits.reload();

        this.getLogger().info("Finished loading other data..");

        this.getLogger().info("Loading economy..");

        this.economy = new Economy(this.getData());

        this.getLogger().info("Finished loading economy");

        this.getLogger().info("Loading commands..");

        this.commands = new ArrayList<SpigotCommand>();

        this.commands.add(new WarpCommand("warp", "Teleport to a warp", Arrays.asList(), "evercraft.commands.warp.warp").register());
        this.commands.add(new SpawnCommand("spawn", "Teleport to the spawn", Arrays.asList(), "evercraft.commands.warp.spawn").register());
        this.commands.add(new SetWarpCommand("setwarp", "Set a warp to your location", Arrays.asList(), "evercraft.commands.warp.setwarp").register());
        this.commands.add(new DelWarpCommand("delwarp", "Delete a warp", Arrays.asList(), "evercraft.commands.warp.delwarp").register());

        this.commands.add(new KitCommand("kit", "Get a kit", Arrays.asList(), "evercraft.commands.kit.kit").register());

        this.commands.add(new GameModeCommand("gamemode", "Change your gamemode", Arrays.asList("gm"), "evercraft.commands.gamemode.use").register());
        this.commands.add(new SurvivalCommand("gms", "Change your gamemode to survival", Arrays.asList("gm0"), "evercraft.commands.gamemode.survival").register());
        this.commands.add(new CreativeCommand("gmc", "Change your gamemode to creative", Arrays.asList("gm1"), "evercraft.commands.gamemode.creative").register());
        this.commands.add(new AdventureCommand("gma", "Change your gamemode to adventure", Arrays.asList("gm2"), "evercraft.commands.gamemode.adventure").register());
        this.commands.add(new SpectatorCommand("gmsp", "Change your gamemode to spectator", Arrays.asList("gm3"), "evercraft.commands.gamemode.spectator").register());

        this.commands.add(new BungeeCommandCommand("bungeecommand", "Run a command on the bungee sever", Arrays.asList(), null).register());

        this.getLogger().info("Finished loading commands");

        this.getLogger().info("Loading listeners..");

        this.listeners = new ArrayList<SpigotListener>();

        this.listeners.add(new MessageListener().register());
        this.listeners.add(new JoinListener().register());

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new MessageListener());

        this.getLogger().info("Finished loading listeners");

        this.assets = new ArrayList<Closable>();

        this.getLogger().info("Finished loading plugin");

        // TODO Fix getting server name

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

    public FileConfig getKits() {
        return this.kits;
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