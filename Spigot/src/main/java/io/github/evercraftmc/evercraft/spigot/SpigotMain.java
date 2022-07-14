package io.github.evercraftmc.evercraft.spigot;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import io.github.evercraftmc.evercraft.shared.Plugin;
import io.github.evercraftmc.evercraft.shared.config.FileConfig;
import io.github.evercraftmc.evercraft.shared.config.MySQLConfig;
import io.github.evercraftmc.evercraft.shared.economy.Economy;
import io.github.evercraftmc.evercraft.shared.util.Closable;
import io.github.evercraftmc.evercraft.shared.util.formatting.TextFormatter;
import io.github.evercraftmc.evercraft.spigot.commands.SpigotCommand;
import io.github.evercraftmc.evercraft.spigot.commands.games.GameCommand;
import io.github.evercraftmc.evercraft.spigot.commands.kit.DelKitCommand;
import io.github.evercraftmc.evercraft.spigot.commands.kit.KitCommand;
import io.github.evercraftmc.evercraft.spigot.commands.kit.SetKitCommand;
import io.github.evercraftmc.evercraft.spigot.commands.player.BungeeCommandCommand;
import io.github.evercraftmc.evercraft.spigot.commands.player.ChestProtectionCommand;
import io.github.evercraftmc.evercraft.spigot.commands.player.PassiveCommand;
import io.github.evercraftmc.evercraft.spigot.commands.staff.ReloadCommand;
import io.github.evercraftmc.evercraft.spigot.commands.staff.gamemode.AdventureCommand;
import io.github.evercraftmc.evercraft.spigot.commands.staff.gamemode.CreativeCommand;
import io.github.evercraftmc.evercraft.spigot.commands.staff.gamemode.GameModeCommand;
import io.github.evercraftmc.evercraft.spigot.commands.staff.gamemode.SpectatorCommand;
import io.github.evercraftmc.evercraft.spigot.commands.staff.gamemode.SurvivalCommand;
import io.github.evercraftmc.evercraft.spigot.commands.warp.DelWarpCommand;
import io.github.evercraftmc.evercraft.spigot.commands.warp.HomeCommand;
import io.github.evercraftmc.evercraft.spigot.commands.warp.SetWarpCommand;
import io.github.evercraftmc.evercraft.spigot.commands.warp.SpawnCommand;
import io.github.evercraftmc.evercraft.spigot.commands.warp.WarpCommand;
import io.github.evercraftmc.evercraft.spigot.games.Game;
import io.github.evercraftmc.evercraft.spigot.games.pvp.DodgeBowGame;
import io.github.evercraftmc.evercraft.spigot.games.pvp.PvpGame;
import io.github.evercraftmc.evercraft.spigot.games.race.BoatRaceGame;
import io.github.evercraftmc.evercraft.spigot.listeners.ChestProtectionListener;
import io.github.evercraftmc.evercraft.spigot.listeners.JoinListener;
import io.github.evercraftmc.evercraft.spigot.listeners.MessageListener;
import io.github.evercraftmc.evercraft.spigot.listeners.PvPListener;
import io.github.evercraftmc.evercraft.spigot.listeners.SpigotListener;
import io.github.evercraftmc.evercraft.spigot.util.formatting.ComponentFormatter;
import io.github.evercraftmc.evercraft.spigot.util.player.SpigotPlayerResolver;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class SpigotMain extends JavaPlugin implements Plugin {
    private static SpigotMain Instance;

    private FileConfig config;
    private FileConfig messages;
    private MySQLConfig data;

    private FileConfig warps;
    private FileConfig kits;
    private FileConfig chests;

    private Economy economy;

    private List<SpigotCommand> commands;
    private List<SpigotListener> listeners;
    private List<Closable> assets;

    private FileConfig games;
    private List<Game> registeredGames;

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

        this.config.addDefault("serverName", "unknown");
        this.config.addDefault("warp.overidespawn", true);
        this.config.addDefault("warp.clearonwarp", false);
        this.config.addDefault("chestProtectionEnabled", false);
        this.config.addDefault("protectable", Arrays.asList("chest:0", "trapped_chest:0", "ender_chest:1", "barrel:0", "shulker_box:0", "{color}_shulker_box:0", "furnace:0", "blast_furnace:0", "smoker:0", "hopper:0", "dropper:0", "dispenser:0", "jukebox:0", "{color}_bed:1"));
        this.config.addDefault("passiveEnabled", false);
        this.config.addDefault("database.host", "localhost");
        this.config.addDefault("database.port", 3306);
        this.config.addDefault("database.name", "evercraft");
        this.config.addDefault("database.username", "root");
        this.config.addDefault("database.password", "");

        this.config.copyDefaults();

        this.serverName = this.config.getString("serverName");

        this.getLogger().info("Finished loading config");

        this.getLogger().info("Loading messages..");

        this.messages = new FileConfig(this.getDataFolder().getAbsolutePath() + File.separator + "messages.json");
        this.messages.reload();

        this.messages.addDefault("error.noPerms", "&cYou need the permission \"{permission}\" to do that");
        this.messages.addDefault("error.noConsole", "&cYou can't do that from the console");
        this.messages.addDefault("error.playerNotFound", "&cCouldn't find player \"{player}&r&c\"");
        this.messages.addDefault("error.invalidArgs", "&cInvalid arguments");
        this.messages.addDefault("reload.reloading", "&aReloading plugin..");
        this.messages.addDefault("reload.reloaded", "&aSuccessfully reloaded");
        this.messages.addDefault("warp.warped", "&aSuccessfully warped to {warp}");
        this.messages.addDefault("warp.setWarp", "&aSuccessfully set warp {warp}&r&a to your location");
        this.messages.addDefault("warp.delWarp", "&aSuccessfully deleted warp {warp}");
        this.messages.addDefault("warp.notFound", "&cWarp {warp}&r&c does not exist");
        this.messages.addDefault("kit.kit", "&aSuccessfully got kit {kit}");
        this.messages.addDefault("kit.setkit", "&aSuccessfully set kit {kit}&r&a to your inventory");
        this.messages.addDefault("kit.delkit", "&cSuccessfully deleted kit {kit}");
        this.messages.addDefault("kit.notFound", "&cKit {kit}&r&c does not exist");
        this.messages.addDefault("games.joined", "&aYou have joined {game} &r&a({players} / {max})");
        this.messages.addDefault("games.join", "&a{player} &r&ahas joined the game ({players} / {max})");
        this.messages.addDefault("games.left", "&aYou have left {game}");
        this.messages.addDefault("games.leave", "&a{player} &r&ahas left the game ({players} / {max})");
        this.messages.addDefault("games.joinedTeam", "&aYou have joined team {team}");
        this.messages.addDefault("games.teamJoin", "&a{player} &r&ahas joined team {team}");
        this.messages.addDefault("games.leftTeam", "&aYou have left team {team}");
        this.messages.addDefault("games.teamLeave", "&a{player} &r&ahas left team {team}");
        this.messages.addDefault("games.start", "&aThat game has started");
        this.messages.addDefault("games.stop", "&aThat game has stopped");
        this.messages.addDefault("games.full", "&cThat game is full");
        this.messages.addDefault("games.started", "&cThat game has already started");
        this.messages.addDefault("games.notFound", "&cGame {game} &r&ccould not be found");
        this.messages.addDefault("games.alreadyInGame", "&cYou are already in a game");
        this.messages.addDefault("games.notInGame", "&cYou are not in a game");
        this.messages.addDefault("passive", "&aSuccessfully toggled passive mode {value}");
        this.messages.addDefault("chestProtection.claimed", "&aThis block is now claimed");
        this.messages.addDefault("chestProtection.unclaimed", "&cThis block is no longer claimed");
        this.messages.addDefault("chestProtection.protected", "&aThis block is now protected");
        this.messages.addDefault("chestProtection.unprotected", "&cThis block is no longer protected");
        this.messages.addDefault("chestProtection.allowedUse", "&aThis block is now usable by others");
        this.messages.addDefault("chestProtection.disallowedUse", "&cThis block is no longer usable by others");
        this.messages.addDefault("chestProtection.notyours", "&cIm sorry but that isn't yours >:(");
        this.messages.addDefault("chestProtection.noblock", "&cYou must be looking at a block to do that");
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

        if (this.config.getBoolean("chestProtectionEnabled")) {
            this.chests = new FileConfig(this.getDataFolder().getAbsolutePath() + File.separator + "chests.json");
            this.chests.reload();
        }

        this.getLogger().info("Finished loading other data..");

        this.getLogger().info("Loading economy..");

        this.economy = new Economy(this.data);

        this.getLogger().info("Finished loading economy");

        this.getLogger().info("Loading commands..");

        this.commands = new ArrayList<SpigotCommand>();

        this.commands.add(new WarpCommand("warp", "Teleport to a warp", Arrays.asList(), "evercraft.commands.warp.warp").register());
        this.commands.add(new SpawnCommand("spawn", "Teleport to the spawn", Arrays.asList(), "evercraft.commands.warp.spawn").register());
        this.commands.add(new HomeCommand("home", "Teleport to your home (bed)", Arrays.asList("bed"), "evercraft.commands.warp.home").register());
        this.commands.add(new SetWarpCommand("setwarp", "Set a warp to your location", Arrays.asList(), "evercraft.commands.warp.setwarp").register());
        this.commands.add(new DelWarpCommand("delwarp", "Delete a warp", Arrays.asList(), "evercraft.commands.warp.delwarp").register());

        this.commands.add(new KitCommand("kit", "Get a kit", Arrays.asList(), "evercraft.commands.kit.kit").register());
        this.commands.add(new SetKitCommand("setkit", "Set a kit", Arrays.asList(), "evercraft.commands.kit.setkit").register());
        this.commands.add(new DelKitCommand("delkit", "Delete a kit", Arrays.asList(), "evercraft.commands.kit.delkit").register());

        if (this.config.getBoolean("chestProtectionEnabled")) {
            this.commands.add(new ChestProtectionCommand("chestprotection", "Manage you chest protections", Arrays.asList("cp", "chests"), "evercraft.commands.player.chestprotection").register());
        }

        if (this.getPluginConfig().getBoolean("passiveEnabled")) {
            this.commands.add(new PassiveCommand("passive", "Toggle passive mode on/off", Arrays.asList("togglepassive", "pvp"), "evercraft.commands.player.passive").register());
        }

        this.commands.add(new GameModeCommand("gamemode", "Change your gamemode", Arrays.asList("gm"), "evercraft.commands.gamemode.use").register());
        this.commands.add(new SurvivalCommand("gms", "Change your gamemode to survival", Arrays.asList("gm0"), "evercraft.commands.gamemode.survival").register());
        this.commands.add(new CreativeCommand("gmc", "Change your gamemode to creative", Arrays.asList("gm1"), "evercraft.commands.gamemode.creative").register());
        this.commands.add(new AdventureCommand("gma", "Change your gamemode to adventure", Arrays.asList("gm2"), "evercraft.commands.gamemode.adventure").register());
        this.commands.add(new SpectatorCommand("gmsp", "Change your gamemode to spectator", Arrays.asList("gm3"), "evercraft.commands.gamemode.spectator").register());

        this.commands.add(new BungeeCommandCommand("bungeecommand", "Run a command on the bungee sever", Arrays.asList(), null).register());

        this.commands.add(new ReloadCommand("evercraftreload", "Reload the plugin", Arrays.asList("ecreload"), "evercraft.commands.staff.reload").register());

        this.getLogger().info("Finished loading commands");

        this.getLogger().info("Loading listeners..");

        this.listeners = new ArrayList<SpigotListener>();

        this.listeners.add(new MessageListener().register());
        this.listeners.add(new JoinListener().register());

        if (this.config.getBoolean("chestProtectionEnabled")) {
            this.listeners.add(new ChestProtectionListener().register());
        }

        if (this.getPluginConfig().getBoolean("passiveEnabled")) {
            this.listeners.add(new PvPListener().register());
        }

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new MessageListener());

        this.getLogger().info("Finished loading listeners");

        this.getLogger().info("Loading games..");

        this.games = new FileConfig(this.getDataFolder().getAbsolutePath() + File.separator + "games.json");
        this.games.reload();

        this.games.addDefault("enabled", false);

        this.games.copyDefaults();

        this.registeredGames = new ArrayList<Game>();

        if (this.games.getBoolean("enabled")) {
            this.commands.add(new GameCommand("game", "Join/leave a game", Arrays.asList("games"), "evercraft.commands.games.game").register());

            this.registeredGames.add(new PvpGame("plainspvp", "plainspvp", "pvp"));
            this.registeredGames.add(new PvpGame("endpvp", "endpvp", "end"));
            this.registeredGames.add(new PvpGame("crystalpvp", "crystalpvp", "crystal"));
            this.registeredGames.add(new PvpGame("ventpvp", "ventpvp", "vents"));
            this.registeredGames.add(new PvpGame("sumo", "sumo", "sumo"));

            this.registeredGames.add(new DodgeBowGame("dodgebow", "dodgebow", "dodgebow-runners", "dodgebow-bowers", "dodgebow"));

            this.registeredGames.add(new BoatRaceGame("boatrace", "boatrace", "boatrace"));
        }

        this.getLogger().info("Finished loading games");

        this.getLogger().info("Loading other assets..");

        this.assets = new ArrayList<Closable>();

        for (Player player : this.getServer().getOnlinePlayers()) {
            player.displayName(ComponentFormatter.stringToComponent(TextFormatter.translateColors(SpigotPlayerResolver.getDisplayName(data, player.getUniqueId()))));
            player.playerListName(player.displayName());
        }

        this.getLogger().info("Finished loading other assets..");

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

        data.close();

        this.getLogger().info("Finished closing player data..");

        this.getLogger().info("Unregistering commands..");

        for (SpigotCommand command : this.commands) {
            command.unregister();
        }

        this.getLogger().info("Finished unregistering commands..");

        this.getLogger().info("Unregistering listeners..");

        HandlerList.unregisterAll(this);

        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this);

        this.getLogger().info("Finished unregistering listeners..");

        this.getLogger().info("Closing assets..");

        for (Closable asset : this.assets) {
            asset.close();
        }

        for (Player player : this.getServer().getOnlinePlayers()) {
            player.displayName(null);
            player.playerListName(null);
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

    public MySQLConfig getPluginData() {
        return this.data;
    }

    public FileConfig getWarps() {
        return this.warps;
    }

    public FileConfig getKits() {
        return this.kits;
    }

    public FileConfig getChests() {
        return this.chests;
    }

    public Economy getEconomy() {
        return this.economy;
    }

    public List<SpigotCommand> getCommands() {
        return this.commands;
    }

    public List<SpigotListener> getListeneres() {
        return this.listeners;
    }

    public List<Closable> getAssets() {
        return this.assets;
    }

    public FileConfig getGames() {
        return this.games;
    }

    public List<Game> getRegisteredGames() {
        return this.registeredGames;
    }

    public String getServerName() {
        return this.serverName;
    }

    public void setServerName(String value) {
        this.serverName = value;

        this.config.set("serverName", value);
    }
}